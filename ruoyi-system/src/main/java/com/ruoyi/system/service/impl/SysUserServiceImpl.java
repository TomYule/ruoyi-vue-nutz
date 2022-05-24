package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Validator;

import com.ruoyi.common.core.page.TableData;
import com.ruoyi.common.core.service.BaseServiceImpl;
import com.ruoyi.system.service.*;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.domain.SysUserPost;
import com.ruoyi.system.domain.SysUserRole;

/**
 * 用户 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUser> implements ISysUserService {

    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private ISysUserRoleService userRoleService;

    @Autowired
    private ISysUserPostService userPostService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    protected Validator validator;


    public Cnd queryWrapper(SysUser sysUser) {
        Cnd cnd = Cnd.NEW();
        if (Lang.isNotEmpty(sysUser)){
            if (Lang.isNotEmpty(sysUser.getDeptId())){
                cnd.and("dept_id" , "=" , sysUser.getDeptId());
            }
            if (Lang.isNotEmpty(sysUser.getUserName())){
                cnd.and("user_name" , "like" , "%" + sysUser.getUserName() + "%");
            }
            if (Lang.isNotEmpty(sysUser.getNickName())){
                cnd.and("nick_name" , "like" , "%" + sysUser.getNickName() + "%");
            }
            if (Lang.isNotEmpty(sysUser.getEmail())){
                cnd.and("email" , "=" , sysUser.getEmail());
            }
            if (Lang.isNotEmpty(sysUser.getPhonenumber())){
                cnd.and("phonenumber" , "=" , sysUser.getPhonenumber());
            }
            if (Lang.isNotEmpty(sysUser.getSex())){
                cnd.and("sex" , "=" , sysUser.getSex());
            }
            if (Lang.isNotEmpty(sysUser.getAvatar())){
                cnd.and("avatar" , "=" , sysUser.getAvatar());
            }
            if (Lang.isNotEmpty(sysUser.getPassword())){
                cnd.and("password" , "=" , sysUser.getPassword());
            }
            if (Lang.isNotEmpty(sysUser.getStatus())){
                cnd.and("status" , "=" , sysUser.getStatus());
            }
            if (Lang.isNotEmpty(sysUser.getLoginIp())){
                cnd.and("login_ip" , "=" , sysUser.getLoginIp());
            }
            if (Lang.isNotEmpty(sysUser.getLoginDate())){
                cnd.and("login_date" , "=" , sysUser.getLoginDate());
            }
            if (Lang.isNotEmpty(sysUser.getSalt())){
                cnd.and("salt" , "=" , sysUser.getSalt());
            }
        }
        return cnd;
    }

    @Override
    public List<SysUser> query(SysUser sysUser) {
        return this.query(queryWrapper(sysUser));
    }

    @Override
    public TableData<SysUser> query(SysUser sysUser, int pageNumber, int pageSize) {
        return this.queryTable(queryWrapper(sysUser), pageNumber, pageSize);
    }

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
//    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user) {
        return this.query();
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
//    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectAllocatedList(SysUser user) {
        String sqlstr = "select distinct u.user_id, u.dept_id, u.user_name, u.nick_name, u.email, u.phonenumber, u.status, u.create_time " +
                " from sys_user u " +
                " left join sys_dept d on u.dept_id = d.dept_id " +
                " left join sys_user_role ur on u.user_id = ur.user_id " +
                " left join sys_role r on r.role_id = ur.role_id " +
                " where u.del_flag = '0' and r.role_id = @roleId ";
        if (Strings.isNotBlank(user.getUserName())) {
            sqlstr += "AND u.user_name like concat('%', @userName, '%')";
        }
        if (Strings.isNotBlank(user.getPhonenumber())) {
            sqlstr += "AND u.phonenumber like concat('%', @phonenumber, '%')";
        }
        if (Lang.isNotEmpty(user.getParams().get("dataScope"))) {
            sqlstr += user.getParams().get("dataScope").toString();
        }
        Sql sql = Sqls.create(sqlstr);
        sql.params().set("roleId" , user.getRoleId());
        sql.params().set("userName" , user.getUserName());
        sql.params().set("phonenumber" , user.getPhonenumber());
        sql.setCallback(Sqls.callback.entities());
        Entity<SysUser> entity = dao().getEntity(SysUser.class);
        sql.setEntity(entity);
        dao().execute(sql);
        return sql.getList(SysUser.class);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
//    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUnallocatedList(SysUser user) {
        String sqlstr = " select distinct u.user_id, u.dept_id, u.user_name, u.nick_name, u.email, u.phonenumber, u.status, u.create_time " +
                " from sys_user u " +
                " left join sys_dept d on u.dept_id = d.dept_id " +
                " left join sys_user_role ur on u.user_id = ur.user_id " +
                " left join sys_role r on r.role_id = ur.role_id " +
                " where u.del_flag = '0' and (r.role_id != #{roleId} or r.role_id IS NULL) " +
                " and u.user_id not in (select u.user_id from sys_user u inner join sys_user_role ur on u.user_id = ur.user_id and ur.role_id = @roleId ) ";
        if (Strings.isNotBlank(user.getUserName())) {
            sqlstr += "AND u.user_name like concat('%', @userName, '%')";
        }
        if (Strings.isNotBlank(user.getPhonenumber())) {
            sqlstr += "AND u.phonenumber like concat('%', @phonenumber, '%')";
        }
        if (Lang.isNotEmpty(user.getParams().get("dataScope"))) {
            sqlstr += user.getParams().get("dataScope").toString();
        }
        Sql sql = Sqls.create(sqlstr);
        sql.params().set("roleId" , user.getRoleId());
        sql.params().set("userName" , user.getUserName());
        sql.params().set("phonenumber" , user.getPhonenumber());
        sql.setCallback(Sqls.callback.entities());
        Entity<SysUser> entity = dao().getEntity(SysUser.class);
        sql.setEntity(entity);
        dao().execute(sql);
        return sql.getList(SysUser.class);
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
        return this.fetch(userName);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId) {
        SysUser user =  fetch(userId);
        fetchLinks(user,"dept|roles");
        return user;
    }

    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        String sqlstr = "select distinct r.role_id, r.role_name, r.role_key, r.role_sort, r.data_scope," +
                " r.menu_check_strictly, r.dept_check_strictly, r.status, r.del_flag, r.create_time, r.remark  " +
                "  from sys_role r " +
                " left join sys_user_role ur on ur.role_id = r.role_id " +
                " left join sys_user u on u.user_id = ur.user_id " +
                " left join sys_dept d on u.dept_id = d.dept_id " +
                " WHERE r.del_flag = '0' and u.user_name = @userName ";
        Sql sql = Sqls.create(sqlstr);
        sql.params().set("userName" , userName);
        sql.setCallback(Sqls.callback.entities());
        Entity<SysRole> entity = dao().getEntity(SysRole.class);
        sql.setEntity(entity);
        dao().execute(sql);
        List<SysRole> list = sql.getList(SysRole.class);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName) {
        String sqlstr = " select p.post_id, p.post_name, p.post_code " +
                " from sys_post p " +
                " left join sys_user_post up on up.post_id = p.post_id " +
                " left join sys_user u on u.user_id = up.user_id " +
                " where u.user_name = @userName ";
        Sql sql = Sqls.create(sqlstr);
        sql.params().set("userName" , userName);
        sql.setCallback(Sqls.callback.entities());
        Entity<SysPost> entity = dao().getEntity(SysPost.class);
        sql.setEntity(entity);
        dao().execute(sql);
        
        List<SysPost> list = sql.getList(SysPost.class);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysPost::getPostName).collect(Collectors.joining(","));
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    @Override
    public String checkUserNameUnique(String userName) {
        int count = this.count(Cnd.where("user_name" , "=" , userName));
        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkPhoneUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = this.fetch(Cnd.where("phonenumber" , "=" , user.getPhonenumber()));
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkEmailUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = this.fetch(Cnd.where("email" , "=" , user.getEmail()));
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 校验用户是否有数据权限
     *
     * @param userId 用户id
     */
    @Override
    public void checkUserDataScope(Long userId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            SysUser user = new SysUser();
            user.setUserId(userId);
            List<SysUser> users = SpringUtils.getAopProxy(this).selectUserList(user);
            if (StringUtils.isEmpty(users)) {
                throw new ServiceException("没有权限访问用户数据！");
            }
        }
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(SysUser user) {
        // 新增用户信息
        int rows = insertUser(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }

    /**
     * 注册用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean registerUser(SysUser user) {
        return insertUser(user) > 0;
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUser(SysUser user) {
        Long userId = user.getUserId();
        // 删除用户与角色关联
        userRoleService.delete(Cnd.where("user_id","=",userId));
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostService.delete(Cnd.where("user_id","=",userId));

        // 新增用户与岗位管理
        insertUserPost(user);
        return this.update(user);
    }

    /**
     * 用户授权角色
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    @Override
    @Transactional
    public void insertUserAuth(Long userId, Long[] roleIds) {
        userRoleService.delete(Cnd.where("user_id","=",userId));
        insertUserRole(userId, roleIds);
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(SysUser user) {
        return updateUser(user);
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(SysUser user) {
        return updateUser(user);
    }

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
        return this.update(Chain.make("avatar" , avatar), Cnd.where("user_name" , "=" , userName)) > 0;
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(SysUser user) {
        return updateUser(user);
    }

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(String userName, String password) {
        return this.update(Chain.make("password" , password), Cnd.where("user_name" , "=" , userName));
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        Long[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roles) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                for(SysUserRole role:list){
                    userRoleService.insert(role);
                }
            }
        }
    }

    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user) {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotNull(posts)) {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>();
            for (Long postId : posts) {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0) {
                for(SysUserPost post:list){
                    userPostService.insert(post);
                }
            }
        }
    }

    /**
     * 新增用户角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(Long userId, Long[] roleIds) {
        if (StringUtils.isNotNull(roleIds)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roleIds) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                for(SysUserRole role:list){
                    userRoleService.insert(role);
                }
            }
        }
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserById(Long userId) {
        // 删除用户与角色关联
        userRoleService.delete(Cnd.where("user_id","=",userId));
        // 删除用户与岗位表
        userPostService.delete(Cnd.where("user_id","=",userId));
        return this.delete(userId);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserByIds(Long[] userIds) {
        try{
            for (Long userId : userIds) {
                checkUserAllowed(new SysUser(userId));
                checkUserDataScope(userId);
            }
            // 删除用户与角色关联
            userRoleService.delete(Cnd.where("user_id","in",userIds));
            // 删除用户与岗位关联
            userPostService.delete(Cnd.where("user_id","in",userIds));
            this.delete(userIds);
        }catch (Exception e){
            return 0;
        }
        return 1;
    }

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (SysUser user : userList) {
            try {
                // 验证是否存在这个用户
                SysUser u = this.fetch(user.getUserName());
                if (StringUtils.isNull(u)) {
                    BeanValidators.validateWithException(validator, user);
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, user);
                    user.setUpdateBy(operName);
                    this.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
