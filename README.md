# 简介

自动生成mybatis框架的mapper.xml文件、接口、实体类的工具库。现在只支持mysql


# 使用方式

这是我平时在学习过程中用于生成mapper.xml文件的工具库，因为做项目的时候需要写很多xml文件，而且我在网上找的自动化生成工具不符合我的心意，它们生成的代码我还需要再改一下，不能一次到位，所以我写了这个工具库。


我在使用的时候，会把当前项目中的所有源代码都粘贴到目标项目的测试目录下，然后配置当前项目的测试文件 config.properties，在当前项目中提供了一个案例，然后编写配置类，调用MapperGenerator，下面是一个案例

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

如果用户不知道自己数据库的连接信息，还可以在调研MapperGenerator的时候，传入当前项目的dataSource实例，通过它来创建数据库连接。

