package org.wyj.utils;

import org.wyj.entity.TableFieldInfo;
import org.wyj.entity.TableInfo;

import java.util.*;

public class CodeUtil {
    private static final String NEW_LINE = "\n";
    private static final String SEMICOLON = ";";
    private static final String SPACE = " ";
    private static final String FOUR_SPACES = "    ";
    private static final String EIGHT_SPACES = "        ";

    public static String generateEntity(TableInfo tableInfo, String entityPkg, String author) {
        List<TableFieldInfo> tableFieldInfoList = tableInfo.getTableFieldInfoList();

        StringBuilder sBuilder = new StringBuilder();
        // 拼接包名
        sBuilder.append(concatPackageStatement(entityPkg));
        // 拼接import语句
        Set<String> javaTypeFullNameSet = new HashSet<>();
        for (TableFieldInfo tableFieldInfo : tableFieldInfoList) {
            String javaTypeFullName = tableFieldInfo.getJavaTypeFullName();
            if (javaTypeFullName.startsWith("java.lang")) {
                continue;
            }
            javaTypeFullNameSet.add(javaTypeFullName);
        }
        for (String javaTypeFullName : javaTypeFullNameSet) {
            String importStatement = concatImportStatement(javaTypeFullName);
            sBuilder.append(importStatement).append(NEW_LINE);
        }
        sBuilder.append(NEW_LINE);
        // 拼接类声明
        sBuilder.append(concatClassComment(author));
        sBuilder.append("public class ").append(tableInfo.getEntityName()).append(" {").append(NEW_LINE);
        // 拼接字段声明
        for (TableFieldInfo tableFieldInfo : tableFieldInfoList) {
            String propertyName = tableFieldInfo.getPropertyName();
            String javaType = tableFieldInfo.getJavaType();
            String comment = tableFieldInfo.getComment();
            if (comment != null && !comment.isEmpty()) {
                sBuilder.append(concatFieldComment(comment)).append(NEW_LINE);
            }
            sBuilder.append(concatFieldStatement(javaType, propertyName)).append(NEW_LINE);
        }
        sBuilder.append(NEW_LINE);
        // 拼接getter/setter方法
        for (TableFieldInfo tableFieldInfo : tableFieldInfoList) {
            String propertyName = tableFieldInfo.getPropertyName();
            String javaType = tableFieldInfo.getJavaType();

            String getterMethodStr = concatGetterMethod(javaType, propertyName);
            String setterMethodStr = concatSetterMethod(javaType, propertyName);
            sBuilder.append(getterMethodStr).append(NEW_LINE);
            sBuilder.append(setterMethodStr).append(NEW_LINE);
        }
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        sBuilder.append("}");
        return sBuilder.toString();
    }

    private static String concatFieldComment(String comment) {
        return FOUR_SPACES + "/**" + NEW_LINE +
                FOUR_SPACES + " * " + comment + NEW_LINE +
                FOUR_SPACES + " */";
    }

    public static String generateMapperInterface(TableInfo tableInfo, String entityPkg,
                                                 String mapperInterfacePkg, String author) {

        String entityName = tableInfo.getEntityName();
        String entityObjName = entityName.substring(0, 1).toLowerCase(Locale.ROOT) +
                entityName.substring(1);
        List<TableFieldInfo> primaryKeyColumnList = tableInfo.getPrimaryKeyColumnList();
        String mapperName = tableInfo.getMapperName();

        StringBuilder sBuilder = new StringBuilder();
        // 拼接包名
        sBuilder.append(concatPackageStatement(mapperInterfacePkg));
        // 拼接import语句
        Set<String> javaTypeFullNameSet = new LinkedHashSet<>();
        javaTypeFullNameSet.add("java.util.List");
        javaTypeFullNameSet.add(entityPkg + "." + entityName);
        if (tableInfo.getHasPrimaryKey()) {
            for (TableFieldInfo tableFieldInfo : primaryKeyColumnList) {
                String javaTypeFullName = tableFieldInfo.getJavaTypeFullName();
                if (javaTypeFullName.startsWith("java.lang.")) {
                    continue;
                }
                javaTypeFullNameSet.add(javaTypeFullName);
            }
        }
        for (String javaTypeFullName : javaTypeFullNameSet) {
            String importStatement = concatImportStatement(javaTypeFullName);
            sBuilder.append(importStatement).append(NEW_LINE);
        }
        sBuilder.append(NEW_LINE);
        // 拼接接口声明
        sBuilder.append(concatClassComment(author, tableInfo.getComment()));
        sBuilder.append("public interface ").append(mapperName).append(" {").append(NEW_LINE);
        // 拼接selectList方法
        sBuilder.append(concatSelectListMethod(entityName));
        // 拼接insert方法
        sBuilder.append(concatInsertMethod(entityName, entityObjName));

        // 如果数据表有主键，拼接selectByPrimaryKey、updateByPrimaryKey、deleteByPrimaryKey方法
        if (tableInfo.getHasPrimaryKey()) {
            // 拼接selectByPrimaryKey方法
            sBuilder.append(concatSelectByPrimaryKeyMethod(entityName, primaryKeyColumnList));
            // 拼接updateByPrimaryKey方法
            sBuilder.append(concatUpdateByPrimaryKeyMethod(entityName, entityObjName));
            // 拼接deleteByPrimaryKey方法
            sBuilder.append(concatDeleteByPrimaryKeyMethod(primaryKeyColumnList));
        }
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        sBuilder.append("}").append(NEW_LINE);
        return sBuilder.toString();
    }

