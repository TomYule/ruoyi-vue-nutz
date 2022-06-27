package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.page.TableData;
import com.ruoyi.common.core.service.BaseServiceImpl;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Lang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.service.ISysDeptService;

/**
 * 部门管理 服务实现
 *
 * @author ruoyi
 */
@Service
public class SysDeptServiceImpl extends BaseServiceImpl<SysDept> implements ISysDeptService {
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysUserService userService;

    public Cnd queryWrapper(SysDept sysDept) {
        Cnd cnd = Cnd.NEW();
        if (Lang.isNotEmpty(sysDept)) {
            if (Lang.isNotEmpty(sysDept.getParentId())) {
                cnd.and("parent_id" , "=" , sysDept.getParentId());
            }
            if (Lang.isNotEmpty(sysDept.getAncestors())) {
                cnd.and("ancestors" , "=" , sysDept.getAncestors());
            }
            if (Lang.isNotEmpty(sysDept.getDeptName())) {
                cnd.and("dept_name" , "like" , "%" + sysDept.getDeptName() + "%");
            }
            if (Lang.isNotEmpty(sysDept.getOrderNum())) {
                cnd.and("order_num" , "=" , sysDept.getOrderNum());
            }
            if (Lang.isNotEmpty(sysDept.getLeader())) {
                cnd.and("leader" , "=" , sysDept.getLeader());
            }
            if (Lang.isNotEmpty(sysDept.getPhone())) {
                cnd.and("phone" , "=" , sysDept.getPhone());
            }
            if (Lang.isNotEmpty(sysDept.getEmail())) {
                cnd.and("email" , "=" , sysDept.getEmail());
            }
            if (Lang.isNotEmpty(sysDept.getStatus())) {
                cnd.and("status" , "=" , sysDept.getStatus());
            }
        }
        return cnd;
    }

    @Override
    public List<SysDept> query(SysDept sysDept) {
        return this.query(queryWrapper(sysDept));
    }

