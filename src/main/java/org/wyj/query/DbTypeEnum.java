package org.wyj.query;

public enum DbTypeEnum {
    MYSQL;

    public static DbTypeEnum getDbTypeByName(String dbName) {
        for (DbTypeEnum dbType : DbTypeEnum.values()) {
            if (dbType.name().equalsIgnoreCase(dbName)) {
                return dbType;
            }
        }
        return null;
    }
}
