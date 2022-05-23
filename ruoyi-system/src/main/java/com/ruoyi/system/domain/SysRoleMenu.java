package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import org.nutz.dao.entity.annotation.*;
import com.ruoyi.common.core.domain.BaseModel;
import org.nutz.plugin.spring.boot.service.entity.DataBaseEntity;

import java.io.Serializable;

/**
 * 角色和菜单关联对象 sys_role_menu
 *
 * @author ruoyi
 * @date 2022-05-23
 */
@Table("sys_role_menu")
public class SysRoleMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @Column("role_id")
    @Comment("角色ID")
    private Long roleId;

    /**
     * 菜单ID
     */
    @Column("menu_id")
    @Comment("菜单ID")
    private Long menuId;

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getMenuId() {
        return menuId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("roleId" , getRoleId())
                .append("menuId" , getMenuId())
                .toString();
    }
}
