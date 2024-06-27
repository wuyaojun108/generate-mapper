package org.wyj.query;

import org.wyj.converts.ITypeConvertor;
import org.wyj.entity.TableFieldInfo;
import org.wyj.entity.TableInfo;

import java.util.List;

public interface IDbQuery {
    TableInfo tableSql(String tableName) throws Exception;

    List<TableFieldInfo> tableFieldsSql(String tableName, ITypeConvertor typeConvertor) throws Exception;
}
