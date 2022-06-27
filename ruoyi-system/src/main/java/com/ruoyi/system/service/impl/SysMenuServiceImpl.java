package com.ruoyi.system.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.service.BaseServiceImpl;
import com.ruoyi.common.utils.QueryUtils;
import com.ruoyi.system.service.ISysRoleMenuService;
import com.ruoyi.system.service.ISysRoleService;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.vo.MetaVo;
import com.ruoyi.system.domain.vo.RouterVo;
import com.ruoyi.system.service.ISysMenuService;

/**
 * 菜单 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenu> implements ISysMenuService {

    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysRoleMenuService sysRoleMenuService;

    public Cnd queryWrapper(SysMenu sysMenu) {
        Cnd cnd = Cnd.NEW();
        if (Lang.isNotEmpty(sysMenu)) {
            if (Lang.isNotEmpty(sysMenu.getMenuName())) {
                cnd.and("menu_name" , "like" , "%" + sysMenu.getMenuName() + "%");
            }
            if (Lang.isNotEmpty(sysMenu.getParentId())) {
                cnd.and("parent_id" , "=" , sysMenu.getParentId());
            }
            if (Lang.isNotEmpty(sysMenu.getOrderNum())) {
                cnd.and("order_num" , "=" , sysMenu.getOrderNum());
            }
            if (Lang.isNotEmpty(sysMenu.getPath())) {
                cnd.and("path" , "=" , sysMenu.getPath());
            }
            if (Lang.isNotEmpty(sysMenu.getComponent())) {
                cnd.and("component" , "=" , sysMenu.getComponent());
            }
            if (Lang.isNotEmpty(sysMenu.getQuery())) {
                cnd.and("query" , "=" , sysMenu.getQuery());
            }
            if (Lang.isNotEmpty(sysMenu.getIsFrame())) {
                cnd.and("is_frame" , "=" , sysMenu.getIsFrame());
            }
            if (Lang.isNotEmpty(sysMenu.getIsCache())) {
                cnd.and("is_cache" , "=" , sysMenu.getIsCache());
            }
            if (Lang.isNotEmpty(sysMenu.getMenuType())) {
                cnd.and("menu_type" , "=" , sysMenu.getMenuType());
            }
            if (Lang.isNotEmpty(sysMenu.getVisible())) {
                cnd.and("visible" , "=" , sysMenu.getVisible());
            }
            if (Lang.isNotEmpty(sysMenu.getStatus())) {
                cnd.and("status" , "=" , sysMenu.getStatus());
            }
            if (Lang.isNotEmpty(sysMenu.getPerms())) {
                cnd.and("perms" , "=" , sysMenu.getPerms());
            }
            if (Lang.isNotEmpty(sysMenu.getIcon())) {
                cnd.and("icon" , "=" , sysMenu.getIcon());
            }
        }
        return cnd;
    }

    @Override
    public List<SysMenu> query(SysMenu sysMenu) {
        return this.query(queryWrapper(sysMenu));
    }

    @Override
    public List<SysMenu> query(SysMenu sysMenu, int pageNumber, int pageSize) {
        return this.query(queryWrapper(sysMenu), pageNumber, pageSize);
    }

    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuList(Long userId) {
        return selectMenuList(new SysMenu(), userId);
    }

    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuList(SysMenu menu, Long userId) {
        List<SysMenu> menuList = null;
        // 管理员显示所有菜单信息
        if (SysUser.isAdmin(userId)) {
            menuList = this.query();
        } else {
            String sqlstr = " select distinct m.menu_id, m.parent_id, m.menu_name, m.path, m.component, m.`query`, " +
                    " m.visible, m.status, ifnull(m.perms,'') as perms, m.is_frame, m.is_cache, m.menu_type, m.icon, " +
                    " m.order_num, m.create_time " +
                    " from sys_menu m " +
                    " left join sys_role_menu rm on m.menu_id = rm.menu_id " +
                    " left join sys_user_role ur on rm.role_id = ur.role_id " +
                    " left join sys_role ro on ur.role_id = ro.role_id " +
                    " where ur.user_id = @userId";
            if (Strings.isNotBlank(menu.getMenuName())) {
                sqlstr += " AND m.menu_name like concat('%', @menuName, '%')";
            }
            if (Strings.isNotBlank(menu.getVisible())) {
                sqlstr += " AND m.visible = @visible";
            }
            if (Strings.isNotBlank(menu.getStatus())) {
                sqlstr += " AND m.status = @status";
            }
            sqlstr += " order by m.parent_id, m.order_num";
            Sql sql = Sqls.create(sqlstr);
            sql.params().set("userId" , userId);
            sql.params().set("menuName" , menu.getMenuName());
            sql.params().set("visible" , menu.getVisible());
            sql.params().set("status" , menu.getStatus());
            sql.setCallback(Sqls.callback.entities());
            Entity<SysMenu> entity = dao().getEntity(SysMenu.class);
            sql.setEntity(entity);
            dao().execute(sql);
            menuList = sql.getList(SysMenu.class);
        }
        return menuList;
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        String sqlstr =  " select distinct m.perms "  +
                " from sys_menu m "  +
                "  left join sys_role_menu rm on m.menu_id = rm.menu_id "  +
                "  left join sys_user_role ur on rm.role_id = ur.role_id "  +
                "  left join sys_role r on r.role_id = ur.role_id "  +
                " where m.status = '0' and r.status = '0' and ur.user_id = @userId";
        Sql sql = Sqls.create(sqlstr);
        sql.params().set("userId" , userId);
        sql.setCallback(Sqls.callback.entities());
        Entity<String> entity = dao().getEntity(String.class);
        sql.setEntity(entity);
        dao().execute(sql);
        List<String> perms = sql.getList(String.class);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户名称
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuTreeByUserId(Long userId) {
        List<SysMenu> menus = null;
        if (SecurityUtils.isAdmin(userId)) {
            Sql sql = QueryUtils.getDbQuery(this.dao().meta().getType()).selectMenuTreeByAdmin();
            sql.setCallback(Sqls.callback.entities());
            Entity<SysMenu> entity = dao().getEntity(SysMenu.class);
            sql.setEntity(entity);
            dao().execute(sql);
            menus = sql.getList(SysMenu.class);
        } else {
            Sql sql = QueryUtils.getDbQuery(this.dao().meta().getType()).selectMenuTreeByUserId(userId);
            sql.setCallback(Sqls.callback.entities());
            Entity<SysMenu> entity = dao().getEntity(SysMenu.class);
            sql.setEntity(entity);
            dao().execute(sql);
            menus = sql.getList(SysMenu.class);
        }
        return getChildPerms(menus, 0);
    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        SysRole role = roleService.fetch(roleId);
        String sqlstr =  " select m.menu_id "  +
                " from sys_menu m "  +
                " left join sys_role_menu rm on m.menu_id = rm.menu_id "  +
                " where rm.role_id = @roleId ";
        if ( role.isMenuCheckStrictly()) {
            sqlstr += " and m.menu_id not in (select m.parent_id from sys_menu m inner join sys_role_menu rm " +
                    "on m.menu_id = rm.menu_id and rm.role_id = @roleId ) ";
        }
        sqlstr +=  " order by m.parent_id, m.order_num";
        Sql sql = Sqls.fetchLong(sqlstr);
        sql.params().set("roleId" , roleId);
        sql.setCallback(Sqls.callback.longs());
        dao().execute(sql);
        return  sql.getList(Long.class);
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1" , menu.getIsCache()), menu.getPath()));
            List<SysMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1" , menu.getIsCache()), menu.getPath()));
                children.setQuery(menu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentId().intValue() == 0 && isInnerLink(menu)) {
                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
                router.setPath("/");
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        List<Long> tempList = new ArrayList<Long>();
        for (SysMenu dept : menus) {
            tempList.add(dept.getMenuId());
        }
        for (Iterator<SysMenu> iterator = menus.iterator(); iterator.hasNext(); ) {
            SysMenu menu = (SysMenu) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus) {
        List<SysMenu> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    @Override
    public List<SysMenu> insertTree(List<SysMenu> menus,Long parentId) {
        if(Lang.isNotEmpty(menus) && menus.size() > 0){
            for(SysMenu m:menus){
                if(Lang.isNotEmpty(parentId)){
                    m.setParentId(parentId);
                }
                insert(m);
                if(Lang.isNotEmpty(m.getChildren()) && m.getChildren().size()> 0){
                    insertTree(m.getChildren(),m.getMenuId());
                }
            }
        }
        return menus;
    }

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    @Override
    public SysMenu selectMenuById(Long menuId) {
        return this.fetch(menuId);
    }

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean hasChildByMenuId(Long menuId) {
        int result = this.count(Cnd.where("parent_id","=",menuId));
        return result > 0;
    }

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean checkMenuExistRole(Long menuId) {
        int result = sysRoleMenuService.count(Cnd.where("menu_id","=",menuId));
        return result > 0;
    }

    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int insertMenu(SysMenu menu) {
        try{
            this.insert(menu);
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int updateMenu(SysMenu menu) {
        return this.update(menu);
    }

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public int deleteMenuById(Long menuId) {
        return this.delete(menuId);
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public String checkMenuNameUnique(SysMenu menu) {
        Long menuId = StringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
        SysMenu info = this.fetch(Cnd.where("menu_name","=",menu.getMenuName())
                .and("parent_id","=", menu.getParentId()));
        if (StringUtils.isNotNull(info) && info.getMenuId().longValue() != menuId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenu menu) {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenu menu) {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            component = UserConstants.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysMenu menu) {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SysMenu menu) {
        return menu.getIsFrame().equals(UserConstants.NO_FRAME) && StringUtils.ishttp(menu.getPath());
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenu menu) {
        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, int parentId) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext(); ) {
            SysMenu t = (SysMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 内链域名特殊字符替换
     *
     * @return
     */
    public String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path, new String[]{Constants.HTTP, Constants.HTTPS},
                new String[]{"" , ""});
    }
}
