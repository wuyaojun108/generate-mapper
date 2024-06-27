package org.wyj.utils;

import org.wyj.entity.TableFieldInfo;
import org.wyj.entity.TableInfo;

import java.util.List;

public class XmlUtil {
    private static final int LINE_LIMIT = 100;
    private static final String NEW_LINE = "\n";
    private static final String FOUR_SPACE = "    ";
    private static final String EIGHT_SPACE = FOUR_SPACE + FOUR_SPACE;
    private static final String TWELVE_SPACE = FOUR_SPACE + FOUR_SPACE + FOUR_SPACE;
    private static final String SIXTEEN_SPACE = FOUR_SPACE + FOUR_SPACE + FOUR_SPACE + FOUR_SPACE;
    private static final String XML_VERSION_STATEMENT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String XML_DOCTYPE_STATEMENT = "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3"
            + ".0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">";


    public static String generateMapperXml(TableInfo tableInfo, String entityPkg, String mapperInterfacePkg) {
        String entityName = tableInfo.getEntityName();
        String entityFullName = entityPkg + "." + entityName;
        String mapperNamespace = mapperInterfacePkg + "." + entityName + "Mapper";

        String tableName = tableInfo.getTableName();
        Boolean hasPrimaryKey = tableInfo.getHasPrimaryKey();
        List<TableFieldInfo> tableFieldInfoList = tableInfo.getTableFieldInfoList();
        List<TableFieldInfo> primaryKeyColumnList = tableInfo.getPrimaryKeyColumnList();
        List<TableFieldInfo> commonColumnList = tableInfo.getCommonColumnList();


        StringBuilder sBuilder = new StringBuilder();
        // 拼接xml文件的声明语句
        sBuilder.append(XML_VERSION_STATEMENT).append(NEW_LINE);
        sBuilder.append(XML_DOCTYPE_STATEMENT).append(NEW_LINE).append(NEW_LINE);

        // 拼接mapper节点的左标签
        sBuilder.append("<mapper namespace=\"").append(mapperNamespace).append("\">").append(NEW_LINE).append(NEW_LINE);
        // 拼接resultMap节点
        sBuilder.append(concatResultMapNode(entityFullName, primaryKeyColumnList, commonColumnList)).append(NEW_LINE);
        // 拼接BaseColumn节点
        sBuilder.append(concatBaseColumnNode(tableFieldInfoList)).append(NEW_LINE);
        // 拼接selectList节点
        sBuilder.append(concatSelectListMethod(tableName)).append(NEW_LINE);
        // 拼接insert节点
        sBuilder.append(concatInsertMethod(entityFullName, hasPrimaryKey, tableName, tableFieldInfoList))
                .append(NEW_LINE);
        if (hasPrimaryKey) {
            // 拼接selectByPrimaryKey节点
            sBuilder.append(concatSelectByPrimaryKeyMethod(tableName,
                    primaryKeyColumnList)).append(NEW_LINE);
            // 拼接updateByPrimaryKey节点
            sBuilder.append(concatUpdateByPrimaryKeyMethod(tableName, entityFullName,
                            primaryKeyColumnList, commonColumnList))
                    .append(NEW_LINE);
            // 拼接deleteByPrimaryKey节点
            sBuilder.append(concatDeleteByPrimaryKey(tableName, primaryKeyColumnList)).append(NEW_LINE);
        }
        // 拼接mapper节点的右标签
        sBuilder.append("</mapper>").append(NEW_LINE);
        return sBuilder.toString();
    }

    private static String concatDeleteByPrimaryKey(String tableName,
                                                   List<TableFieldInfo> primaryKeyColumnList) {
        TableFieldInfo pkField = primaryKeyColumnList.get(0);
        return FOUR_SPACE + "<delete id=\"deleteByPrimaryKey\" parameterType=\"" +
                pkField.getJavaTypeFullName() + "\">" + NEW_LINE +
                EIGHT_SPACE + "DELETE FROM " + tableName + NEW_LINE +
                EIGHT_SPACE + "WHERE " + pkField.getColumnName() + " = " +
                concatMybatisSqlParam(pkField.getPropertyName(), pkField.getJdbcType()) + NEW_LINE +
                FOUR_SPACE + "</delete>" + NEW_LINE;
    }

