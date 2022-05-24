package com.ruoyi.generator.util;

import com.ruoyi.generator.db.DbQueryRegistry;
import com.ruoyi.generator.db.IDbQuery;
import com.ruoyi.generator.db.converts.ITypeConvert;
import com.ruoyi.generator.db.converts.TypeConvertRegistry;
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
     * 类型转换
     */
    private static ITypeConvert typeConvert;


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

    /**
     * 数据库类型抽象类 封装 实现
     * @return
     */
    public static synchronized  ITypeConvert getTypeConvert(DB dbType) {
        if (null == typeConvert) {
            TypeConvertRegistry typeConvertRegistry = new TypeConvertRegistry();
            // 默认 MYSQL
            typeConvert = Optional.ofNullable(typeConvertRegistry.getTypeConvert(dbType))
                    .orElseGet(() -> typeConvertRegistry.getTypeConvert(DB.MYSQL));
        }
        return typeConvert;
    }
}
