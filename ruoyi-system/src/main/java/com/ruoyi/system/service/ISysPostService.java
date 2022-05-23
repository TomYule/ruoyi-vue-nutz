package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.common.core.service.BaseService;
import com.ruoyi.system.domain.SysPost;

/**
 * 岗位信息Service接口
 *
 * @author ruoyi
 * @date 2022-05-23
 */
public interface ISysPostService extends BaseService<SysPost> {

    List<SysPost> query(SysPost post);

    List<SysPost> query(SysPost post,int pageNumber, int pageSize);
    /**
     * 校验岗位名称
     *
     * @param post 岗位信息
     * @return 结果
     */
    public String checkPostNameUnique(SysPost post);

    /**
     * 校验岗位编码
     *
     * @param post 岗位信息
     * @return 结果
     */
    public String checkPostCodeUnique(SysPost post);


    /**
     * 根据用户ID获取岗位选择框列表
     *
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */
    public List<Long> selectPostListByUserId(Long userId);
}
