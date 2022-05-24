package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.page.TableData;
import com.ruoyi.common.core.service.BaseServiceImpl;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Lang;
import org.springframework.stereotype.Service;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.service.ISysPostService;

import java.util.List;

/**
 * 岗位信息 服务层处理
 *
 * @author ruoyi
 */
@Service
public class SysPostServiceImpl extends BaseServiceImpl<SysPost> implements ISysPostService {

    public Cnd queryWrapper(SysPost sysPost) {
        Cnd cnd = Cnd.NEW();
        if (Lang.isNotEmpty(sysPost)) {
            if (Lang.isNotEmpty(sysPost.getPostCode())) {
                cnd.and("post_code", "=", sysPost.getPostCode());
            }
            if (Lang.isNotEmpty(sysPost.getPostName())) {
                cnd.and("post_name", "like", "%" + sysPost.getPostName() + "%");
            }
            if (Lang.isNotEmpty(sysPost.getPostSort())) {
                cnd.and("post_sort", "=", sysPost.getPostSort());
            }
            if (Lang.isNotEmpty(sysPost.getStatus())) {
                cnd.and("status", "=", sysPost.getStatus());
            }
        }
        return cnd;
    }

    @Override
    public List<SysPost> query(SysPost sysPost) {
        return this.query(queryWrapper(sysPost));
    }

    @Override
    public TableData<SysPost> query(SysPost sysPost, int pageNumber, int pageSize) {
        return this.queryTable(queryWrapper(sysPost), pageNumber, pageSize);
    }

    /**
     * 校验岗位名称是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public String checkPostNameUnique(SysPost post) {
        Long postId = StringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();
        SysPost info = this.fetch(post.getPostName());
        if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验岗位编码是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public String checkPostCodeUnique(SysPost post) {
        Long postId = StringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();
        SysPost info = this.fetch(Cnd.where("post_code", "=", post.getPostCode()));
        if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public List<Long> selectPostListByUserId(Long userId) {
        try {
            String sqlstr = " select p.post_id " +
                    " from sys_post p " +
                    " left join sys_user_post up on up.post_id = p.post_id " +
                    " left join sys_user u on u.user_id = up.user_id " +
                    " where u.user_id = @userId";
            Sql sql = Sqls.fetchLong(sqlstr);
            sql.params().set("userId", userId);
            sql.setCallback(Sqls.callback.longs());
            dao().execute(sql);
            return sql.getList(Long.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