    public static String generateServiceInterface(TableInfo tableInfo, String entityPkg,
                                                  String serviceInterfacePkg, String author) {

        String entityName = tableInfo.getEntityName();
        String entityObjName = entityName.substring(0, 1).toLowerCase(Locale.ROOT) + entityName.substring(1);
        String serviceInterfaceName = tableInfo.getServiceInterfaceName();
        List<TableFieldInfo> primaryKeyColumnList = tableInfo.getPrimaryKeyColumnList();

        StringBuilder sBuilder = new StringBuilder();
        // 拼接包名
        sBuilder.append(concatPackageStatement(serviceInterfacePkg));
        // 拼接import语句
        Set<String> javaTypeFullNameSet = new LinkedHashSet<>();
        javaTypeFullNameSet.add("java.util.List");
        javaTypeFullNameSet.add(entityPkg + "." + entityName);
        for (String javaTypeFullName : javaTypeFullNameSet) {
            String importStatement = concatImportStatement(javaTypeFullName);
            sBuilder.append(importStatement).append(NEW_LINE);
        }
        sBuilder.append(NEW_LINE);
        // 拼接接口声明
        sBuilder.append(concatClassComment(author));
        sBuilder.append("public interface ").append(serviceInterfaceName).append(" {").append(NEW_LINE);

        // 拼接selectAll方法
        sBuilder.append(concatSelectAllMethod(entityName));
        // 拼接insert方法
        if (tableInfo.getHasPrimaryKey()) {
            sBuilder.append(concatCreateWithPkMethod(entityName, entityObjName, primaryKeyColumnList));
        } else {
            sBuilder.append(concatCreateMethod(entityName, entityObjName));
        }

        // 如果数据表有主键，拼接selectById、update、delete方法
        if (tableInfo.getHasPrimaryKey()) {
            // 拼接selectById方法
            sBuilder.append(concatSelectByIdMethod(entityName, primaryKeyColumnList));
            // 拼接update方法
            sBuilder.append(concatUpdateMethod(entityName, entityObjName));
            // 拼接delete方法
            sBuilder.append(concatDeleteMethod(primaryKeyColumnList));
        }
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        sBuilder.append("}").append(NEW_LINE);

        return sBuilder.toString();
    }

