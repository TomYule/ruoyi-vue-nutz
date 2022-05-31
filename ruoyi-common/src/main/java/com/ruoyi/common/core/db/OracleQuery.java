package com.ruoyi.common.core.db;

import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


/**
 * @author Haiming
 * @date 2020/8/11 8:33 PM
 */
@Component
@ConfigurationProperties(prefix = "gen")
@PropertySource(value = {"classpath:generator.yml"})
public class OracleQuery extends AbstractDbQuery {

    private String schema = "";

    @Value("${schema}")
    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Override
    public Sql tableList(String tableName, String tableComment, String orderByColumn, String isAsc) {
        String sqlstr = "SELECT TABLE_NAME , COMMENTS as table_comment FROM ALL_TAB_COMMENTS " +
                " where not regexp_like(OWNER,'SYS|SYSTEM|OUTLN|XDB|HR|APEX') " +
                " AND table_name NOT IN (select table_name from gen_table) ";
        if (Strings.isNotBlank(tableName)) {
            tableName = tableName.toUpperCase();
            sqlstr += "and TABLE_NAME like @TABNAME";
        }
        if (Strings.isNotBlank(tableComment)) {
            sqlstr += "and COMMENTS like @REMARKS";
        }
        sqlstr += " order by TABLE_NAME asc";
        Sql sql = Sqls.create(sqlstr);
        sql.params().set("TABNAME", "%" + tableName + "%");
        sql.params().set("REMARKS", "%" + tableComment + "%");
        sql.setCallback(Sqls.callback.entities());
        return sql;
    }

    @Override
    public Sql tableList(String[] tableName) {
        String sqlstr = "SELECT distinct TABLE_NAME , COMMENTS as table_comment FROM ALL_TAB_COMMENTS where TABLE_NAME in (@TABNAME) ";
        Sql sql = Sqls.create(sqlstr);
        sql.params().set("TABNAME",tableName);
        sql.setCallback(Sqls.callback.entities());
        return sql;
    }

    @Override
    public Sql tableColumnsByName(String tableName) {
        String sqlstr = " SELECT A.COLUMN_NAME, "  +
                "       CASE "  +
                "           WHEN A.DATA_TYPE = 'NUMBER' THEN "  +
                "               (CASE "  +
                "                    WHEN A.DATA_PRECISION IS NULL THEN A.DATA_TYPE "  +
                "                    WHEN NVL(A.DATA_SCALE, 0) > 0 "  +
                "                        THEN A.DATA_TYPE || '(' || A.DATA_PRECISION || ',' || A.DATA_SCALE || ')' "  +
                "                    ELSE A.DATA_TYPE || '(' || A.DATA_PRECISION || ')' END) "  +
                "           ELSE A.DATA_TYPE END       column_type, "  +
                "       B.COMMENTS as column_comment, "  +
                "       (CASE WHEN DECODE(C.POSITION, '1', 'PRI') ='PRI' then '1' else null end) is_required, "  +
                "       (CASE WHEN DECODE(C.POSITION, '1', 'PRI') ='PRI' then '1' else '0' end) is_pk, "  +
                "       A.COLUMN_ID  as sort " +
                " FROM ALL_TAB_COLUMNS A" +
                "          INNER JOIN ALL_COL_COMMENTS B "  +
                "                    ON A.TABLE_NAME = B.TABLE_NAME AND A.COLUMN_NAME = B.COLUMN_NAME AND B.OWNER = '"+ schema +"' "  +
                "         LEFT JOIN ALL_CONSTRAINTS D ON D.TABLE_NAME = A.TABLE_NAME AND D.CONSTRAINT_TYPE = 'P' AND D.OWNER = '"+ schema +"' "  +
                "         LEFT JOIN ALL_CONS_COLUMNS C "  +
                "                   ON C.CONSTRAINT_NAME = D.CONSTRAINT_NAME AND C.COLUMN_NAME = A.COLUMN_NAME AND C.OWNER = '"+ schema +"' " +
                " WHERE A.OWNER =  '"+ schema +"' AND A.TABLE_NAME=@TABNAME " +
                " ORDER BY A.COLUMN_ID ";
        Sql sql = Sqls.create(sqlstr);
        sql.params().set("TABNAME", tableName);
        sql.setCallback(Sqls.callback.entities());
        return sql;
    }

    @Override
    public Sql selectMenuTreeByAdmin() {
        String sqlstr = " select distinct m.menu_id, m.parent_id, m.menu_name, m.path, m.component, m.query, m.visible," +
                " m.status, COALESCE(m.perms,'') as perms, m.is_frame, m.is_cache, m.menu_type, m.icon, m.order_num, m.create_time " +
                " from sys_menu m where m.menu_type in ('M', 'C') and m.status = 0 " +
                " order by m.parent_id, m.order_num ";
        Sql sql = Sqls.create(sqlstr);
        sql.setCallback(Sqls.callback.entities());
        return sql;
    }

    @Override
    public Sql selectMenuTreeByUserId(Long userId) {
        String sqlstr =   " select distinct m.menu_id, m.parent_id, m.menu_name, m.path, m.component, m.query," +
                " m.visible, m.status, COALESCE(m.perms,'') as perms, m.is_frame, m.is_cache, m.menu_type, m.icon, m.order_num, m.create_time "  +
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
