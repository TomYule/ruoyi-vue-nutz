package com.ruoyi.common.core.db;

import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

/**
 * @author Haiming
 * @date 2020/8/4 10:34 AM
 */
public class MySqlQuery extends AbstractDbQuery {

    @Override
    public Sql tableList(String tableName, String tableComment, String orderByColumn, String isAsc) {
        String sqlstr = " select table_name, table_comment, create_time, update_time from information_schema.tables " +
                " where table_schema = (select database()) " +
                " AND table_name NOT LIKE 'qrtz_%' " +
                " AND table_name NOT IN (select table_name from gen_table) ";
        if (Strings.isNotBlank(tableName)) {
            sqlstr += " AND lower(table_name) like lower(concat('%', @tableName, '%'))";
        }
        if (Strings.isNotBlank(tableComment)) {
            sqlstr += " AND lower(table_comment) like lower(concat('%', @tableComment, '%'))";
        }
        if (Lang.isNotEmpty(orderByColumn)) {
            if (Strings.isBlank(isAsc)) {
                isAsc = "asc";
            }
            sqlstr += " order by " + orderByColumn + " " + isAsc;
        }
        Sql sql = Sqls.create(sqlstr);
        sql.params().set("tableName", tableName);
        sql.params().set("tableComment", tableComment);
        sql.setCallback(Sqls.callback.entities());
        return sql;
    }

    @Override
    public Sql tableList(String[] tableNames) {
        String sqlstr =" select table_name, table_comment, create_time, update_time from information_schema.tables "  +
                " where table_name NOT LIKE 'qrtz_%'  and table_schema = (select database()) " +
//                " and table_name NOT LIKE 'gen_%' "  +
                " and table_name in (@tableNames)";
        Sql sql = Sqls.create(sqlstr);
        sql.params().set("tableNames" , tableNames);
        sql.setCallback(Sqls.callback.entities());
        return sql;
    }

    @Override
    public Sql tableColumnsByName(String tableName) {
        String sqlstr = "select column_name,  "  +
                "       (case when (is_nullable = 'no' && column_key != 'PRI') then '1' else null end) as is_required,  "  +
                "       (case when column_key = 'PRI' then '1' else '0' end)                           as is_pk,  "  +
                "       ordinal_position                                                               as sort,  "  +
                "       column_comment,  "  +
                "       (case when extra = 'auto_increment' then '1' else '0' end)                     as is_increment,  "  +
                "       column_type  "  +
                " from information_schema.columns  "  +
                " where table_schema = (select database())  "  +
                "  and table_name = (@tableNames)  "  +
                " order by ordinal_position ";
        Sql sql = Sqls.create(sqlstr);
        sql.params().set("tableNames", tableName);
        sql.setCallback(Sqls.callback.entities());
        return sql;
    }

    @Override
    public Sql selectMenuTreeByAdmin() {
        String sqlstr = " select distinct m.menu_id, m.parent_id, m.menu_name, m.path, m.component, m.query, m.visible," +
                " m.status, ifnull(m.perms,'') as perms, m.is_frame, m.is_cache, m.menu_type, m.icon, m.order_num, m.create_time " +
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
