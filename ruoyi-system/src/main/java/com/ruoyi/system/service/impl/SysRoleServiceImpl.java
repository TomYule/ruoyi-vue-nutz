package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import cn.hutool.core.util.ObjectUtil;
import com.ruoyi.common.core.page.TableData;
import com.ruoyi.common.core.service.BaseServiceImpl;
import com.ruoyi.system.service.ISysRoleDeptService;
import com.ruoyi.system.service.ISysRoleMenuService;
import com.ruoyi.system.service.ISysUserRoleService;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Lang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.SysRoleDept;
import com.ruoyi.system.domain.SysRoleMenu;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.service.ISysRoleService;

/**
 * 角色 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRole> implements ISysRoleService {

    @Autowired
    private ISysUserRoleService sysUserRoleService;
    @Autowired
    private ISysRoleMenuService sysRoleMenuService;
    @Autowired
    private ISysRoleDeptService sysRoleDeptService;

    public Cnd queryWrapper(SysRole sysRole) {
        Cnd cnd = Cnd.NEW();
        if (Lang.isNotEmpty(sysRole)) {
            if (Lang.isNotEmpty(sysRole.getRoleName())) {
                cnd.and("role_name", "like", "%" + sysRole.getRoleName() + "%");
            }
            if (Lang.isNotEmpty(sysRole.getRoleKey())) {
                cnd.and("role_key", "=", sysRole.getRoleKey());
            }
            if (Lang.isNotEmpty(sysRole.getRoleSort())) {
                cnd.and("role_sort", "=", sysRole.getRoleSort());
            }
            if (Lang.isNotEmpty(sysRole.getDataScope())) {
                cnd.and("data_scope", "=", sysRole.getDataScope());
            }
//            if (Lang.isNotEmpty(sysRole.getMenuCheckStrictly())){
//                cnd.and("menu_check_strictly" , "=" , sysRole.getMenuCheckStrictly());
//            }
//            if (Lang.isNotEmpty(sysRole.getDeptCheckStrictly())){
//                cnd.and("dept_check_strictly" , "=" , sysRole.getDeptCheckStrictly());
//            }
            if (Lang.isNotEmpty(sysRole.getStatus())) {
                cnd.and("status", "=", sysRole.getStatus());
            }
        }
        return cnd;
    }

    @Override
    public List<SysRole> query(SysRole sysRole) {
        return this.query(queryWrapper(sysRole));
    }

    @Override
    public TableData<SysRole> query(SysRole sysRole, int pageNumber, int pageSize) {
        return this.queryTable(queryWrapper(sysRole), pageNumber, pageSize);
    }

    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysRole> selectRoleList(SysRole role) {
        return this.query();
    }

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRolesByUserId(Long userId) {
        String sqlstr = " select distinct r.role_id, r.role_name, r.role_key, r.role_sort, r.data_scope, r.menu_check_strictly, r.dept_check_strictly, " +
                " r.status, r.del_flag, r.create_time, r.remark  " +
                " from sys_role r " +
                " left join sys_user_role ur on ur.role_id = r.role_id " +
                " left join sys_user u on u.user_id = ur.user_id " +
                " left join sys_dept d on u.dept_id = d.dept_id " +
                " WHERE r.del_flag = '0' and ur.user_id = @userId";
        Sql sql = Sqls.create(sqlstr);
        sql.params().set("userId", userId);
        sql.setCallback(Sqls.callback.entities());
        Entity<SysRole> entity = dao().getEntity(SysRole.class);
        sql.setEntity(entity);
        dao().execute(sql);
        List<SysRole> userRoles = sql.getList(SysRole.class);

        List<SysRole> roles = selectRoleAll();
        for (SysRole role : roles) {
            for (SysRole userRole : userRoles) {
                if (role.getRoleId().longValue() == userRole.getRoleId().longValue()) {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        String sqlstr = " select distinct r.role_id, r.role_name, r.role_key, r.role_sort, r.data_scope," +
                " r.menu_check_strictly, r.dept_check_strictly, r.status, r.del_flag, r.create_time, r.remark  " +
                " from sys_role r " +
                " left join sys_user_role ur on ur.role_id = r.role_id " +
                " left join sys_user u on u.user_id = ur.user_id " +
                " WHERE r.del_flag = '0' and ur.user_id = @userId";
        Sql sql = Sqls.create(sqlstr);
        sql.params().set("userId", userId);
        sql.setCallback(Sqls.callback.entities());
        Entity<SysRole> entity = dao().getEntity(SysRole.class);
        sql.setEntity(entity);
        dao().execute(sql);
        List<SysRole> perms = sql.getList(SysRole.class);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (StringUtils.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRoleAll() {
        return SpringUtils.getAopProxy(this).selectRoleList(new SysRole());
    }

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    @Override
    public List<Long> selectRoleListByUserId(Long userId) {
        String sqlstr = " select r.role_id " +
                " from sys_role r " +
                " left join sys_user_role ur on ur.role_id = r.role_id " +
                " left join sys_user u on u.user_id = ur.user_id " +
                " where u.user_id = @userId";
        Sql sql = Sqls.fetchLong(sqlstr);
        sql.params().set("userId", userId);
        sql.setCallback(Sqls.callback.longs());;
        dao().execute(sql);
        return sql.getList(Long.class);
    }

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public SysRole selectRoleById(Long roleId) {
        return fetch(roleId);
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleNameUnique(SysRole role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = fetch(Cnd.where("role_name","=",role.getRoleName()));
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleKeyUnique(SysRole role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = this.fetch(Cnd.where("role_key", "=", role.getRoleKey()));
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(SysRole role) {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员角色");
        }
    }

    /**
     * 校验角色是否有数据权限
     *
     * @param roleId 角色id
     */
    @Override
    public void checkRoleDataScope(Long roleId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            SysRole role = new SysRole();
            role.setRoleId(roleId);
            List<SysRole> roles = SpringUtils.getAopProxy(this).selectRoleList(role);
            if (StringUtils.isEmpty(roles)) {
                throw new ServiceException("没有权限访问角色数据！");
            }
        }
    }

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int countUserRoleByRoleId(Long roleId) {
        return sysUserRoleService.count(Cnd.where("role_id", "=", roleId));
    }

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertRole(SysRole role) {
        // 新增角色信息
        insert(role);
        return insertRoleMenu(role);
    }

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateRole(SysRole role) {
        // 修改角色信息
        update(role);
        // 删除角色与菜单关联
        sysRoleMenuService.delete(Cnd.where("role_id", "=", role.getRoleId()));
        return insertRoleMenu(role);
    }

    /**
     * 修改角色状态
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public int updateRoleStatus(SysRole role) {
        return updateRole(role);
    }

    /**
     * 修改数据权限信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int authDataScope(SysRole role) {
        // 修改角色信息
        updateRole(role);
        // 删除角色与部门关联
        sysRoleMenuService.delete(Cnd.where("role_id", "=", role.getRoleId()));
        // 新增角色和部门信息（数据权限）
        return insertRoleDept(role);
    }

    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    public int insertRoleMenu(SysRole role) {
        AtomicInteger rows = new AtomicInteger(1);
        // 新增用户与角色管理
        List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
        for (Long menuId : role.getMenuIds()) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0) {
            list.stream().forEach(sysRoleMenu -> {
                sysRoleMenuService.insert(sysRoleMenu);
                rows.set(rows.getAndIncrement() + 1);
            });
        }
        return rows.get();
    }

    /**
     * 新增角色部门信息(数据权限)
     *
     * @param role 角色对象
     */
    public int insertRoleDept(SysRole role) {
        AtomicInteger rows = new AtomicInteger(1);
        // 新增角色与部门（数据权限）管理
        List<SysRoleDept> list = new ArrayList<SysRoleDept>();
        for (Long deptId : role.getDeptIds()) {
            SysRoleDept rd = new SysRoleDept();
            rd.setRoleId(role.getRoleId());
            rd.setDeptId(deptId);
            list.add(rd);
        }
        if (list.size() > 0) {
            list.stream().forEach(roleDept -> {
                sysRoleDeptService.insert(roleDept);
                rows.set(rows.getAndIncrement() + 1);
            });
        }
        return rows.get();
    }

    /**
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleById(Long roleId) {
        // 删除角色与菜单关联
        sysRoleMenuService.delete(Cnd.where("role_id", "=", roleId));
        // 删除角色与部门关联
        sysRoleDeptService.delete(Cnd.where("role_id", "=", roleId));
        return delete(roleId);
    }

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleByIds(Long[] roleIds) {
        for (Long roleId : roleIds) {
            checkRoleAllowed(new SysRole(roleId));
            checkRoleDataScope(roleId);
            SysRole role = selectRoleById(roleId);
            role = fetchLinks(role,"users");
            if (ObjectUtil.isNotNull(role.getUsers()) && role.getUsers().size() > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        // 删除角色与菜单关联
        sysRoleMenuService.delete(Cnd.where("role_id", "in", roleIds));
        // 删除角色与部门关联
        sysRoleDeptService.delete(Cnd.where("role_id", "in", roleIds));
        return delete(roleIds);
    }

    /**
     * 取消授权用户角色
     *
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    @Override
    public int deleteAuthUser(SysUserRole userRole) {
        return sysUserRoleService.delete(Cnd.where("user_id", "=", userRole.getUserId())
                .and("role_id", "=", userRole.getRoleId()));
    }

    /**
     * 批量取消授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要取消授权的用户数据ID
     * @return 结果
     */
    @Override
    public int deleteAuthUsers(Long roleId, Long[] userIds) {
        return sysUserRoleService.delete(Cnd.where("user_id", "in", userIds)
                .and("role_id", "in", roleId));
    }

    /**
     * 批量选择授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要授权的用户数据ID
     * @return 结果
     */
    @Override
    public int insertAuthUsers(Long roleId, Long[] userIds) {
        // 新增用户与角色管理
        List<SysUserRole> list = new ArrayList<SysUserRole>();
        for (Long userId : userIds) {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        int row = 0;
        for (SysUserRole userRole : list) {
            sysUserRoleService.insert(userRole);
            row++;
        }
        return row;
    }
}
