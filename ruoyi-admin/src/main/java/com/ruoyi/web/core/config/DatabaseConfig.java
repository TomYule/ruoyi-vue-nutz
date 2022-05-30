package com.ruoyi.web.core.config;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.quartz.domain.SysJob;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.service.ISysUserService;
import org.nutz.dao.Dao;
import org.nutz.dao.FieldFilter;
import org.nutz.dao.FieldMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

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

    @PostConstruct
    public void init() {
//        String path = "D:\\IdeaProjects\\ruoyi-vue-nutz\\ruoyi-framework\\target";
//        List<SysConfig> configList = dao.query(SysConfig.class, Cnd.NEW());
//        try {
//            FileUtils.writeFileBytes(JSON.toJSONString(configList).getBytes(),(path+ "\\config.json"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //若必要的数据不存在，则初始化数据库
        if (0 == dao.count(SysUser.class)) {
            try {
                String data = FileUtils.getFileData("db/menu.json");
                List<SysMenu> menuList = JSON.parseArray(data, SysMenu.class);
                for (SysMenu menu : menuList) {
                    dao.insert(menu, FieldFilter.create(SysMenu.class, FieldMatcher.create(false)));
                }
                String roleJson = FileUtils.getFileData("db/role.json");
                List<SysRole> roleList = JSON.parseArray(roleJson, SysRole.class);
                for (SysRole role : roleList) {
                    dao.fastInsert(role);
                    if ("admin".equals(role.getRoleKey())) {
                        role.setMenus(menuList);
                        dao.insertRelation(role, "menus");
                    }
                }
                String userJson = FileUtils.getFileData("db/user.json");
                List<SysUser> userList = JSON.parseArray(userJson, SysUser.class);
                for (SysUser user : userList) {
                    dao.insert(user, FieldFilter.create(SysUser.class, FieldMatcher.create(false)));
                    if ("admin".equals(user.getUserName())) {
                        user.setPassword("admin123");
                        userService.resetUserPwd("admin", "admin123");
                        user.setRoles(roleList);
                        dao.insertRelation(user, "roles");
                    }
                }

                String deptJson = FileUtils.getFileData("db/dept.json");
                List<SysDept> deptList = JSON.parseArray(deptJson, SysDept.class);
                for (SysDept d : deptList) {
                    dao.insert(d, FieldFilter.create(SysDept.class, FieldMatcher.create(false)));
                }
                String configJson = FileUtils.getFileData("db/config.json");
                List<SysConfig> configList =JSON.parseArray(configJson, SysConfig.class);

                for(SysConfig config:configList){
                    dao.insert(config);
                }

                String taskJson = FileUtils.getFileData("db/job.json");
                List<SysJob> jobList = JSON.parseArray(taskJson, SysJob.class);
                for (SysJob j : jobList) {
                    dao.insert(j);
                }

                String dictTypeJson = FileUtils.getFileData("db/dictType.json");
                List<SysDictType> dictTypeList = JSON.parseArray(dictTypeJson, SysDictType.class);
                for (SysDictType dictType : dictTypeList) {
                    dao.insert(dictType, FieldFilter.create(SysDictType.class, FieldMatcher.create(false)));
                }

                String dictDataJson = FileUtils.getFileData("db/dictData.json");
                List<SysDictData> dictDataList = JSON.parseArray(dictDataJson, SysDictData.class);
                for (SysDictData dictData : dictDataList) {
                    dao.insert(dictData, FieldFilter.create(SysDictData.class, FieldMatcher.create(false)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
