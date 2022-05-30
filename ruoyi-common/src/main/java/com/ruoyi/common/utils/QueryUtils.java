package com.ruoyi.common.utils;

import com.ruoyi.common.core.db.DbQueryRegistry;
import com.ruoyi.common.core.db.IDbQuery;
import org.nutz.dao.DB;

import java.util.Optional;

/**
 * 查询SQL生成工具类
 * @author haiming yu
 * @date Created in 2022/5/24 18:02
 */
public class QueryUtils {
    /**
     * 数据库信息查询
     */
    private static IDbQuery dbQuery;



    /**
     * sql 查询抽象类 接口实现
     * @return
     */
    public static synchronized IDbQuery getDbQuery(DB dbType) {
        if (null == dbQuery) {
            DbQueryRegistry dbQueryRegistry = new DbQueryRegistry();
            // 默认 MYSQL
            dbQuery = Optional.ofNullable(dbQueryRegistry.getDbQuery(dbType))
                    .orElseGet(() -> dbQueryRegistry.getDbQuery(DB.MYSQL));
        }
        return dbQuery;
    }

}
