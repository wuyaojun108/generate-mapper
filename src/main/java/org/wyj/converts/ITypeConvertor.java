package org.wyj.converts;

public interface ITypeConvertor {
    JavaTypeEnum convertType(String jdbcTypeName);
}
