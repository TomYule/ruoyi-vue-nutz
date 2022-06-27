package com.ruoyi.web.core.config;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.quartz.domain.SysJob;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.system.service.ISysUserService;
import org.nutz.dao.*;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Lang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;

/**
 * @author haiming yu
 * @date Created in 2022/5/27 18:02
 */
@Configuration
public class DatabaseConfig {
    @Autowired
    private Dao dao;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysMenuService menuService;
    @Autowired
    private ISysDeptService deptService;
    @PostConstruct
    public void init() {
//        String path = "D:\\IdeaProjects\\ruoyi-vue-nutz\\ruoyi-framework\\target";
//        List<SysConfig> configList = dao.query(SysConfig.class, Cnd.NEW());
//        try {
//            FileUtils.writeFileBytes(JSON.toJSONString(configList).getBytes(),(path+ "\\config.json"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Random random = new Random();
        //若必要的数据不存在，则初始化数据库
        if (0 == dao.count(SysUser.class)) {
            try {
                // SQL server 需要特殊处理
                if(DB.SQLSERVER.equals(dao.meta().getType())){
                    Sql sqlOff = Sqls.create("SET IDENTITY_INSERT $table OFF");
                    sqlOff.vars().set("table",dao.getEntity(SysMenu.class).getTableName());
                    dao.execute(sqlOff);
                    sqlOff.vars().set("table",dao.getEntity(SysRole.class).getTableName());
                    dao.execute(sqlOff);
                    sqlOff.vars().set("table",dao.getEntity(SysUser.class).getTableName());
                    dao.execute(sqlOff);
                    sqlOff.vars().set("table",dao.getEntity(SysDept.class).getTableName());
                    dao.execute(sqlOff);
                    sqlOff.vars().set("table",dao.getEntity(SysConfig.class).getTableName());
                    dao.execute(sqlOff);
                    sqlOff.vars().set("table",dao.getEntity(SysJob.class).getTableName());
                    dao.execute(sqlOff);
                }

                String data = FileUtils.getFileData("db/menu.json");
                List<SysMenu> menuList = JSON.parseArray(data, SysMenu.class);
                menuList = menuService.buildMenuTree(menuList);
                menuList = menuService.insertTree(menuList,null);

                String roleJson = FileUtils.getFileData("db/role.json");
                List<SysRole> roleList = JSON.parseArray(roleJson, SysRole.class);
                for (SysRole role : roleList) {
                    dao.fastInsert(role);
                    if ("admin".equals(role.getRoleKey())) {
                        role.setMenus(menuList);
                        dao.insertRelation(role, "menus");
                    }
                }


                String deptJson = FileUtils.getFileData("db/dept.json");
                List<SysDept> deptList = JSON.parseArray(deptJson, SysDept.class);
                deptList = deptService.buildDeptTree(deptList);
                deptService.insertTree(deptList,null);
                deptList = deptService.query();

                String userJson = FileUtils.getFileData("db/user.json");
                List<SysUser> userList = JSON.parseArray(userJson, SysUser.class);
                for (SysUser user : userList) {
                    SysDept dept = deptList.get(random.nextInt(deptList.size()));
                    if(Lang.isNotEmpty(dept)){
                        user.setDeptId(dept.getDeptId());
                    }
                    dao.fastInsert(user);
                    if ("admin".equals(user.getUserName())) {
                        user.setPassword("admin123");
                        userService.resetUserPwd("admin", "admin123");
                        user.setRoles(roleList);
                        dao.insertRelation(user, "roles");
                    }
                }

                String configJson = FileUtils.getFileData("db/config.json");
                List<SysConfig> configList =JSON.parseArray(configJson, SysConfig.class);

                for(SysConfig config:configList){
                    dao.fastInsert(config);
                }

                String taskJson = FileUtils.getFileData("db/job.json");
                List<SysJob> jobList = JSON.parseArray(taskJson, SysJob.class);
                for (SysJob j : jobList) {
                    dao.fastInsert(j);
                }

                String dictTypeJson = FileUtils.getFileData("db/dictType.json");
                List<SysDictType> dictTypeList = JSON.parseArray(dictTypeJson, SysDictType.class);
                for (SysDictType dictType : dictTypeList) {
                    dao.fastInsert(dictType);
                }

                String dictDataJson = FileUtils.getFileData("db/dictData.json");
                List<SysDictData> dictDataList = JSON.parseArray(dictDataJson, SysDictData.class);
                for (SysDictData dictData : dictDataList) {
                    dao.fastInsert(dictData);
                }

                if(DB.SQLSERVER.equals(dao.meta().getType())){
                    Sql sqlOn = Sqls.create("SET IDENTITY_INSERT $table ON");
                    sqlOn.vars().set("table",dao.getEntity(SysMenu.class).getTableName());
                    dao.execute(sqlOn);
                    sqlOn.vars().set("table",dao.getEntity(SysRole.class).getTableName());
                    dao.execute(sqlOn);
                    sqlOn.vars().set("table",dao.getEntity(SysUser.class).getTableName());
                    dao.execute(sqlOn);
                    sqlOn.vars().set("table",dao.getEntity(SysDept.class).getTableName());
                    dao.execute(sqlOn);
                    sqlOn.vars().set("table",dao.getEntity(SysConfig.class).getTableName());
                    dao.execute(sqlOn);
                    sqlOn.vars().set("table",dao.getEntity(SysJob.class).getTableName());
                    dao.execute(sqlOn);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
