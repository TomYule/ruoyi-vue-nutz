package com.ruoyi.common.core.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import org.nutz.dao.entity.annotation.*;
import com.ruoyi.common.core.domain.BaseModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单权限对象 sys_menu
 *
 * @author ruoyi
 * @date 2022-05-23
 */
@Table("sys_menu")
public class SysMenu extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @Id
    @ColDefine(type = ColType.INT, width = 10)
    @Column("menu_id")
    @Comment("菜单ID")
    private Long menuId;

    /**
     * 菜单名称
     */
    @Column("menu_name")
    @Comment("菜单名称")
    @Excel(name = "菜单名称")
    private String menuName;

    /**
     * 父菜单名称
     */
    private String parentName;

    /**
     * 父菜单ID
     */
    @Column("parent_id")
    @Comment("父菜单ID")
    @Excel(name = "父菜单ID")
    private Long parentId;

    /**
     * 显示顺序
     */
    @Column("order_num")
    @Comment("显示顺序")
    @Excel(name = "显示顺序")
    private Integer orderNum;

    /**
     * 路由地址
     */
    @Column("path")
    @Comment("路由地址")
    @Excel(name = "路由地址")
    private String path;

    /**
     * 组件路径
     */
    @Column("component")
    @Comment("组件路径")
    @Excel(name = "组件路径")
    private String component;

    /**
     * 路由参数
     */
    @Column("query")
    @Comment("路由参数")
    @Excel(name = "路由参数")
    private String query;

    /**
     * 是否为外链（0是 1否）
     */
    @Column("is_frame")
    @Comment("是否为外链（0是 1否）")
    @Excel(name = "是否为外链" , readConverterExp = "0=是,1=否")
    private String isFrame;

    /**
     * 是否缓存（0缓存 1不缓存）
     */
    @Column("is_cache")
    @Comment("是否缓存（0缓存 1不缓存）")
    @Excel(name = "是否缓存" , readConverterExp = "0=缓存,1=不缓存")
    private String isCache;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    @Column("menu_type")
    @Comment("菜单类型（M目录 C菜单 F按钮）")
    @Excel(name = "菜单类型" , readConverterExp = "M=目录,C=菜单,F=按钮")
    private String menuType;

    /**
     * 菜单状态（0显示 1隐藏）
     */
    @Column("visible")
    @Comment("菜单状态（0显示 1隐藏）")
    @Excel(name = "菜单状态" , readConverterExp = "0=显示,1=隐藏")
    private String visible;

    /**
     * 菜单状态（0正常 1停用）
     */
    @Column("status")
    @Comment("菜单状态（0正常 1停用）")
    @Excel(name = "菜单状态" , readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 权限标识
     */
    @Column("perms")
    @Comment("权限标识")
    @Excel(name = "权限标识")
    private String perms;

    /**
     * 菜单图标
     */
    @Column("icon")
    @Comment("菜单图标")
    @Excel(name = "菜单图标")
    private String icon;

    /**
     * 子菜单
     */
    private List<SysMenu> children = new ArrayList<SysMenu>();

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    @NotBlank(message = "菜单名称不能为空")
    @Size(min = 0, max = 50, message = "菜单名称长度不能超过50个字符")
    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @NotNull(message = "显示顺序不能为空")
    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @Size(min = 0, max = 200, message = "路由地址不能超过200个字符")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Size(min = 0, max = 200, message = "组件路径不能超过255个字符")
    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getIsFrame() {
        return isFrame;
    }

    public void setIsFrame(String isFrame) {
        this.isFrame = isFrame;
    }

    public String getIsCache() {
        return isCache;
    }

    public void setIsCache(String isCache) {
        this.isCache = isCache;
    }

    @NotBlank(message = "菜单类型不能为空")
    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Size(min = 0, max = 100, message = "权限标识长度不能超过100个字符")
    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<SysMenu> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenu> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("menuId" , getMenuId())
                .append("menuName" , getMenuName())
                .append("parentId" , getParentId())
                .append("orderNum" , getOrderNum())
                .append("path" , getPath())
                .append("component" , getComponent())
                .append("isFrame" , getIsFrame())
                .append("IsCache" , getIsCache())
                .append("menuType" , getMenuType())
                .append("visible" , getVisible())
                .append("status " , getStatus())
                .append("perms" , getPerms())
                .append("icon" , getIcon())
                .append("createBy" , getCreateBy())
                .append("createTime" , getCreateTime())
                .append("updateBy" , getUpdateBy())
                .append("updateTime" , getUpdateTime())
                .append("remark" , getRemark())
                .toString();
    }
}
