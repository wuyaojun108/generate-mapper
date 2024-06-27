package org.wyj.converts;

import java.math.BigDecimal;
import java.sql.*;
import java.time.*;

public enum JavaTypeEnum {
    // 包装类型
    BYTE("Byte", Byte.class.getTypeName(), JdbcTypeEnum.TINYINT.name()),
    SHORT("Short", Short.class.getTypeName(), JdbcTypeEnum.SMALLINT.name()),
    INTEGER("Integer", Integer.class.getTypeName(), JdbcTypeEnum.INTEGER.name()),
    LONG("Long", Long.class.getTypeName(), JdbcTypeEnum.BIGINT.name()),
    FLOAT("Float", Float.class.getTypeName(), JdbcTypeEnum.FLOAT.name()),
    DOUBLE("Double", Double.class.getTypeName(), JdbcTypeEnum.DOUBLE.name()),
    BOOLEAN("Boolean", Boolean.class.getTypeName(), JdbcTypeEnum.BOOLEAN.name()),
    STRING("String", String.class.getTypeName(), JdbcTypeEnum.VARCHAR.name()),

    // sql 包下数据类型
    DATE_SQL("Date", Date.class.getTypeName(), JdbcTypeEnum.DATE.name()),
    TIME("Time", Time.class.getTypeName(), JdbcTypeEnum.TIME.name()),
    TIMESTAMP("Timestamp", Timestamp.class.getTypeName(), JdbcTypeEnum.TIMESTAMP.name()),
    BLOB("Blob", Blob.class.getTypeName(), JdbcTypeEnum.BLOB.name()),
    CLOB("Clob", Clob.class.getTypeName(), JdbcTypeEnum.CLOB.name()),

    // java8 新时间类型
    LOCAL_DATE("LocalDate", LocalDate.class.getTypeName(), JdbcTypeEnum.DATE.name()),
    LOCAL_TIME("LocalTime", LocalTime.class.getTypeName(), JdbcTypeEnum.TIME.name()),
    YEAR("Year", Year.class.getTypeName(), JdbcTypeEnum.DATE.name()),
    YEAR_MONTH("YearMonth", YearMonth.class.getTypeName(), JdbcTypeEnum.DATE.name()),
    LOCAL_DATE_TIME("LocalDateTime", LocalDateTime.class.getTypeName(), JdbcTypeEnum.TIMESTAMP.name()),
    INSTANT("Instant", Instant.class.getTypeName(), JdbcTypeEnum.TIMESTAMP.name()),

    // 其他杂类
    BYTE_ARRAY("byte[]", byte[].class.getTypeName(), JdbcTypeEnum.BINARY.name()),
    DATE("Date", java.util.Date.class.getTypeName(), JdbcTypeEnum.TIMESTAMP.name()),
    BIG_DECIMAL("BigDecimal", BigDecimal.class.getTypeName(), JdbcTypeEnum.DECIMAL.name());

    // 类名
    private final String javaType;
    // 包名
    private final String javaTypeFullName;
    private final String jdbcType;

    JavaTypeEnum(String javaType, String javaTypeFullName, String jdbcType) {
        this.javaType = javaType;
        this.javaTypeFullName = javaTypeFullName;
        this.jdbcType = jdbcType;
    }

    public String getJavaType() {
        return javaType;
    }

    public String getJavaTypeFullName() {
        return javaTypeFullName;
    }

    public String getJdbcType() {
        return jdbcType;
    }
}
