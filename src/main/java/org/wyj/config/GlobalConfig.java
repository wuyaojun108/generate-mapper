package org.wyj.config;

import javax.sql.DataSource;
import java.util.List;

/**
 * 全局配置
 */
public class GlobalConfig {
    private String jdbcDriver;
    private String jdbcUrl;
    private String jdbcUsername;
    private String jdbcPassword;
    private String jdbcTables;
    private String codeDir;
    private String entityPkg;
    private String mapperInterfacePkg;
    private String serviceInterfacePkg;
    private String resourceDir;
    private String mapperPath;
    private String codeAuthor;
    private DataSource dataSource;
    private String jdbcDbType;
    private String testCodeDir;
    private String testCodePkg;
    private List<TableBasicMsgConfig> tableBasicMsgConfigList;

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcUsername() {
        return jdbcUsername;
    }

    public void setJdbcUsername(String jdbcUsername) {
        this.jdbcUsername = jdbcUsername;
    }

    public String getJdbcPassword() {
        return jdbcPassword;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }

    public String getJdbcTables() {
        return jdbcTables;
    }

    public void setJdbcTables(String jdbcTables) {
        this.jdbcTables = jdbcTables;
    }

    public String getCodeDir() {
        return codeDir;
    }

    public void setCodeDir(String codeDir) {
        this.codeDir = codeDir;
    }

    public String getEntityPkg() {
        return entityPkg;
    }

    public void setEntityPkg(String entityPkg) {
        this.entityPkg = entityPkg;
    }

    public String getMapperInterfacePkg() {
        return mapperInterfacePkg;
    }

    public void setMapperInterfacePkg(String mapperInterfacePkg) {
        this.mapperInterfacePkg = mapperInterfacePkg;
    }

    public String getResourceDir() {
        return resourceDir;
    }

    public void setResourceDir(String resourceDir) {
        this.resourceDir = resourceDir;
    }

    public String getMapperPath() {
        return mapperPath;
    }

    public void setMapperPath(String mapperPath) {
        this.mapperPath = mapperPath;
    }

    public String getCodeAuthor() {
        return codeAuthor;
    }

    public void setCodeAuthor(String codeAuthor) {
        this.codeAuthor = codeAuthor;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getJdbcDbType() {
        return jdbcDbType;
    }

    public void setJdbcDbType(String jdbcDbType) {
        this.jdbcDbType = jdbcDbType;
    }

    public String getTestCodeDir() {
        return testCodeDir;
    }

    public void setTestCodeDir(String testCodeDir) {
        this.testCodeDir = testCodeDir;
    }

    public String getTestCodePkg() {
        return testCodePkg;
    }

    public void setTestCodePkg(String testCodePkg) {
        this.testCodePkg = testCodePkg;
    }

    public String getServiceInterfacePkg() {
        return serviceInterfacePkg;
    }

    public void setServiceInterfacePkg(String serviceInterfacePkg) {
        this.serviceInterfacePkg = serviceInterfacePkg;
    }

    public List<TableBasicMsgConfig> getTableBasicMsgConfigList() {
        return tableBasicMsgConfigList;
    }

    public void setTableBasicMsgConfigList(List<TableBasicMsgConfig> tableBasicMsgConfigList) {
        this.tableBasicMsgConfigList = tableBasicMsgConfigList;
    }

    public static class TableBasicMsgConfig {
        private String tableName;
        private String entityName;
        private String mapperName;

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public String getEntityName() {
            return entityName;
        }

        public void setEntityName(String entityName) {
            this.entityName = entityName;
        }

        public String getMapperName() {
            return mapperName;
        }

        public void setMapperName(String mapperName) {
            this.mapperName = mapperName;
        }
    }
}
