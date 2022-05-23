package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.common.core.service.BaseService;
import com.ruoyi.system.domain.SysUserPost;

/**
 * 用户与岗位关联Service接口
 *
 * @author ruoyi
 * @date 2022-05-23
 */
public interface ISysUserPostService extends BaseService<SysUserPost>{
    List<SysUserPost> query(SysUserPost sysUserPost);

    List<SysUserPost> query(SysUserPost sysUserPost,int pageNumber, int pageSize);
}
