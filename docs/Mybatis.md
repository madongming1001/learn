# 存储过程

​		存储过程（Stored Procedure）是在大型数据库系统中，**一组为了完成特定功能的SQL 语句集**，存储在数据库中，经过第一次编译后再次调用不需要再次编译，用户通过指定存储过程的名字并给出参数（如果该存储过程带有参数）来调用存储过程。

**参考文章:**https://www.cnblogs.com/xiaoxi/p/6606011.html



![image-20220408164728464](noteImg/image-20220408164728464.png)


####mybatisPlus实现真实批量插入操作 三种方式
https://blog.csdn.net/qq_21223653/article/details/121171293

1)数据库连接配置文件添加 rewriteBatchedStatements=true
2)直接采用手写sql的方式 mapper.xml foreach
3)引入mybats-plus-extends包 配置insertBatchSomeColumn类

## Sqlsession四大关键组件

![image-20220612171455120](/Users/madongming/notes/noteImg/image-20220612171455120.png)

- Executor（执行器）[Executor类图](#executor类图)

- StatementHandler[（数据库会话器）](#数据库会话器)
- ParameterHandler [（参数处理器）](#参数处理器)
- ResultSetHandler[（结果处理器）](#结果处理器)

### <a style='color:black' name="executor类图">Executor类图</a> 

![image-20220612171913627](/Users/madongming/notes/noteImg/image-20220612171913627.png)

### <a style='color:black' name="数据库会话器">StatementHandler类图</a>

![image-20220612172032243](/Users/madongming/notes/noteImg/image-20220612172032243.png)

### <a style='color:black' name="参数处理器">ParameterHandler类图</a>

![image-20220612172122646](/Users/madongming/notes/noteImg/image-20220612172122646.png)

### <a style='color:black' name="结果处理器">ResultSetHandler类图</a>

![image-20220612172200239](/Users/madongming/notes/noteImg/image-20220612172200239.png)



### mybatis执行流程

![图片](https://mmbiz.qpic.cn/mmbiz_png/z40lCFUAHpkQ2HfT6YibhB7gDLsT2Grl8Nn9oXiaUQeVXicr09koMpclCXzic3aAkqYkVLQvGdyPlKfWc249mn032g/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

![图片](https://mmbiz.qpic.cn/mmbiz_png/z40lCFUAHpkQ2HfT6YibhB7gDLsT2Grl8HsgdSUgaG6xIGmvOWmKHCPWeibtNI3uSQ3VCm8WaCpk4icMPk5CpGicuQ/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

1. 读取 MyBatis 配置文件——mybatis-config.xml 、加载映射文件——映射文件即 SQL 映射文件，文件中配置了操作数据库的 SQL 语句。最后生成一个配置对象。

2. 构造会话工厂：通过 MyBatis 的环境等配置信息构建会话工厂 SqlSessionFactory。

3. 创建会话对象：由会话工厂创建 SqlSession 对象，该对象中包含了执行 SQL 语句的所有方法。

4. Executor 执行器：MyBatis 底层定义了一个 Executor 接口来操作数据库，它将根据 SqlSession 传递的参数动态地生成需要执行的 SQL 语句，同时负责查询缓存的维护。

5. StatementHandler：数据库会话器，串联起参数映射的处理和运行结果映射的处理。

6. 参数处理：对输入参数的类型进行处理，并预编译。

7. 结果处理：对返回结果的类型进行处理，根据对象映射规则，返回相应的对象。

   

### mybatis功能架构

![图片](https://mmbiz.qpic.cn/mmbiz_png/z40lCFUAHpkQ2HfT6YibhB7gDLsT2Grl8iaEHDTuHlcEnrpAgtOeAJDLGoqLbCXmBrgfCknCibvPvLqiahsuT8f1RA/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)



### mybatis获取mapper

![图片](https://mmbiz.qpic.cn/mmbiz_png/z40lCFUAHpkQ2HfT6YibhB7gDLsT2Grl8pmLjuxMZIV6BiadLmLpFQ6DO8FEsu3KBDeW5wvHNs20Bd2gj6bdgW3Q/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

### Mybatis都有哪些执行器？

![图片](https://mmbiz.qpic.cn/mmbiz_png/z40lCFUAHpkQ2HfT6YibhB7gDLsT2Grl88exP2AvLUpPYPrqnXrCeBjPOPaw3ElNicsWia11rt7jDad6O8SjuWMmA/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

- **SimpleExecutor**：每执行一次update或select，就开启一个Statement对象，用完立刻关闭Statement对象。
- **ReuseExecutor**：执行update或select，以sql作为key查找Statement对象，存在就使用，不存在就创建，用完后，不关闭Statement对象，而是放置于Map<String, Statement>内，供下一次使用。简言之，就是重复使用Statement对象。
- **BatchExecutor**：执行update（没有select，JDBC批处理不支持select），将所有sql都添加到批处理中（addBatch()），等待统一执行（executeBatch()），它缓存了多个Statement对象，每个Statement对象都是addBatch()完毕后，等待逐一执行executeBatch()批处理。与JDBC批处理相同。
- 在Mybatis配置文件中，在设置（settings）可以指定默认的ExecutorType执行器类型，也可以手动给DefaultSqlSessionFactory的创建SqlSession的方法传递ExecutorType类型参数，如`SqlSession openSession(ExecutorType execType)`。

# SqlSessionFactory and SqlSession

SqlSessionFactory（资源对象）是MyBatis（线程安全的）的关键对象，它是个单个[数据库](https://cloud.tencent.com/solution/database?from=10680)映射关系经过编译后的内存镜像。**SqlSessionFactory就是生产SqlSession对象的工厂。那也就是说整个Mybatis中，如果只有一个数据库Server要连接，那么只需要一个工厂就够了（只有一个SqlSessionFactory的实例对象）**

SqlSession**（线程独有）**它是应用程序与持久层之间交互操作的一个单线程对象,类似于JDBC中的Connection。SqlSession对象完全包含以数据库为背景的所有执行SQL操作的方法，它的底层封装了JDBC连接，可以用SqlSession实例来直接执行被映射的SQL语句。**SqlSession表示的是数据库客户端和数据库服务端之间的一种会话，并维护了两者之间的状态信息。里面有许多操作数据库的方法。**

**SqlSessionFactoryBean是生产SqlSessionFactory的一种工厂bean。**

**SqlSessionFactory是打开SqlSession会话的工厂，是一个接口，可以根据需求自己实现，它的默认实现类DefaultSqlSessionFactory使用了数据库连接池技术。**

**参考文章：**https://segmentfault.com/a/1190000021523973

# 面试官：Mybatis中 Dao接口和XML文件的SQL如何建立关联？

参考文章：https://mp.weixin.qq.com/s/FqwtyVhkUq7xJL7xCV-N6w



# 面试官：MyBatis 插件有什么用途？说说底层原理？我竟然不会。

参考文章：https://mp.weixin.qq.com/s/acddVQSo2exXd0yij8wFiQ

# mybatis的一级缓存失效 

**会现在自己的threadlocal里面找sqlsession 如果没有创建一个新的 创建完成之后在设置到threadloca变量里面，如果没有开启事务就会一级缓存失效。**

```java
@Override
public <T> T selectOne(String statement, Object parameter) {
  return this.sqlSessionProxy.selectOne(statement, parameter);
}
```

```java
public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType,
    PersistenceExceptionTranslator exceptionTranslator) {

  notNull(sqlSessionFactory, "Property 'sqlSessionFactory' is required");
  notNull(executorType, "Property 'executorType' is required");

  this.sqlSessionFactory = sqlSessionFactory;
  this.executorType = executorType;
  this.exceptionTranslator = exceptionTranslator;
  this.sqlSessionProxy = (SqlSession) newProxyInstance(
      SqlSessionFactory.class.getClassLoader(),
      new Class[] { SqlSession.class },
      new SqlSessionInterceptor());
}
```

![image-20220115165113769](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20220115165113769.png)

![image-20220115165125624](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20220115165125624.png)



![image-20220115165225216](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20220115165225216.png)



**mybatis和mybatisplus的区别？**

 MyBatis和MyBatis Plus是两个不同的开源框架，它们在Java持久层框架中有一些区别： 1. 功能扩展：MyBatis Plus是在MyBatis的基础上进行了功能扩展和增强。它提供了更加方便、简洁的API，以及更多的内置功能，例如通用CRUD操作、分页、逻辑删除、代码生成等，可以减少开发人员的工作量。 2. 代码生成：MyBatis Plus提供了代码生成器（Generator）工具，可以根据数据库表结构自动生成实体类、Mapper接口、XML映射文件等代码，极大地提高了开发效率。 3. 注解支持：MyBatis Plus对注解的支持更加全面。它提供了丰富的注解，如@TableName、@TableId、@TableField等，可以简化实体类与数据库表的映射关系配置。 4. 分页支持：MyBatis Plus内置了强大的分页插件，可以方便地进行分页查询操作，无需手动编写复杂的SQL语句。 5. Lambda表达式：MyBatis Plus引入了Lambda表达式的支持，可以使用Lambda表达式替代传统的XML或者注解方式，更加直观地编写查询条件。 6. SQL优化：MyBatis Plus在底层做了很多SQL的优化工作，例如SQL预编译、动态SQL等，提高了数据库访问的性能。 需要注意的是，虽然MyBatis Plus在功能和扩展性上有所增强，但它仍然保留了MyBatis的核心思想和灵活性。因此，对于一些特殊需求，仍然可以使用原生的MyBatis进行扩展和定制。



**jpa和mybatis的区别？**

Spring JPA和MyBatis是两种不同的持久化框架，它们在实现上有很大的区别。 

**1. 编程风格**：Spring JPA采用了面向对象的编程风格，使用JPA规范进行数据访问。而MyBatis则采用了基于SQL的编程风格，通过XML文件或注解来定义SQL语句。

 **2. 对象关系映射**：Spring JPA通过实体类和注解的方式来实现对象关系映射（ORM），开发者无需编写SQL语句，JPA会自动将实体类映射到数据库表。而MyBatis需要开发者手动编写SQL语句，并通过映射文件或注解来定义SQL语句与实体类的关系。

 **3. 数据库支持**：Spring JPA支持多种数据库，包括MySQL、Oracle、PostgreSQL等，可以通过简单的配置切换数据库。而MyBatis对各种数据库的支持更加灵活，可以根据需要编写特定数据库的SQL语句。

 **4. 性能**：由于Spring JPA是基于ORM**(Object Relation Mapping 对象关系映射，用于在关系型数据库和业务实体对象之间作一个映射 )**的，它的性能相对较低。而MyBatis是基于SQL的，直接操作数据库，所以性能较高。

 **5. 维护成本**：Spring JPA可以通过继承**JpaRepository**等接口来实现常见的CRUD操作，减少了编码工作量，但灵活性较差。而MyBatis需要开发者手动编写SQL语句，维护成本相对较高，但可以更加灵活地控制SQL语句的执行。 总体来说，如果你希望简化开发流程，减少编码量，同时对性能要求不是很高，可以选择Spring JPA；如果你需要更高的性能和灵活性，愿意花费一些时间编写和维护SQL语句，可以选择MyBatis。选择哪种框架取决于你的项目需求和个人偏好。

#### MyBatis-Plus

为了更高的效率，出现了MyBatis-Plus这类工具，对MyBatis进行增强。

1. **考虑到MyBatis是半自动化ORM**，MyBatis-Plus 启动即会自动注入基本 CURD，性能基本无损耗，直接面向对象操作; 并且内置通用 Mapper、通用 Service，仅仅通过少量配置即可实现单表大部分 CRUD 操作，更有强大的条件构造器，满足各类使用需求；总体上让其支持全自动化的使用方式（本质上借鉴了Hibernate思路）。
2. **考虑到Java8 Lambda（函数式编程）开始流行**，MyBatis-Plus支持 Lambda 表达式，方便的编写各类查询条件，无需再担心字段写错
3. **考虑到MyBatis还需要独立引入PageHelper分页插件**，MyBatis-Plus支持了内置分页插件，同PageHelper一样基于 MyBatis 物理分页，开发者无需关心具体操作，配置好插件之后，写分页等同于普通 List 查询
4. **考虑到自动化代码生成方式**，MyBatis-Plus也支持了内置代码生成器，采用代码或者 Maven 插件可快速生成 Mapper 、 Model 、 Service 、 Controller 层代码，支持模板引擎，更有超多自定义配置等您来使用
5. **考虑到SQL性能优化等问题**，MyBatis-Plus内置性能分析插件, 可输出 SQL 语句以及其执行时间，建议开发测试时启用该功能，能快速揪出慢查询
6. 其它还有解决一些常见开发问题，比如**支持主键自动生成**，支持4 种主键策略（内含分布式唯一 ID 生成器 - Sequence），可自由配置，完美解决主键问题；以及**内置全局拦截插件**，提供全表 delete 、 update 操作智能分析阻断，也可自定义拦截规则，预防误操作

------



# **mybatis.xml文件中的标签<if></if>叫作ognl表达式**

# **什么是p6spy？**

MyBatis-Plus是一个开源的MyBatis增强工具，它在MyBatis的基础上进行了功能扩展和性能优化，提供了很多方便的功能和操作。

关于MyBatis-Plus的第三方性能分析插件，常用的有以下几种：

1. p6spy：p6spy是一个开源的、可配置的JDBC监控和审计工具，它可以通过代理方式注入到JDBC驱动中，从而截获JDBC执行的SQL语句、参数、执行时间等信息，并可以将这些信息输出到日志文件或其他存储介质中，用于性能分析和问题定位。

2. Mybatis-Plus自带的性能分析插件：MyBatis-Plus提供了自带的性能分析插件，通过配置开启该插件后，可以在控制台或日志中输出SQL执行的相关信息，包括SQL语句、参数、执行时间等，用于性能分析和调优。

3. Alibaba Druid：Alibaba Druid是一个开源的高性能、可扩展的数据库连接池和监控平台，它可以作为数据源连接池使用，并提供了丰富的监控统计信息，包括SQL执行次数、执行时间、慢查询等，用于性能分析和优化。

这些插件都可以帮助开发人员进行SQL性能分析和优化，根据具体需求和场景选择适合的插件使用。

**p6spy是一个Java开源的JDBC监控和审计工具，它可以代理JDBC驱动并截获JDBC执行的SQL语句、参数、执行时间等信息，通过输出到日志文件或其他存储介质中，用于性能分析、SQL调优和问题定位。**p6spy可以对常用的数据库连接池、JDBC驱动进行适配，并提供了灵活的配置方式，方便集成到各种Java应用中。f

# 多租户

saas(software as a service)多租户是指一种软件架构模式，即一个软件系统可以为多个租户提供服务，每个租户都有自己的独立数据和配置，但是这些租户共享同一个应用程序实例。