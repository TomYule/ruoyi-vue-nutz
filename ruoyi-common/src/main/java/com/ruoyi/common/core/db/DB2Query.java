package com.ruoyi.common.core.db;

import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Strings;

/**
 * @author Haiming
 * @date 2020/8/4 2:26 PM
 */
public class DB2Query extends AbstractDbQuery {
    @Override
    public Sql tableList(String tableName, String tableComment, String orderByColumn, String isAsc) {
        String sqlstr = "SELECT TABNAME AS TABLE_NAME, CREATE_TIME AS CREATE_TIME, ALTER_TIME AS UPDATE_TIME  FROM SYSCAT.TABLES " +
                " WHERE OWNER <> 'SYSIBM' " +
                " AND TABNAME NOT IN (SELECT TABLE_NAME FROM GEN_TABLE) ";
        if (Strings.isNotBlank(tableName)) {
            tableName = tableName.toUpperCase();
            sqlstr += "and TABNAME like @TABNAME";
        }
        if (Strings.isNotBlank(tableComment)) {
            sqlstr += "and REMARKS like @REMARKS";
        }
        if(Strings.isNotBlank(orderByColumn)) {
            if(Strings.isBlank(isAsc)){
                isAsc = "asc";
            }
            sqlstr += " order by " + orderByColumn + " " + isAsc;
        }
        Sql sql = Sqls.create(sqlstr);
        sql.params().set("TABNAME", "%" + tableName + "%");
        sql.params().set("REMARKS", "%" + tableComment + "%");
        sql.setCallback(Sqls.callback.entities());
        return sql;
    }

    @Override
    public Sql tableList(String[] tableName) {
        String sqlstr = "select TABNAME as table_name, REMARKS as table_comment FROM SYSCAT.TABLES " +
                "WHERE TABNAME in (@tableName)";
        Sql sql = Sqls.create(sqlstr);
        sql.params().set("tableName", tableName);
        sql.setCallback(Sqls.callback.entities());
        return sql;
    }

    @Override
    public Sql tableColumnsByName(String tableName) {
        String sqlstr = "SELECT COLNAME                                       AS COLUMN_NAME, "  +
                "       TYPENAME                                      AS COLUMN_TYPE, "  +
                "       (CASE WHEN KEYSEQ = '1' THEN '1' ELSE '0' END) AS IS_PK, "  +
                "       (CASE WHEN NULLS = 'N' THEN '1' ELSE '0' END) AS IS_REQUIRED, "  +
                "       COLNO                                         AS SORT, "  +
                "       REMARKS                                       AS COLUMN_COMMENT "  +
                "FROM SYSCAT.COLUMNS " +
                "WHERE TABNAME = @tableName ORDER BY COLNO";
        Sql sql = Sqls.create(sqlstr);
        sql.params().set("tableName", tableName);
        sql.setCallback(Sqls.callback.entities());
        return sql;
    }

    @Override
    public Sql selectMenuTreeByAdmin() {
        String sqlstr = " select distinct m.menu_id, m.parent_id, m.menu_name, m.path, m.component, m.query, m.visible," +
                " m.status, IFNULL(m.perms,'') as perms, m.is_frame, m.is_cache, m.menu_type, m.icon, m.order_num, m.create_time " +
                " from sys_menu m where m.menu_type in ('M', 'C') and m.status = 0 " +
                " order by m.parent_id, m.order_num ";
        Sql sql = Sqls.create(sqlstr);
        sql.setCallback(Sqls.callback.entities());
        return sql;
    }

    @Override
    public Sql selectMenuTreeByUserId(Long userId) {
        String sqlstr =   " select distinct m.menu_id, m.parent_id, m.menu_name, m.path, m.component, m.query," +
                " m.visible, m.status, ifnull(m.perms,'') as perms, m.is_frame, m.is_cache, m.menu_type, m.icon, m.order_num, m.create_time "  +
                " from sys_menu m "  +
                "  left join sys_role_menu rm on m.menu_id = rm.menu_id "  +
                "  left join sys_user_role ur on rm.role_id = ur.role_id "  +
                "  left join sys_role ro on ur.role_id = ro.role_id "  +
                "  left join sys_user u on ur.user_id = u.user_id "  +
                " where u.user_id = @userId and m.menu_type in ('M', 'C') and m.status = 0  AND ro.status = 0 "  +
                " order by m.parent_id, m.order_num";
        Sql sql = Sqls.create(sqlstr);
        sql.params().set("userId" , userId);
        sql.setCallback(Sqls.callback.entities());
        return sql;
    }
}