    public static String generateServiceImpl(TableInfo tableInfo, String entityPkg,
                                             String mapperInterfacePkg, String serviceInterfacePkg,
                                             String author) {

        String entityName = tableInfo.getEntityName();
        String entityObjName = entityName.substring(0, 1).toLowerCase(Locale.ROOT) + entityName.substring(1);
        String mapperName = tableInfo.getMapperName();
        String mapperObjName = mapperName.substring(0, 1).toLowerCase(Locale.ROOT) + mapperName.substring(1);
        String serviceInterfaceName = tableInfo.getServiceInterfaceName();
        String serviceImplName = tableInfo.getServiceImplName();
        List<TableFieldInfo> primaryKeyColumnList = tableInfo.getPrimaryKeyColumnList();

        StringBuilder sBuilder = new StringBuilder();
        // 拼接包名
        sBuilder.append(concatPackageStatement(serviceInterfacePkg));
        // 拼接import语句
        Set<String> javaTypeFullNameSet = new LinkedHashSet<>();
        javaTypeFullNameSet.add("java.util.List");
        javaTypeFullNameSet.add(entityPkg + "." + entityName);
        javaTypeFullNameSet.add(mapperInterfacePkg + "." + mapperName);
        javaTypeFullNameSet.add(serviceInterfacePkg + "." + serviceInterfaceName);
        for (String javaTypeFullName : javaTypeFullNameSet) {
            String importStatement = concatImportStatement(javaTypeFullName);
            sBuilder.append(importStatement).append(NEW_LINE);
        }
        sBuilder.append(NEW_LINE);
        // 拼接接口声明
        sBuilder.append(concatClassComment(author));
        sBuilder.append("public class ").append(serviceImplName)
                .append(" implements ").append(serviceInterfaceName).append(" {").append(NEW_LINE).append(NEW_LINE);

        sBuilder.append(FOUR_SPACES).append("@Autowired").append(NEW_LINE)
                .append(FOUR_SPACES).append("private ").append(mapperName).append(" ").append(mapperObjName).append(";")
                .append(NEW_LINE).append(NEW_LINE);

        // 拼接selectAllImpl方法
        sBuilder.append(concatSelectAllImplMethod(entityName, mapperObjName));

        // 拼接insertImpl方法
        if (tableInfo.getHasPrimaryKey()) {
            sBuilder.append(concatCreateImplWithPkMethod(entityName, entityObjName, mapperObjName, primaryKeyColumnList));
        } else {
            sBuilder.append(concatCreateImplMethod(entityName, entityObjName, mapperObjName));
        }

        // 如果数据表有主键，拼接selectByIdImpl、updateImpl、deleteImpl方法
        if (tableInfo.getHasPrimaryKey()) {
            // 拼接selectById方法
            sBuilder.append(concatSelectByIdImplMethod(entityName, mapperObjName, primaryKeyColumnList));
            // 拼接update方法
            sBuilder.append(concatUpdateImplMethod(entityName, entityObjName, mapperObjName));
            // 拼接delete方法
            sBuilder.append(concatDeleteImplMethod(mapperObjName, primaryKeyColumnList));
        }
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        sBuilder.append("}").append(NEW_LINE);

        return sBuilder.toString();
    }


