package com.ruoyi.common.core.service;

import com.ruoyi.common.core.page.TableData;
import com.ruoyi.common.core.page.TableDataInfo;
import org.nutz.dao.*;
import org.nutz.dao.pager.Pager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Author: Haimming
 * @Date: 2019-10-17 14:51
 * @Version 1.0
 */
public interface BaseService<T> {
    Dao dao();

    public int count(Condition cnd);

    public int count();

    public int count(String tableName, Condition cnd);

    public int count(String tableName);

    public T fetch(long id);

    public T fetch(String name);

    public T fetch(Condition cnd);

    public T fetchLinks(T t, String name);

    public List<T> fetchLinks(List<T> t, String name);

    public T fetchLinks(T t, String name, Condition cnd);

    public T insert(T t);

    public void insert(String tableName, Chain chain);

    public T fastInsert(T t);

    /**
     * 查询获取部分字段
     *
     * @param fieldName 支持通配符 ^(a|b)$
     * @param cnd
     * @return
     */
    List<T> query(String fieldName, Condition cnd);

    /**
     * 查询一组对象。你可以为这次查询设定条件
     *
     * @param cnd WHERE 条件。如果为 null，将获取全部数据，顺序为数据库原生顺序<br>
     *            只有在调用这个函数的时候， cnd.limit 才会生效
     * @return 对象列表
     */
    List<T> query(Condition cnd);


    /**
     * 获取全部数据
     *
     * @return
     */
    List<T> query();

    /**
     * @param cnd      查询条件
     * @param linkName 关联字段，支持正则 ^(a|b)$
     * @return
     */
    List<T> query(Condition cnd, String linkName);

    /**
     * 获取表及关联表全部数据(支持子查询)
     *
     * @param cnd      查询条件
     * @param linkName 关联字段，支持正则 ^(a|b)$
     * @param linkCnd  关联条件
     * @return
     */
    List<T> query(Condition cnd, String linkName, Condition linkCnd);

    /**
     * 获取表及关联表全部数据
     *
     * @param linkName 关联字段，支持正则 ^(a|b)$
     * @return
     */
    List<T> query(String linkName);

    /**
     * 分页关联字段查询
     *
     * @param cnd      查询条件
     * @param linkName 关联字段，支持正则 ^(a|b)$
     * @param pager    分页对象
     * @return
     */
    List<T> query(Condition cnd, String linkName, Pager pager);

    /**
     * 分页关联字段查询(支持关联条件)
     *
     * @param cnd      查询条件
     * @param linkName 关联字段，支持正则 ^(a|b)$
     * @param linkCnd  关联条件
     * @param pager    分页对象
     * @return
     */
    List<T> query(Condition cnd, String linkName, Condition linkCnd, Pager pager);

    /**
     * 分页查询
     *
     * @param cnd   查询条件
     * @param pager 分页对象
     * @return
     */
    List<T> query(Condition cnd, Pager pager);

    TableData<T> queryTable(Condition cnd, Pager pager);

    /**
     * 分页查询
     * @param cnd
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public List<T> query(Condition cnd, int pageNumber, int pageSize);

    TableData<T> queryTable(Condition cnd, int pageNumber, int pageSize);
    /**
     * 更新
     * @param obj
     * @return
     */
    public int update(Object obj);

    /**
     * 忽略值为null的字段
     *
     * @param obj
     * @return
     */
    public int updateIgnoreNull(Object obj);

    /**
     * 部分更新实体表
     *
     * @param chain
     * @param cnd
     * @return
     */
    public int update(Chain chain, Condition cnd);
    /**
     * 部分更新表
     *
     * @param tableName
     * @param chain
     * @param cnd
     * @return
     */
    public int update(String tableName, Chain chain, Condition cnd);

    public int delete(long id);

    public int delete(int id);

    public int delete(String name);

    public int delete( Condition cnd);

    /**
     * 批量删除
     *
     * @param ids
     */
    public int delete(Integer[] ids);


    /**
     * 批量删除
     *
     * @param ids
     */
    public int delete(Long[] ids);

    public int delete(List<Long> ids);
    /**
     * 批量删除
     *
     * @param ids
     */
    public int delete(String[] ids);


    /**
     * 伪删除
     *
     * @param id
     * @return
     */
    public int vDelete(String id);

    /**
     * 批量伪删除
     *
     * @param ids
     * @return
     */
    public int vDelete(String[] ids);

    /**
     * 分页查询
     * @param pageable
     * @return
     */
    Page<T> query(Pageable pageable);

    /**
     * 条件分页查询
     * @param cnd
     * @param pageable
     * @return
     */
    Page<T> query(Cnd cnd, Pageable pageable);

    /**
     * 条件分页 关联 查询
     * @param cnd
     * @param linkname
     * @param pageable
     * @return
     */
    Page<T> query(Cnd cnd, String linkname, Pageable pageable);

}