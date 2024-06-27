package org.wyj.query;

import org.wyj.converts.ITypeConvertor;
import org.wyj.converts.JavaTypeEnum;
import org.wyj.entity.TableFieldInfo;
import org.wyj.entity.TableInfo;
import org.wyj.utils.ConnectionUtil;
import org.wyj.utils.NamingUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MySQLDbQuery implements IDbQuery {
    private static final String TABLE_SQL = "SHOW TABLE STATUS WHERE name = '%s'";
    private static final String TABLE_FIELD_SQL = "SHOW FULL FIELDS FROM %s";

    @Override
    public TableInfo tableSql(String tableName) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        String tableSql = String.format(TABLE_SQL, tableName);
        PreparedStatement pStatement = connection.prepareStatement(tableSql);
        // pStatement.setString(1, tableName); // 索引从1开始
        ResultSet resultSet = pStatement.executeQuery();
        ConnectionUtil.closeConnection(connection);

        TableInfo tableInfo = null;
        if (resultSet.next()) {
            tableInfo = new TableInfo();
            tableInfo.setTableName(resultSet.getString("Name"));
            tableInfo.setComment(resultSet.getString("Comment"));
        }
        return tableInfo;
    }

    @Override
    public List<TableFieldInfo> tableFieldsSql(String tableName, ITypeConvertor typeConvertor)
            throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        String formatSql = String.format(TABLE_FIELD_SQL, tableName);
        PreparedStatement pStatement = connection.prepareStatement(formatSql);
        ResultSet resultSet = pStatement.executeQuery();
        ConnectionUtil.closeConnection(connection);

        ArrayList<TableFieldInfo> tableFieldInfoList = new ArrayList<>();
        while (resultSet.next()) {
            TableFieldInfo tableFieldInfo = new TableFieldInfo();
            tableFieldInfoList.add(tableFieldInfo);

            String columnName = resultSet.getString("Field");
            String dbType = resultSet.getString("Type");
            tableFieldInfo.setColumnName(columnName);
            tableFieldInfo.setDbType(dbType);
            tableFieldInfo.setComment(resultSet.getString("Comment"));
            tableFieldInfo.setKey(resultSet.getString("Key"));
            tableFieldInfo.setPropertyName(NamingUtil.underlineToCamel(columnName));
            JavaTypeEnum javaType = typeConvertor.convertType(dbType);
            tableFieldInfo.setJavaType(javaType.getJavaType());
            tableFieldInfo.setJavaTypeFullName(javaType.getJavaTypeFullName());
            tableFieldInfo.setJdbcType(javaType.getJdbcType());
        }
        return tableFieldInfoList;
    }
}
