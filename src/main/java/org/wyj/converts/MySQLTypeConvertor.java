package org.wyj.converts;

public class MySQLTypeConvertor implements ITypeConvertor {

    @Override
    public JavaTypeEnum convertType(String dbType) {
        String t = dbType.toLowerCase();

        JavaTypeEnum javaType;
        if (t.contains("char")) {
            javaType = JavaTypeEnum.STRING;
        } else if (t.contains("bigint")) {
            javaType = JavaTypeEnum.LONG;
        } else if (t.contains("tinyint(1)")) {
            javaType = JavaTypeEnum.BOOLEAN;
        } else if (t.contains("tinyint")) {
            javaType = JavaTypeEnum.BYTE;
        } else if (t.contains("smallint")) {
            javaType = JavaTypeEnum.SHORT;
        } else if (t.contains("int")) {
            javaType = JavaTypeEnum.INTEGER;
        } else if (t.contains("text")) {
            javaType = JavaTypeEnum.STRING;
        } else if (t.contains("bit")) {
            javaType = JavaTypeEnum.BOOLEAN;
        } else if (t.contains("decimal")) {
            javaType = JavaTypeEnum.BIG_DECIMAL;
        } else if (t.contains("clob")) {
            javaType = JavaTypeEnum.CLOB;
        } else if (t.contains("blob")) {
            javaType = JavaTypeEnum.BLOB;
        } else if (t.contains("binary")) {
            javaType = JavaTypeEnum.BYTE_ARRAY;
        } else if (t.contains("float")) {
            javaType = JavaTypeEnum.FLOAT;
        } else if (t.contains("double")) {
            javaType = JavaTypeEnum.DOUBLE;
        } else if (t.contains("json") || t.contains("enum")) {
            javaType = JavaTypeEnum.STRING;
        } else if (t.contains("timestamp")) {
            javaType = JavaTypeEnum.TIMESTAMP;
        } else if (t.contains("datetime")) {
            javaType = JavaTypeEnum.DATE;
        } else if (t.contains("date") || t.contains("time") || t.contains("year")) {
            javaType = JavaTypeEnum.DATE;
        } else {
            javaType = JavaTypeEnum.STRING;
        }
        return javaType;
    }
}
