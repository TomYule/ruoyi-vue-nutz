package com.ruoyi.web.core.config;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.quartz.domain.SysJob;
import com.ruoyi.system.domain.SysMenu;
import com.ruoyi.system.service.ISysUserService;
import org.nutz.dao.Dao;
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
         //若必要的数据不存在，则初始化数据库
        if (0 == dao.count(SysUser.class)) {
            String data = FileUtils.getFileData("db/menu.json");
            List<SysMenu> menuList = JSON.parseArray(data,SysMenu.class);
            for(SysMenu menu:menuList){
                dao.fastInsert(menu);
            }
            String roleJson = FileUtils.getFileData("db/role.json");
            List<SysRole> roleList = JSON.parseArray(roleJson,SysRole.class);
            for(SysRole role:roleList){
                dao.fastInsert(role);
                if("admin".equals(role.getRoleKey())){
                    role.setMenus(menuList);
                    dao.insertRelation(role, "menus");
                }
            }
            String userJson = FileUtils.getFileData("db/user.json");
            List<SysUser> userList = JSON.parseArray(userJson,SysUser.class);
            for(SysUser user:userList){
                dao.fastInsert(user);
                if("admin".equals(user.getUserName())){
                    user.setPassword("admin123");
                    userService.resetUserPwd("admin","admin123");
                    user.setRoles(roleList);
                    dao.insertRelation(user, "roles");
                }
            }

            String deptJson = FileUtils.getFileData("db/dept.json");
            List<SysDept> deptList = JSON.parseArray(deptJson,SysDept.class);
            for(SysDept d:deptList){
                dao.fastInsert(d);
            }
            String taskJson = FileUtils.getFileData("db/job.json");
            List<SysJob> jobList = JSON.parseArray(taskJson,SysJob.class);
            for(SysJob j:jobList){
                dao.fastInsert(j);
            }
        }
    }

}
