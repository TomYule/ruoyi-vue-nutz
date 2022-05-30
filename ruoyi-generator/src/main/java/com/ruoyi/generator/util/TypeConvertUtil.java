package com.ruoyi.generator.util;

import com.ruoyi.generator.db.converts.ITypeConvert;
import com.ruoyi.generator.db.converts.TypeConvertRegistry;
import org.nutz.dao.DB;

import java.util.Optional;

/**
 * @author haiming yu
 * @date Created in 2022/5/30 16:17
 */
public class TypeConvertUtil {

    /**
     * 类型转换
     */
    private static ITypeConvert typeConvert;


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