    public static String generateServiceTest(TableInfo tableInfo, String entityPkg,
                                             String serviceInterfacePkg, String author, String testCodePkg) {

        String entityName = tableInfo.getEntityName();
        String entityObjName = entityName.substring(0, 1).toLowerCase(Locale.ROOT) + entityName.substring(1);
        String serviceInterfaceName = tableInfo.getServiceInterfaceName();
        String serviceInterfaceObjName = serviceInterfaceName.substring(1, 2).toLowerCase(Locale.ROOT) +
                serviceInterfaceName.substring(2);
        String serviceTestName = tableInfo.getServiceTestName();
        List<TableFieldInfo> primaryKeyColumnList = tableInfo.getPrimaryKeyColumnList();
        List<TableFieldInfo> tableFieldInfoList = tableInfo.getTableFieldInfoList();

        StringBuilder sBuilder = new StringBuilder();
        // 拼接包名
        sBuilder.append(concatPackageStatement(testCodePkg));
        // 拼接import语句
        List<String> javaTypeFullNameList = new ArrayList<>();
        javaTypeFullNameList.add("java.util.List");
        javaTypeFullNameList.add("org.junit.Test");
        javaTypeFullNameList.add(entityPkg + "." + entityName);
        javaTypeFullNameList.add(serviceInterfacePkg + "." + serviceInterfaceName);
        for (TableFieldInfo tableFieldInfo : tableFieldInfoList) {
            String javaTypeFullName = tableFieldInfo.getJavaTypeFullName();
            if (javaTypeFullNameList.contains(javaTypeFullName)) {
                continue;
            }
            if (javaTypeFullName.startsWith("java.lang.")) {
                continue;
            }
            javaTypeFullNameList.add(javaTypeFullName);
        }
        javaTypeFullNameList.add("static junit.framework.TestCase.assertEquals");
        javaTypeFullNameList.add("static junit.framework.TestCase.assertNotNull");
        javaTypeFullNameList.add("static junit.framework.TestCase.assertTrue");

        for (String javaTypeFullName : javaTypeFullNameList) {
            String importStatement = concatImportStatement(javaTypeFullName);
            sBuilder.append(importStatement).append(NEW_LINE);
        }
        sBuilder.append(NEW_LINE);
        // 拼接接口声明
        sBuilder.append(concatClassComment(author));
        sBuilder.append("public class ").append(serviceTestName).append(" {").append(NEW_LINE).append(NEW_LINE);

        sBuilder.append(FOUR_SPACES).append("@Autowired").append(NEW_LINE)
                .append(FOUR_SPACES).append("private ").append(serviceInterfaceName).append(" ")
                .append(serviceInterfaceObjName).append(";")
                .append(NEW_LINE).append(NEW_LINE);

        // 拼接insertImpl方法
        if (tableInfo.getHasPrimaryKey()) {
            sBuilder.append(concatCreateTestWithPkMethod(tableInfo, entityName, entityObjName, serviceInterfaceObjName));
        }
//        else {
//            // 不支持
//            // sBuilder.append(concatCreateImplMethod(entityName, entityObjName, mapperObjName));
//        }

        // 拼接selectAllTest方法
        sBuilder.append(concatSelectAllTestMethod(entityName, entityObjName, serviceInterfaceObjName));

        // 如果数据表有主键，拼接selectByIdTest、updateTest、deleteTest方法
        if (tableInfo.getHasPrimaryKey()) {
            // 拼接selectByIdTest方法
            sBuilder.append(
                    concatSelectByIdTestMethod(entityName, entityObjName, serviceInterfaceObjName, primaryKeyColumnList));
            // 拼接updateTest方法
            sBuilder.append(concatUpdateTestMethod(tableInfo, entityName, entityObjName, serviceInterfaceObjName));
            // 拼接deleteTest方法
            sBuilder.append(concatDeleteTestMethod(serviceInterfaceObjName, primaryKeyColumnList));
        }
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        sBuilder.append("}").append(NEW_LINE);

        return sBuilder.toString();
    }

    private static String concatDeleteByPrimaryKeyMethod(List<TableFieldInfo> primaryKeyColumnList) {
        String pkStatement = concatPkStatement(primaryKeyColumnList);
        return FOUR_SPACES + "int deleteByPrimaryKey(" + pkStatement  + ");" + NEW_LINE + NEW_LINE;
    }

    private static String concatDeleteTestMethod(String serviceInterfaceObjName,
                                                 List<TableFieldInfo> primaryKeyColumnList) {

        TableFieldInfo tableFieldInfo = primaryKeyColumnList.get(0);
        String jdbcType = tableFieldInfo.getJdbcType();
        String value = generateRandomVale(jdbcType);

        return FOUR_SPACES + "@Test" + NEW_LINE +
                FOUR_SPACES + "public void testDelete() {" + NEW_LINE +
                EIGHT_SPACES + "int i = " + serviceInterfaceObjName + ".delete(" + value + ");" + NEW_LINE +
                EIGHT_SPACES + "assertEquals(1, i);" + NEW_LINE +
                FOUR_SPACES + "}" + NEW_LINE + NEW_LINE;
    }

    private static String concatDeleteImplMethod(String mapperObjName, List<TableFieldInfo> primaryKeyColumnList) {
        String pkStatement = concatPkStatement(primaryKeyColumnList);

        TableFieldInfo tableFieldInfo = primaryKeyColumnList.get(0);
        String propertyName = tableFieldInfo.getPropertyName();
        return FOUR_SPACES + "@Override" + NEW_LINE +
                FOUR_SPACES + "public int delete(" + pkStatement  + ") {" + NEW_LINE +
                EIGHT_SPACES + "return " + mapperObjName + ".deleteByPrimaryKey(" + propertyName + ");" + NEW_LINE +
                FOUR_SPACES + "}" + NEW_LINE + NEW_LINE;
    }

    private static String concatDeleteMethod(List<TableFieldInfo> primaryKeyColumnList) {
        String pkStatement = concatPkStatement(primaryKeyColumnList);
        return FOUR_SPACES + "int delete(" + pkStatement  + ");" + NEW_LINE + NEW_LINE;
    }

