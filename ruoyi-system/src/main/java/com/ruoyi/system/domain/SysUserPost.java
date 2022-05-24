package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 用户与岗位关联对象 sys_user_post
 *
 * @author ruoyi
 * @date 2022-05-23
 */
@Table("sys_user_post")
public class SysUserPost implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Column("user_id")
    @Comment("用户ID")
    private Long userId;

    /**
     * 岗位ID
     */
    @Column("post_id")
    @Comment("岗位ID")
    private Long postId;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userId" , getUserId())
                .append("postId" , getPostId())
                .toString();
    }
}
