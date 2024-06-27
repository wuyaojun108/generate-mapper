package org.wyj.config;

import org.wyj.converts.ITypeConvertor;
import org.wyj.converts.MySQLTypeConvertor;
import org.wyj.query.DbTypeEnum;
import org.wyj.query.IDbQuery;
import org.wyj.query.MySQLDbQuery;

import java.util.HashMap;
import java.util.Map;

public class DbRegistry {
    private static final Map<DbTypeEnum, IDbQuery> dbType2dbQueryMap = new HashMap<>();
    private static final Map<DbTypeEnum, ITypeConvertor> dbType2typeConvertorMap = new HashMap<>();

    static {
        dbType2dbQueryMap.put(DbTypeEnum.MYSQL, new MySQLDbQuery());
        dbType2typeConvertorMap.put(DbTypeEnum.MYSQL, new MySQLTypeConvertor());
    }

    public static IDbQuery getDbQueryByDbType(DbTypeEnum dbType) {
        return dbType2dbQueryMap.get(dbType);
    }

    public static ITypeConvertor getTypeConvertorByDbType(DbTypeEnum dbType) {
        return dbType2typeConvertorMap.get(dbType);
    }
}
