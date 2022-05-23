package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.core.service.BaseServiceImpl;
import org.nutz.dao.Cnd;
import org.nutz.lang.Lang;

import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.SysUserPost;
import com.ruoyi.system.service.ISysUserPostService;

/**
 * 用户与岗位关联Service业务层处理
 *
 * @author ruoyi
 * @date 2022-05-23
 */
@Service
public class SysUserPostServiceImpl extends BaseServiceImpl<SysUserPost> implements ISysUserPostService {
    public Cnd queryWrapper(SysUserPost sysUserPost) {
        Cnd cnd = Cnd.NEW();
        if (Lang.isNotEmpty(sysUserPost)){
        }
        return cnd;
    }

    @Override
    public List<SysUserPost> query(SysUserPost sysUserPost) {
        return this.query(queryWrapper(sysUserPost));
    }

    @Override
    public List<SysUserPost> query(SysUserPost sysUserPost, int pageNumber, int pageSize) {
        return this.query(queryWrapper(sysUserPost), pageNumber, pageSize);
    }
}