    private static String concatUpdateByPrimaryKeyMethod(String entityName, String entityObjName) {
        return FOUR_SPACES + "int updateByPrimaryKey(" + entityName + SPACE + entityObjName + ");" + NEW_LINE + NEW_LINE;
    }

    private static String concatUpdateTestMethod(TableInfo tableInfo, String entityName, String entityObjName,
                                                 String serviceInterfaceObjName) {

        String createInstance = entityName + " " + entityObjName + " = new " + entityName + "();";
        List<TableFieldInfo> tableFieldInfoList = tableInfo.getTableFieldInfoList();

        StringBuilder sBuilder = new StringBuilder();
        for (TableFieldInfo tableFieldInfo : tableFieldInfoList) {
            String propertyName = tableFieldInfo.getPropertyName();
            String setMethod = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);

            sBuilder.append(FOUR_SPACES).append(FOUR_SPACES).append(entityObjName)
                    .append(".").append(setMethod).append("(")
                    .append(generateRandomVale(tableFieldInfo.getJdbcType())).append(");").append(NEW_LINE);
        }

        return FOUR_SPACES + "@Test" + NEW_LINE +
                FOUR_SPACES + "public void testUpdate() {" + NEW_LINE +
                EIGHT_SPACES + createInstance + NEW_LINE +
                sBuilder +
                EIGHT_SPACES + "int i = " + serviceInterfaceObjName + ".update(" + entityObjName + ");" + NEW_LINE +
                EIGHT_SPACES + "assertEquals(1, i);" + NEW_LINE +
                FOUR_SPACES + "}" + NEW_LINE + NEW_LINE;
    }

    private static String concatUpdateImplMethod(String entityName, String entityObjName, String mapperObjName) {
        return FOUR_SPACES + "@Override" + NEW_LINE +
                FOUR_SPACES + "public int update(" + entityName + SPACE + entityObjName + ") {" + NEW_LINE +
                EIGHT_SPACES + "return " + mapperObjName + ".updateByPrimaryKey(" + entityObjName + ");" + NEW_LINE +
                FOUR_SPACES + "}" + NEW_LINE + NEW_LINE;
    }

    private static String concatUpdateMethod(String entityName, String entityObjName) {
        return FOUR_SPACES + "int update(" + entityName + SPACE + entityObjName + ");" + NEW_LINE + NEW_LINE;
    }

    private static String concatSelectByPrimaryKeyMethod(String entityName, List<TableFieldInfo> primaryKeyColumnList) {
        String pkStatement = concatPkStatement(primaryKeyColumnList);
        return FOUR_SPACES + entityName + SPACE + "selectByPrimaryKey(" + pkStatement + ");" + NEW_LINE + NEW_LINE;
    }

    private static String concatSelectByIdTestMethod(String entityName, String entityObjName,
                                                     String serviceInterfaceObjName, List<TableFieldInfo> primaryKeyColumnList) {

        TableFieldInfo tableFieldInfo = primaryKeyColumnList.get(0);
        String value = generateRandomVale(tableFieldInfo.getJdbcType());
        String selectInstance = entityName + " " + entityObjName + " = " +
                serviceInterfaceObjName + ".selectById(" + value + ");";
        return FOUR_SPACES + "@Test" + NEW_LINE +
                FOUR_SPACES + "public void testSelectById() {" + NEW_LINE +
                EIGHT_SPACES + selectInstance + NEW_LINE +
                EIGHT_SPACES + "assertNotNull(" + entityObjName + ");" + NEW_LINE +
                FOUR_SPACES + "}" + NEW_LINE + NEW_LINE;
    }

    private static String concatSelectByIdImplMethod(String entityName, String mapperObjName,
                                                     List<TableFieldInfo> primaryKeyColumnList) {

        TableFieldInfo tableFieldInfo = primaryKeyColumnList.get(0);
        String propertyName = tableFieldInfo.getPropertyName();
        String pkStatement = concatPkStatement(primaryKeyColumnList);

        return FOUR_SPACES + "@Override" + NEW_LINE +
                FOUR_SPACES + "public " + entityName + SPACE + "selectById(" + pkStatement + ") {" + NEW_LINE +
                EIGHT_SPACES + "return " + mapperObjName + ".selectByPrimaryKey(" + propertyName + ");" + NEW_LINE +
                FOUR_SPACES + "}" + NEW_LINE + NEW_LINE;
    }

    private static String concatSelectByIdMethod(String entityName, List<TableFieldInfo> primaryKeyColumnList) {
        String pkStatement = concatPkStatement(primaryKeyColumnList);
        return FOUR_SPACES + entityName + SPACE + "selectById(" + pkStatement + ");" + NEW_LINE + NEW_LINE;
    }

    private static String concatPkStatement(List<TableFieldInfo> primaryKeyColumnList) {
        StringBuilder sBuilder = new StringBuilder();
        for (TableFieldInfo tableFieldInfo : primaryKeyColumnList) {
            sBuilder.append(tableFieldInfo.getJavaType()).append(SPACE)
                    .append(tableFieldInfo.getPropertyName()).append(",");
        }
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }

    private static String concatCreateImplWithPkMethod(String entityName, String entityObjName, String mapperObjName,
                                                       List<TableFieldInfo> primaryKeyColumnList) {

        TableFieldInfo tableFieldInfo = primaryKeyColumnList.get(0);
        String propertyName = tableFieldInfo.getPropertyName();
        String getMethod = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        String javaType = tableFieldInfo.getJavaType();

        return FOUR_SPACES + "@Override" + NEW_LINE +
                FOUR_SPACES + "public " + javaType+" create(" + entityName + SPACE + entityObjName + ") {" + NEW_LINE +
                EIGHT_SPACES + mapperObjName + ".insert(" + entityObjName + ");" + NEW_LINE +
                EIGHT_SPACES + "return " + entityObjName + "." + getMethod + "();" + NEW_LINE +
                FOUR_SPACES + "}" + NEW_LINE + NEW_LINE;
    }

    private static String concatCreateImplMethod(String entityName, String entityObjName, String mapperObjName) {
        return FOUR_SPACES + "@Override" + NEW_LINE +
                FOUR_SPACES + "public int create(" + entityName + SPACE + entityObjName + ") {" + NEW_LINE +
                EIGHT_SPACES + mapperObjName + ".insert(" + entityObjName + ");" + NEW_LINE +
                EIGHT_SPACES + "return 1;" + NEW_LINE +
                FOUR_SPACES + "}" + NEW_LINE + NEW_LINE;
    }

    private static String concatCreateWithPkMethod(String entityName, String entityObjName,
                                                   List<TableFieldInfo> primaryKeyColumnList) {

        TableFieldInfo tableFieldInfo = primaryKeyColumnList.get(0);
        String javaType = tableFieldInfo.getJavaType();
        return FOUR_SPACES + javaType + " create(" + entityName + SPACE + entityObjName + ");" + NEW_LINE + NEW_LINE;
    }

    private static String concatCreateMethod(String entityName, String entityObjName) {
        return FOUR_SPACES + "int create(" + entityName + SPACE + entityObjName + ");" + NEW_LINE + NEW_LINE;
    }

    private static String concatInsertMethod(String entityName, String entityObjName) {
        return FOUR_SPACES + "int insert(" + entityName + SPACE + entityObjName + ");" + NEW_LINE + NEW_LINE;
    }

    private static String generateRandomVale(String jdbcType) {
        String value;
        switch (jdbcType) {
            case "BIGINT":
                value = "1L";
                break;
            case "INTEGER":
                value = "1";
                break;
            case "VARCHAR":
                value = "\"a\"";
                break;
            case "TIMESTAMP":
                value = "new Date()";
                break;
            case "DOUBLE":
                value = "1.0";
                break;
            case "OTHER":
                value = "{}";
                break;
            default:
                return "";
        }
        return value;
    }

    private static String concatCreateTestWithPkMethod(TableInfo tableInfo, String entityName,
                                                       String entityObjName, String serviceInterfaceObjName) {

        String createInstance = entityName + " " + entityObjName + " = new " + entityName + "();";
        List<TableFieldInfo> primaryKeyColumnList = tableInfo.getPrimaryKeyColumnList();
        List<TableFieldInfo> tableFieldInfoList = tableInfo.getTableFieldInfoList();

        StringBuilder sBuilder = new StringBuilder();
        for (TableFieldInfo tableFieldInfo : tableFieldInfoList) {
            String propertyName = tableFieldInfo.getPropertyName();
            String setMethod = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);

            sBuilder.append(FOUR_SPACES).append(FOUR_SPACES).append(entityObjName)
                    .append(".").append(setMethod).append("(")
                    .append(generateRandomVale(tableFieldInfo.getJdbcType())).append(");").append(NEW_LINE);
        }

        TableFieldInfo tableFieldInfo = primaryKeyColumnList.get(0);
        String javaType = tableFieldInfo.getJavaType();

        return FOUR_SPACES + "@Test" + NEW_LINE +
                FOUR_SPACES + "public void testInsert() {" + NEW_LINE +
                EIGHT_SPACES + createInstance + NEW_LINE +
                sBuilder +
                EIGHT_SPACES + javaType + " id = " + serviceInterfaceObjName + ".create(" + entityObjName + ");" + NEW_LINE +
                EIGHT_SPACES + "assertTrue(id > 0);" + NEW_LINE +
                FOUR_SPACES + "}" + NEW_LINE + NEW_LINE;
    }

    private static String concatSelectAllTestMethod(String entityName, String entityObjName,
                                                    String serviceInterfaceObjName) {
        String resultList = entityObjName + "List";
        return FOUR_SPACES + "@Test" + NEW_LINE +
                FOUR_SPACES + "public void testSelectAll() {" + NEW_LINE +
                EIGHT_SPACES + "List<" + entityName + "> " + resultList + " = " +
                serviceInterfaceObjName + ".selectAll();" + NEW_LINE +
                EIGHT_SPACES + "assertTrue(" + resultList + " != null && " + resultList + ".size() > 0);" + NEW_LINE +
                FOUR_SPACES + "}" + NEW_LINE + NEW_LINE;
    }

    private static String concatSelectAllImplMethod(String entityName, String mapperObjName) {
        return FOUR_SPACES + "@Override" + NEW_LINE +
                FOUR_SPACES + "public List<" + entityName + "> " + "selectAll() {" + NEW_LINE +
                EIGHT_SPACES + "return " + mapperObjName + ".selectList();" + NEW_LINE +
                FOUR_SPACES + "}" + NEW_LINE + NEW_LINE;
    }

    private static String concatSelectAllMethod(String entityName) {
        return FOUR_SPACES + "List<" + entityName + "> " + "selectAll();" + NEW_LINE + NEW_LINE;
    }

    private static String concatSelectListMethod(String entityName) {
        return FOUR_SPACES + "List<" + entityName + "> " + "selectList();" + NEW_LINE + NEW_LINE;
    }

    private static String concatSetterMethod(String javaType, String propertyName) {
        String methodName = "set" + propertyName.substring(0, 1).toUpperCase(Locale.ROOT) + propertyName.substring(1);
        return FOUR_SPACES + "public void " + methodName + "(" + javaType + SPACE + propertyName + ") {" + NEW_LINE +
                EIGHT_SPACES + "this." + propertyName + " = " + propertyName + SEMICOLON + NEW_LINE +
                FOUR_SPACES + "}" + NEW_LINE;
    }

    private static String concatGetterMethod(String javaType, String propertyName) {
        String methodName = "get" + propertyName.substring(0, 1).toUpperCase(Locale.ROOT) + propertyName.substring(1);
        return FOUR_SPACES + "public " + javaType + SPACE + methodName + "() {" + NEW_LINE +
                EIGHT_SPACES + " return " + propertyName + SEMICOLON + NEW_LINE +
                FOUR_SPACES + "}" + NEW_LINE;
    }

    private static String concatFieldStatement(String javaType, String propertyName) {
        return FOUR_SPACES + "private " + javaType + SPACE + propertyName + SEMICOLON;
    }

    private static String concatClassComment(String author) {
        return "/**" + NEW_LINE
                + " * @author " + author + NEW_LINE
                + " * @date " + DateUtil.getCurrentDate() + NEW_LINE
                + " */" + NEW_LINE;
    }

    private static String concatClassComment(String author, String comment) {
        return "/**" + NEW_LINE
                + " * @author " + author + NEW_LINE
                + " * @date " + DateUtil.getCurrentDate() + NEW_LINE
                + " *" + NEW_LINE
                + " * " + comment + NEW_LINE
                + " */" + NEW_LINE;
    }

    private static String concatImportStatement(String className) {
        return "import " + className + SEMICOLON;
    }

    private static String concatPackageStatement(String packageName) {
        return "package " + packageName + SEMICOLON + NEW_LINE + NEW_LINE;
    }
}
