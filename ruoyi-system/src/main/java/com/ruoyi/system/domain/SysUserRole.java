package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 用户和角色关联对象 sys_user_role
 *
 * @author ruoyi
 * @date 2022-05-23
 */
@Table("sys_user_role")
public class SysUserRole implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    @Comment("id")
    private Long id;

    /**
     * 用户ID
     */
    @Column("user_id")
    @Comment("用户ID")
    private Long userId;

    /**
     * 角色ID
     */
    @Column("role_id")
    @Comment("角色ID")
    private Long roleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return roleId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userId" , getUserId())
                .append("roleId" , getRoleId())
                .toString();
    }
}
