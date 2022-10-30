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





# 