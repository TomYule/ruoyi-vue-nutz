package com.ruoyi.generator.db;
import org.nutz.dao.sql.Sql;

/**
 * IDbQuery
 * @author kricss
 */
public interface IDbQuery {
    /**
     *  查询数据库表信息
     * @param tableName 表名称
     * @param tableComment 表注释
     * @param orderByColumn 排序字段
     * @param isAsc 排序方式
     * @return
     */
    public Sql tableList(String tableName, String tableComment, String orderByColumn, String isAsc);

    /**
     * 根据表面查多个
     * @param tableNames
     * @return
     */
    public Sql tableList(String[] tableNames);

    /**
     * 表字段查询
     * @param tableName 表名称
     * @return
     */
    public Sql tableColumnsByName(String tableName);

}
