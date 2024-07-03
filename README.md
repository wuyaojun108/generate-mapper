# 简介

自动生成mybatis框架的mapper.xml文件的工具库，可以生成mapper.xml、mapper接口、实体类、service类、测试类。现在只支持mysql数据库


这是我平时在学习过程中用于生成mapper.xml文件的工具库，因为做项目的时候需要写很多xml文件，而且我在网上找的自动化生成工具不符合我的要求，它们生成的代码我还需要再改一下，不能一次到位，所以我写了这个工具库。


# 使用方式

第一步：把当前项目中src/main/java目录下的所有源代码都粘贴到目标项目的测试目录下，随后要使用这些源代码


第二步：编写配置文件 config.properties，把它放在test目录下的resources目录下，这里有配置项讲解

```properties
# 配置数据库连接。如果项目中不是这么配的，可以不用写这四项配置
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/demo?\
  useUnicode=true&useSSL=false&characterEncoding=UTF-8&serverTimeZone=UTC&allowPublicKeyRetrieval=true
jdbc.username=root
jdbc.password=123456

# 表名:实体名:mapper接口名;...  
# 这里就是配置要生成哪张表的代码，以及表名对应的实体类名称和mapper接口名称，service类的名称和测试类
# 的名称会根据mapper接口名称自动计算。多张表可以用分号隔开。
jdbc.tables=demo1:demo1DO:Demo1Mapper

# 数据库类型，现在只支持mysql
jdbc.dbType=mysql

# 配置代码存放位置

# 代码存放在哪个文件夹下，这里选择测试目录
code.dir=src/test/java
# 实体类的存放位置，这以下几项都要写包名
entity.pkg=org.wyj.entity
# mapper接口的存放位置
mapper.interface.pkg=org.wyj.mapper
# service接口的存放位置
service.interface.pkg=org.wyj.service

# mapper.xml文件存放在哪个文件夹下
resource.dir=src/test/resources
# mapper.xml文件在上一项配置下的路径，这里相当于放在resources目录下的mapper目录中
mapper.path=mapper

# 测试类的存放位置
testCode.dir=src/test/java
testCode.pkg=org.wyj.test

# 代码中的注释信息，在生成的类和接口中，会拼接上作者信息
code.author=wuyaojun
```

第三步：编写测试类，调用工具库，生成mapper.xml。测试类有两种写法：

- 第一种：普通的测试类，需要提供jdbc.driver等配置项

```java
import org.junit.Test;
import org.wyj.MapperGenerator;

public class MapperGeneratorTest {

    @Test
    public void test2() {
        MapperGenerator.execute();
    }
}
```
- 第二种：如果不知道项目中数据库的连接信息，无法提供jdbc.driver等参数，可以选择写一个springboot的测试类，将数据库连接池的实例注入到测试类中

```java
@SpringbootTest
public class MapperGeneratorTest {
    
    @Autowired
    private DataSource dataSource;

    @Test
    public void test2() {
        MapperGenerator.execute(dataSource);
    }
}
```

# 使用案例

为了演示项目效果，在这里提供一个使用案例，配置文件、测试类、生成的代码，都在“案例1”目录下。


这是数据表：

```sql
CREATE TABLE `demo1` (
  `id` int NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  `a_tinyint` tinyint DEFAULT NULL COMMENT '注释1',
  `a_smallint` smallint DEFAULT NULL COMMENT '注释2',
  `a_mediumint` mediumint DEFAULT NULL,
  `a_int` int DEFAULT NULL,
  `a_integer` int DEFAULT NULL,
  `a_bigint` bigint DEFAULT NULL,
  `a_boolean` tinyint(1) DEFAULT NULL,
  `a_bit` bit(1) DEFAULT NULL,
  `a_float` float(10,2) DEFAULT NULL,
  `a_double` double(15,2) DEFAULT NULL,
  `a_decimal` decimal(10,5) DEFAULT NULL,
  `a_char` char(10) DEFAULT NULL,
  `a_varchar` varchar(50) DEFAULT NULL,
  `a_tinytext` tinytext,
  `a_text` text,
  `a_mediumtext` mediumtext,
  `a_long_text` longtext,
  `a_date` date DEFAULT NULL,
  `a_time` time DEFAULT NULL,
  `a_datetime` datetime DEFAULT NULL,
  `a_timestamp` timestamp NULL DEFAULT NULL,
  `a_year` year DEFAULT NULL,
  `a_blob` blob
);
```