    private static String concatUpdateByPrimaryKeyMethod(String tableName, String entityFullName,
                                                         List<TableFieldInfo> primaryKeyColumnList,
                                                         List<TableFieldInfo> commonColumnList) {

        TableFieldInfo pkField = primaryKeyColumnList.get(0);

        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(FOUR_SPACE).append("<update id=\"updateByPrimaryKey\" ");
        String parameterTypeStatement = concatParameterTypeStatement(entityFullName);
        if (sBuilder.length() + parameterTypeStatement.length() > LINE_LIMIT) {
            sBuilder.append(NEW_LINE).append(TWELVE_SPACE);
        }
        sBuilder.append(parameterTypeStatement).append(">").append(NEW_LINE);
        sBuilder.append(EIGHT_SPACE).append("UPDATE ").append(tableName).append(NEW_LINE);
        sBuilder.append(EIGHT_SPACE).append("<set>").append(NEW_LINE);
        for (TableFieldInfo field : commonColumnList) {
            sBuilder.append(concatIfNode(field.getColumnName(), field.getPropertyName(), field.getJdbcType()));
        }
        sBuilder.append(EIGHT_SPACE).append("</set>").append(NEW_LINE);
        sBuilder.append(EIGHT_SPACE).append("WHERE ").append(pkField.getColumnName()).append(" = ")
                .append(concatMybatisSqlParam(pkField.getPropertyName(), pkField.getJdbcType())).append(NEW_LINE);
        sBuilder.append(FOUR_SPACE).append("</update>").append(NEW_LINE);
        return sBuilder.toString();
    }

    private static String concatIfNode(String columnName, String propertyName, String jdbcType) {
        return TWELVE_SPACE + "<if test=\"" + propertyName + " != null\">" + NEW_LINE +
                SIXTEEN_SPACE + columnName + " = " + concatMybatisSqlParam(propertyName, jdbcType) + "," + NEW_LINE +
                TWELVE_SPACE + "</if>" + NEW_LINE;
    }

    private static String concatSelectByPrimaryKeyMethod(String tableName,
                                                         List<TableFieldInfo> primaryKeyColumnList) {
        TableFieldInfo pkField = primaryKeyColumnList.get(0);
        String sqlParam = concatMybatisSqlParam(pkField.getPropertyName(), pkField.getJdbcType());

        return FOUR_SPACE + "<select id=\"selectByPrimaryKey\" parameterType=\""
                + pkField.getJavaTypeFullName() + "\" resultMap=\"BaseResultMap\">" + NEW_LINE
                + EIGHT_SPACE + "SELECT <include refid=\"BaseColumn\" />" + NEW_LINE
                + EIGHT_SPACE + "FROM " + tableName + NEW_LINE
                + EIGHT_SPACE + "WHERE " + pkField.getColumnName() + " = " + sqlParam + NEW_LINE
                + FOUR_SPACE + "</select>" + NEW_LINE;
    }

    private static String concatInsertMethod(String entityFullName, Boolean hasPrimaryKey,
                                             String tableName,
                                             List<TableFieldInfo> tableFieldInfoList) {

        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(FOUR_SPACE).append("<insert id=\"insert\" ");
        String parameterTypeStatement = concatParameterTypeStatement(entityFullName);
        if (sBuilder.length() + parameterTypeStatement.length() > 80) {
            sBuilder.append(NEW_LINE).append(TWELVE_SPACE);
        }
        sBuilder.append(parameterTypeStatement);
        if (hasPrimaryKey) {
            sBuilder.append(NEW_LINE).append(TWELVE_SPACE)
                    .append("useGeneratedKeys=\"true\" keyProperty=\"id\">").append(NEW_LINE);
        } else {
            sBuilder.append(">").append(NEW_LINE);
        }
        sBuilder.append(concatInsertIntoStatement(tableName, tableFieldInfoList)).append(NEW_LINE);
        sBuilder.append(concatInsertValuesStatement(tableFieldInfoList)).append(NEW_LINE);
        sBuilder.append(FOUR_SPACE).append("</insert>").append(NEW_LINE);
        return sBuilder.toString();
    }

    private static String concatParameterTypeStatement(String entityFullName) {
        return "parameterType=\"" + entityFullName + "\"";
    }