    @Override
    public TableData<SysDept> query(SysDept sysDept, int pageNumber, int pageSize) {
        return this.queryTable(queryWrapper(sysDept), pageNumber, pageSize);
    }

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysDept> selectDeptList(SysDept dept) {
        return query(dept);
    }

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts) {
        List<SysDept> returnList = new ArrayList<SysDept>();
        List<Long> tempList = new ArrayList<Long>();
        for (SysDept dept : depts) {
            tempList.add(dept.getDeptId());
        }
        for (SysDept dept : depts) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }

    @Override
    public List<SysDept> insertTree(List<SysDept> depts, Long parentId) {
        if(Lang.isNotEmpty(depts) && depts.size() > 0){
            for(SysDept d:depts){
                if(Lang.isNotEmpty(parentId)){
                    d.setParentId(parentId);
                }
                insert(d);
                if(Lang.isNotEmpty(d.getChildren()) && d.getChildren().size()> 0){
                    insertTree(d.getChildren(),d.getDeptId());
                }
            }
        }
        return depts;
    }


    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts) {
        List<SysDept> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    @Override
    public List<Long> selectDeptListByRoleId(Long roleId) {
        SysRole role = roleService.fetch(roleId);
        String sqlstr = " select d.dept_id " +
                " from sys_dept d " +
                " left join sys_role_dept rd on d.dept_id = rd.dept_id " +
                " where rd.role_id = @roleId ";

        if (role.isDeptCheckStrictly()) {
            sqlstr += " and d.dept_id not in (select d.parent_id from sys_dept d inner join sys_role_dept rd on d.dept_id = rd.dept_id and rd.role_id = @roleId ) ";
        }
        sqlstr += " order by d.parent_id, d.order_num";
        Sql sql = Sqls.fetchLong(sqlstr);
        sql.params().set("roleId" , roleId);
        sql.setCallback(Sqls.callback.longs());
        dao().execute(sql);
        return sql.getList(Long.class);
    }

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Override
    public SysDept selectDeptById(Long deptId) {
        return fetch(deptId);
    }

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Override
    public int selectNormalChildrenDeptById(Long deptId) {
        Cnd cnd = Cnd.NEW();
        cnd.where().andInBySql("dept_id" , "SELECT dept_id FROM sys_dept  WHERE FIND_IN_SET ('%s',ancestors)" , deptId);
        cnd.and("status" , "=" , "0");
        cnd.and("del_flag" , "=" , "0");
        return count(cnd);
    }

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(Long deptId) {
        int result = count(Cnd.where("parent_id" , "=" , deptId).and("del_flag" , "=" , "0"));
        return result > 0;
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(Long deptId) {
        int result = userService.count(Cnd.where("dept_id" , "=" , deptId));
        return result > 0;
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public String checkDeptNameUnique(SysDept dept) {
        Long deptId = StringUtils.isNull(dept.getDeptId()) ? -1L : dept.getDeptId();
        SysDept info = fetch(Cnd.where("dept_name" , "=" , dept.getDeptName())
                .and("parent_id" , "=" , dept.getParentId()));
        if (StringUtils.isNotNull(info) && info.getDeptId().longValue() != deptId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验部门是否有数据权限
     *
     * @param deptId 部门id
     */
    @Override
    public void checkDeptDataScope(Long deptId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            SysDept dept = new SysDept();
            dept.setDeptId(deptId);
            List<SysDept> depts = SpringUtils.getAopProxy(this).selectDeptList(dept);
            if (StringUtils.isEmpty(depts)) {
                throw new ServiceException("没有权限访问部门数据！");
            }
        }
    }

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int insertDept(SysDept dept) {
        SysDept info = fetch(dept.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
            throw new ServiceException("部门停用，不允许新增");
        }
        dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        return Lang.isNotEmpty(insert(dept).getDeptId()) ? 1 : 0;
    }

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int updateDept(SysDept dept) {
        SysDept newParentDept = fetch(dept.getParentId());
        SysDept oldDept = fetch(dept.getDeptId());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept)) {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
        }
        int result = update(dept);
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus()) && StringUtils.isNotEmpty(dept.getAncestors())
                && !StringUtils.equals("0" , dept.getAncestors())) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatusNormal(dept);
        }
        return result;
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatusNormal(SysDept dept) {
        String ancestors = dept.getAncestors();
        Long[] deptIds = Convert.toLongArray(ancestors);
        update(Chain.make("status" , "0"), Cnd.where("dept_id" , "in" , deptIds));
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */

    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        if (!newAncestors.equals(oldAncestors)) {
            Cnd cnd = Cnd.NEW();
            cnd.where().andInBySql("dept_id", "SELECT dept_id FROM sys_dept  WHERE FIND_IN_SET ('%s',ancestors)", deptId);
            List<SysDept> children = query(cnd);
            for (SysDept child : children) {
                child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
            }
            if (children.size() > 0) {
                String sqlstr = "update sys_dept set ancestors = case dept_id ";
                List<Long> ids = new ArrayList<>();
                for (SysDept child : children) {
                    sqlstr += " when " + child.getDeptId() + " then '" + child.getAncestors()+ "'";
                    ids.add(child.getDeptId());
                }
                sqlstr += " end where dept_id in (@deptId)";
                Sql sql = Sqls.create(sqlstr);
                sql.params().set("deptId", ids);
                dao().execute(sql);
            }
        }
    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public int deleteDeptById(Long deptId) {
        return delete(deptId);
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysDept> list, SysDept t) {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysDept> getChildList(List<SysDept> list, SysDept t) {
        List<SysDept> tlist = new ArrayList<SysDept>();
        Iterator<SysDept> it = list.iterator();
        while (it.hasNext()) {
            SysDept n = (SysDept) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getDeptId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDept> list, SysDept t) {
        return getChildList(list, t).size() > 0;
    }
}
