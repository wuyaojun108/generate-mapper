package org.wyj.entity;

import java.util.List;

public class TableInfo {
    private String tableName;
    private String comment;
    private List<TableFieldInfo> tableFieldInfoList;
    private String entityName;
    private Boolean hasPrimaryKey;
    private List<TableFieldInfo> primaryKeyColumnList;
    private List<TableFieldInfo> commonColumnList;
    private String mapperName;
    private String serviceInterfaceName;
    private String serviceImplName;
    private String serviceTestName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<TableFieldInfo> getTableFieldInfoList() {
        return tableFieldInfoList;
    }

    public void setTableFieldInfoList(List<TableFieldInfo> tableFieldInfoList) {
        this.tableFieldInfoList = tableFieldInfoList;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Boolean getHasPrimaryKey() {
        return hasPrimaryKey;
    }

    public void setHasPrimaryKey(Boolean hasPrimaryKey) {
        this.hasPrimaryKey = hasPrimaryKey;
    }

    public List<TableFieldInfo> getPrimaryKeyColumnList() {
        return primaryKeyColumnList;
    }

    public void setPrimaryKeyColumnList(List<TableFieldInfo> primaryKeyColumnList) {
        this.primaryKeyColumnList = primaryKeyColumnList;
    }

    public List<TableFieldInfo> getCommonColumnList() {
        return commonColumnList;
    }

    public void setCommonColumnList(List<TableFieldInfo> commonColumnList) {
        this.commonColumnList = commonColumnList;
    }

    public String getMapperName() {
        return mapperName;
    }

    public void setMapperName(String mapperName) {
        this.mapperName = mapperName;
    }

    public String getServiceInterfaceName() {
        return serviceInterfaceName;
    }

    public void setServiceInterfaceName(String serviceInterfaceName) {
        this.serviceInterfaceName = serviceInterfaceName;
    }

    public String getServiceImplName() {
        return serviceImplName;
    }

    public void setServiceImplName(String serviceImplName) {
        this.serviceImplName = serviceImplName;
    }

    public String getServiceTestName() {
        return serviceTestName;
    }

    public void setServiceTestName(String serviceTestName) {
        this.serviceTestName = serviceTestName;
    }
}
