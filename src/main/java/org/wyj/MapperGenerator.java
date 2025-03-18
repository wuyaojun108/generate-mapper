package org.wyj;

import org.wyj.config.DbRegistry;
import org.wyj.config.GlobalConfig;
import org.wyj.config.GlobalConfig.TableBasicMsgConfig;
import org.wyj.constants.Constant;
import org.wyj.converts.ITypeConvertor;
import org.wyj.entity.TableFieldInfo;
import org.wyj.entity.TableInfo;
import org.wyj.query.DbTypeEnum;
import org.wyj.query.IDbQuery;
import org.wyj.utils.CodeUtil;
import org.wyj.utils.ConnectionUtil;
import org.wyj.utils.FileUtil;
import org.wyj.utils.XmlUtil;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MapperGenerator {

    public static void execute() {
        execute(null);
    }

    public static void execute(DataSource dataSource) {
        MapperGenerator mapperGenerator = new MapperGenerator();
        try {
            // 加载配置文件
            GlobalConfig config = mapperGenerator.loadConfig();

            // 如果不知道数据库的连接参数，在这里可以向config对象中注入数据库连接池，同时在配置文件中指定数据库类型
            config.setDataSource(dataSource);

            // 初始化：获取生成mapper文件需要的组件
            DbTypeEnum dbType;
            if (config.getJdbcDbType() == null || "".equals(config.getJdbcDbType())) {
                dbType = mapperGenerator.parseDbTypeByJdbcUrl(config.getJdbcUrl());
            } else {
                dbType = DbTypeEnum.getDbTypeByName(config.getJdbcDbType());
            }
            IDbQuery dbQuery = DbRegistry.getDbQueryByDbType(dbType);
            ITypeConvertor typeConvertor = DbRegistry.getTypeConvertorByDbType(dbType);
            ConnectionUtil.createConnection(config);
            // 退出程序时关闭数据库连接
            Runtime.getRuntime().addShutdownHook(new Thread(ConnectionUtil::cancelConnection));

            // 读取表的元数据信息
            List<TableInfo> tableInfoList = mapperGenerator.readDbMetadata(config, dbQuery, typeConvertor);

            // 生成代码、写出文件
            mapperGenerator.concatCodeAndWriteFile(config, tableInfoList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 读取表的元数据信息
    private List<TableInfo> readDbMetadata(GlobalConfig config, IDbQuery dbQuery, ITypeConvertor typeConvertor)
            throws Exception {

        ArrayList<TableInfo> tableInfoList = new ArrayList<>();
        List<TableBasicMsgConfig> tableBasicMsgConfigList = config.getTableBasicMsgConfigList();
        for (TableBasicMsgConfig tableBasicMsgConfig : tableBasicMsgConfigList) {
            String tableName = tableBasicMsgConfig.getTableName();
            String entityName = tableBasicMsgConfig.getEntityName();
            String mapperName = tableBasicMsgConfig.getMapperName();

            String objName = mapperName.substring(0, mapperName.indexOf("Mapper"));
            String serviceInterfaceName = "I" + objName + "Service";
            String serviceImplName = objName + "ServiceImpl";
            String serviceTestName = objName + "ServiceTest";

            // 读取表信息
            TableInfo tableInfo = dbQuery.tableSql(tableName);
            tableInfo.setEntityName(entityName);
            tableInfo.setMapperName(mapperName);
            // 读取字段信息
            List<TableFieldInfo> tableFieldInfoList = dbQuery.tableFieldsSql(tableName, typeConvertor);

            boolean hasPrimaryKey = false;
            ArrayList<TableFieldInfo> primaryKeyColumnList = new ArrayList<>();
            ArrayList<TableFieldInfo> commonColumnList = new ArrayList<>();
            for (TableFieldInfo tableFieldInfo : tableFieldInfoList) {
                String key = tableFieldInfo.getKey();
                if (Constant.PRI.equals(key)) {
                    hasPrimaryKey = true;
                    primaryKeyColumnList.add(tableFieldInfo);
                } else {
                    commonColumnList.add(tableFieldInfo);
                }
            }

            tableInfo.setTableFieldInfoList(tableFieldInfoList);
            tableInfo.setHasPrimaryKey(hasPrimaryKey);
            tableInfo.setPrimaryKeyColumnList(primaryKeyColumnList);
            tableInfo.setCommonColumnList(commonColumnList);
            tableInfo.setServiceInterfaceName(serviceInterfaceName);
            tableInfo.setServiceImplName(serviceImplName);
            tableInfo.setServiceTestName(serviceTestName);
            tableInfoList.add(tableInfo);
        }
        return tableInfoList;
    }

    // 生成代码、写出文件
    private void concatCodeAndWriteFile(GlobalConfig config, List<TableInfo> tableInfoList) throws Exception {
        String codeDir = config.getCodeDir();
        String entityPkg = config.getEntityPkg();
        String mapperInterfacePkg = config.getMapperInterfacePkg();
        String serviceInterfacePkg = config.getServiceInterfacePkg();
        String resourceDir = config.getResourceDir();
        String mapperPath = config.getMapperPath();
        String testCodeDir = config.getTestCodeDir();
        String testCodePkg = config.getTestCodePkg();
        String author = config.getCodeAuthor();

        String entityPath = codeDir + "/" + entityPkg.replace(".", "/");
        String mapperInterfacePath = codeDir + "/" + mapperInterfacePkg.replace(".", "/");
        String serviceInterfacePath = codeDir + "/" + serviceInterfacePkg.replace(".", "/");
        String serviceImplPath = codeDir + "/" + serviceInterfacePkg.replace(".", "/");
        String mapperXmlPath = resourceDir + "/" + mapperPath;
        String testCodePath = testCodeDir + "/" + testCodePkg.replace(".", "/");

        for (TableInfo tableInfo : tableInfoList) {
            // 生成实体类
            String entityStr = CodeUtil.generateEntity(tableInfo, entityPkg, author);
            // 生成mapper接口
            String mapperInterfaceStr = CodeUtil.generateMapperInterface(tableInfo, entityPkg,
                    mapperInterfacePkg, author);
            // 生成mapper.xml
            String mapperXmlStr = XmlUtil.generateMapperXml(tableInfo, entityPkg, mapperInterfacePkg);
            // 生成service接口
            String serviceInterfaceStr = CodeUtil.generateServiceInterface(tableInfo, entityPkg,
                    serviceInterfacePkg, author);
            // 生成service的实现类
            String serviceImplStr = CodeUtil.generateServiceImpl(tableInfo, entityPkg, mapperInterfacePkg,
                    serviceInterfacePkg, author);
            // 生成service的测试类
            String serviceTestStr = CodeUtil.generateServiceTest(tableInfo, entityPkg,
                    serviceInterfacePkg, author, testCodePkg);

            // 写出文件
            FileUtil.writeToFile(entityStr, entityPath, tableInfo.getEntityName() + ".java");
            FileUtil.writeToFile(mapperInterfaceStr, mapperInterfacePath, tableInfo.getMapperName() + ".java");
            FileUtil.writeToFile(mapperXmlStr, mapperXmlPath, tableInfo.getMapperName() + ".xml");
            FileUtil.writeToFile(serviceInterfaceStr, serviceInterfacePath,
                    tableInfo.getServiceInterfaceName() + ".java");
            FileUtil.writeToFile(serviceImplStr, serviceImplPath, tableInfo.getServiceImplName() + ".java");
            FileUtil.writeToFile(serviceTestStr, testCodePath, tableInfo.getServiceTestName() + ".java");
        }
    }

    private DbTypeEnum parseDbTypeByJdbcUrl(String jdbcUrl) throws Exception {
        String dbName = jdbcUrl.substring(5, jdbcUrl.indexOf("://"));
        DbTypeEnum dbType = DbTypeEnum.getDbTypeByName(dbName);
        if (dbType == null) {
            throw new Exception("不支持当前数据库：" + dbName);
        }
        return dbType;
    }

    /**
     * 加载默认的配置文件
     */
    private GlobalConfig loadConfig() throws IOException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = contextClassLoader.getResourceAsStream(Constant.DEFAULT_CONFIG_FILE);
        Properties properties = new Properties();
        properties.load(inputStream);

        GlobalConfig globalConfig = new GlobalConfig();
        String jdbcTables = properties.getProperty(Constant.JDBC_TABLES);
        globalConfig.setJdbcDriver(properties.getProperty(Constant.JDBC_DRIVER));
        globalConfig.setJdbcUrl(properties.getProperty(Constant.JDBC_URL));
        globalConfig.setJdbcUsername(properties.getProperty(Constant.JDBC_USERNAME));
        globalConfig.setJdbcPassword(properties.getProperty(Constant.JDBC_PASSWORD));
        globalConfig.setJdbcTables(jdbcTables);
        globalConfig.setCodeDir(properties.getProperty(Constant.CODE_DIR));
        globalConfig.setEntityPkg(properties.getProperty(Constant.ENTITY_PKG));
        globalConfig.setMapperInterfacePkg(properties.getProperty(Constant.MAPPER_INTERFACE_PKG));
        globalConfig.setResourceDir(properties.getProperty(Constant.RESOURCE_DIR));
        globalConfig.setMapperPath(properties.getProperty(Constant.MAPPER_PATH));
        globalConfig.setCodeAuthor(properties.getProperty(Constant.CODE_AUTHOR));
        globalConfig.setJdbcDbType(properties.getProperty(Constant.JDBC_DB_TYPE));
        globalConfig.setServiceInterfacePkg(properties.getProperty(Constant.SERVICE_INTERFACE_PKG));
        globalConfig.setTestCodeDir(properties.getProperty(Constant.TEST_CODE_DIR));
        globalConfig.setTestCodePkg(properties.getProperty(Constant.TEST_CODE_PKG));

        List<TableBasicMsgConfig> tableBasicMsgConfigList = getTableBasicMsgConfigs(jdbcTables);
        globalConfig.setTableBasicMsgConfigList(tableBasicMsgConfigList);
        return globalConfig;
    }

    private List<TableBasicMsgConfig> getTableBasicMsgConfigs(String jdbcTables) throws ExportException {
        List<TableBasicMsgConfig> tableBasicMsgConfigList = new ArrayList<>();
        String[] tablesArr = jdbcTables.split(",");
        for (String tableAndEntity : tablesArr) {
            String[] tableEntityArr = tableAndEntity.split(":");
            if (tableEntityArr.length != 3) {
                throw new ExportException("table名、实例名、mapper接口名称配置错误：" + tableAndEntity);
            }
            TableBasicMsgConfig tableBasicMsgConfig = new TableBasicMsgConfig();
            tableBasicMsgConfig.setTableName(tableEntityArr[0]);
            tableBasicMsgConfig.setEntityName(tableEntityArr[1]);
            tableBasicMsgConfig.setMapperName(tableEntityArr[2]);
            tableBasicMsgConfigList.add(tableBasicMsgConfig);
        }
        return tableBasicMsgConfigList;
    }

}
