package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import org.nutz.dao.entity.annotation.*;
import com.ruoyi.common.core.domain.BaseModel;

import java.io.Serializable;

/**
 * 角色和部门关联对象 sys_role_dept
 *
 * @author ruoyi
 * @date 2022-05-23
 */
@Table("sys_role_dept")
public class SysRoleDept implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    @Comment("id")
    private Long id;

    /**
     * 角色ID
     */
    @Column("role_id")
    @Comment("角色ID")
    private Long roleId;

    /**
     * 部门ID
     */
    @Column("dept_id")
    @Comment("部门ID")
    private Long deptId;

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("roleId" , getRoleId())
                .append("deptId" , getDeptId())
                .toString();
    }
}
