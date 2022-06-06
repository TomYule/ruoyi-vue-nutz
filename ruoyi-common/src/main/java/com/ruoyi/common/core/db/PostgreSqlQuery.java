package com.ruoyi.common.core.db;

import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Strings;

/**
 * @author Haiming
 * @date 2020/8/5 11:25 AM
 */
public class PostgreSqlQuery extends AbstractDbQuery {
    @Override
    public Sql tableList(String tableName, String tableComment, String orderByColumn, String isAsc) {
        String sqlstr = "SELECT A.tablename as table_name, obj_description(relfilenode, 'pg_class') AS table_comment " +
                "FROM pg_tables A, pg_class B " +
                "WHERE A.schemaname='public' and A.tablename = B.relname";
        if (Strings.isNotBlank(tableName)) {
            tableName = tableName.toUpperCase();
            sqlstr += "and A.tablename = like @TABNAME";
        }
//        if (Strings.isNotBlank(tableComment)) {
//            sqlstr += "and REMARKS like @REMARKS";
//        }

        sqlstr += " order by A.tablename asc";
        Sql sql = Sqls.create(sqlstr);
        sql.params().set("TABNAME", "%" + tableName + "%");
        sql.params().set("REMARKS", "%" + tableComment + "%");
        sql.setCallback(Sqls.callback.entities());
        return sql;
    }

    @Override
    public Sql tableList(String[] tableName) {
        String sqlstr = "SELECT A.tablename as table_name, obj_description(relfilenode, 'pg_class') AS table_comment " +
                "FROM pg_tables A, pg_class B " +
                "WHERE A.schemaname='public' and A.tablename = B.relname";
        sqlstr += " and A.tablename in (@TABNAME)";
        Sql sql = Sqls.create(sqlstr);
        sql.params().set("TABNAME", tableName);
        sql.setCallback(Sqls.callback.entities());
        return sql;
    }

    @Override
    public Sql tableColumnsByName(String tableName) {
        String sqlstr = "SELECT A.attname AS column_name ,format_type (A.atttypid,A.atttypmod) AS data_type,col_description (A.attrelid,A.attnum) AS column_comment," +
                "(CASE WHEN (SELECT COUNT (*) FROM pg_constraint AS PC WHERE A.attnum = PC.conkey[1] AND PC.contype = 'p') > 0 THEN 'PRI' ELSE '' END) AS key " +
                "FROM pg_class AS C,pg_attribute AS A " +
                "WHERE A.attrelid = @tableName ::regclass and A.attrelid= C.oid AND A.attnum> 0 AND NOT A.attisdropped ORDER  BY A.attnum";
        Sql sql = Sqls.create(sqlstr);
        sql.params().set("tableName", tableName);
        sql.setCallback(Sqls.callback.entities());
        return sql;
    }

    @Override
    public Sql selectMenuTreeByAdmin() {
        String sqlstr = " SELECT DISTINCT M.MENU_ID, M.PARENT_ID, M.MENU_NAME, M.PATH, M.COMPONENT, M.QUERY, M.VISIBLE," +
                " M.STATUS, COALESCE(M.PERMS,'') AS PERMS, M.IS_FRAME, M.IS_CACHE, M.MENU_TYPE, M.ICON, M.ORDER_NUM, M.CREATE_TIME " +
                " FROM SYS_MENU M WHERE M.MENU_TYPE IN ('M', 'C') AND M.STATUS = '0' " +
                " ORDER BY M.PARENT_ID, M.ORDER_NUM ";
        Sql sql = Sqls.create(sqlstr);
        sql.setCallback(Sqls.callback.entities());
        return sql;
    }

    @Override
    public Sql selectMenuTreeByUserId(Long userId) {
        String sqlstr = " SELECT DISTINCT M.MENU_ID, M.PARENT_ID, M.MENU_NAME, M.PATH, M.COMPONENT, M.QUERY," +
                " M.VISIBLE, M.STATUS, IFNULL(M.PERMS,'') AS PERMS, M.IS_FRAME, M.IS_CACHE, M.MENU_TYPE, M.ICON, M.ORDER_NUM, M.CREATE_TIME "  +
                " FROM SYS_MENU M "  +
                "  LEFT JOIN SYS_ROLE_MENU RM ON M.MENU_ID = RM.MENU_ID "  +
                "  LEFT JOIN SYS_USER_ROLE UR ON RM.ROLE_ID = UR.ROLE_ID "  +
                "  LEFT JOIN SYS_ROLE RO ON UR.ROLE_ID = RO.ROLE_ID "  +
                "  LEFT JOIN SYS_USER U ON UR.USER_ID = U.USER_ID "  +
                " WHERE U.USER_ID = @userId AND M.MENU_TYPE IN ('M', 'C') AND M.STATUS = '0'  AND RO.STATUS = '0' "  +
                " ORDER BY M.PARENT_ID, M.ORDER_NUM";
        Sql sql = Sqls.create(sqlstr);
        sql.params().set("userId" , userId);
        sql.setCallback(Sqls.callback.entities());
        return sql;
    }
}