    private static String concatInsertValuesStatement(List<TableFieldInfo> tableFieldInfoList) {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(EIGHT_SPACE).append("VALUES (");
        int lineLen = sBuilder.length();
        for (TableFieldInfo field : tableFieldInfoList) {
            String sqlParam = concatMybatisSqlParam(field.getPropertyName(), field.getJdbcType());
            if ((lineLen + sqlParam.length()) > LINE_LIMIT) {
                sBuilder.append(NEW_LINE).append(TWELVE_SPACE);
                lineLen = TWELVE_SPACE.length();
            }
            sBuilder.append(sqlParam).append(", ");
            lineLen += (sqlParam.length() + 2);
        }
        sBuilder.delete(sBuilder.length() - 2, sBuilder.length());
        sBuilder.append(")");
        return sBuilder.toString();
    }

    public static String concatMybatisSqlParam(String propertyName, String jdbcType) {
        return "#{" + propertyName + "," + "jdbcType=" + jdbcType + "}";
    }

    private static String concatInsertIntoStatement(String tableName,
                                                    List<TableFieldInfo> tableFieldInfoList) {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(EIGHT_SPACE).append("INSERT INTO ").append(tableName).append("(");
        int lineLen = sBuilder.length();
        for (TableFieldInfo field : tableFieldInfoList) {
            String columnName = field.getColumnName();
            if ((lineLen + columnName.length()) > LINE_LIMIT) {
                sBuilder.append(NEW_LINE).append(TWELVE_SPACE);
                lineLen = TWELVE_SPACE.length();
            }
            sBuilder.append(columnName).append(", ");
            lineLen += (columnName.length() + 2);
        }
        sBuilder.delete(sBuilder.length() - 2, sBuilder.length());
        sBuilder.append(")");
        return sBuilder.toString();
    }

    private static String concatSelectListMethod(String tableName) {
        return FOUR_SPACE + "<select id=\"selectList\" resultMap=\"BaseResultMap\">" + NEW_LINE +
                EIGHT_SPACE + "SELECT <include refid=\"BaseColumn\" />" + NEW_LINE +
                EIGHT_SPACE + "FROM " + tableName + NEW_LINE +
                FOUR_SPACE + "</select>" + NEW_LINE;
    }

    private static String concatBaseColumnNode(List<TableFieldInfo> tableFieldInfoList) {
        return FOUR_SPACE + "<sql id=\"BaseColumn\">" + NEW_LINE
                + concatColumnListStatement(tableFieldInfoList) + NEW_LINE
                + FOUR_SPACE + "</sql>" + NEW_LINE;
    }

    private static String concatColumnListStatement(List<TableFieldInfo> tableFieldInfoList) {
        StringBuilder sBuilder = new StringBuilder();
        int lineLen = 0;
        sBuilder.append(EIGHT_SPACE);
        lineLen += EIGHT_SPACE.length();
        for (TableFieldInfo field : tableFieldInfoList) {
            String columnName = field.getColumnName();
            if ((lineLen + columnName.length() + 2) > LINE_LIMIT) {
                sBuilder.append(NEW_LINE).append(EIGHT_SPACE);
                lineLen = EIGHT_SPACE.length();
            }
            sBuilder.append(columnName).append(", ");
            lineLen += (columnName.length() + 2);
        }
        sBuilder.delete(sBuilder.length() - 2, sBuilder.length());
        return sBuilder.toString();
    }

    private static String concatResultMapNode(String entityFullName,
                                              List<TableFieldInfo> primaryKeyColumnList,
                                              List<TableFieldInfo> commonColumnList) {

        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(FOUR_SPACE).append("<resultMap id=\"BaseResultMap\"");
        String typeStatement = " type=\"" + entityFullName + "\">";
        if (sBuilder.length() + typeStatement.length() > 100) {
            sBuilder.append(NEW_LINE).append(TWELVE_SPACE).append(typeStatement).append(NEW_LINE);
        } else {
            sBuilder.append(typeStatement).append(NEW_LINE);
        }

        for (TableFieldInfo field : primaryKeyColumnList) {
            sBuilder.append(EIGHT_SPACE).append("<id column=\"").append(field.getColumnName())
                    .append("\" jdbcType=\"").append(field.getJdbcType())
                    .append("\" property=\"").append(field.getPropertyName())
                    .append("\" />").append(NEW_LINE);
        }

        for (TableFieldInfo field : commonColumnList) {
            sBuilder.append(EIGHT_SPACE).append("<result column=\"").append(field.getColumnName())
                    .append("\" jdbcType=\"").append(field.getJdbcType())
                    .append("\" property=\"").append(field.getPropertyName())
                    .append("\" />").append(NEW_LINE);
        }
        sBuilder.append(FOUR_SPACE).append("</resultMap>").append(NEW_LINE);
        return sBuilder.toString();
    }
}
