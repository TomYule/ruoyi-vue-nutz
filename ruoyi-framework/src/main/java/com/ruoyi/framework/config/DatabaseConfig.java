package com.ruoyi.framework.config;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SysMenu;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @author haiming yu
 * @date Created in 2022/5/27 18:02
 */
@Configuration
public class DatabaseConfig {
    @Autowired
    private Dao dao;

    @PostConstruct
    public void init() {
        // 若必要的数据不存在，则初始化数据库
//        String path = "D:\\IdeaProjects\\ruoyi-vue-nutz\\ruoyi-framework\\target";
//        List<SysDept> list = dao.query(SysDept.class, Cnd.NEW());
//        byte[] sourceByte = JSON.toJSONString(list).getBytes();
//        if(null != sourceByte){
//            try {
//                //文件路径（路径+文件名）
//                File file = new File(path+ "\\dept.json");
//                //文件不存在则创建文件，先创建目录
//                if (!file.exists()) {
//                    File dir = new File(file.getParent());
//                    dir.mkdirs();
//                    file.createNewFile();
//                }
//                //文件输出流用于将数据写入文件
//                FileOutputStream outStream = new FileOutputStream(file);
//                outStream.write(sourceByte);
//                //关闭文件输出流
//                outStream.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        if (0 == dao.count(SysUser.class)) {

        }
    }

}
