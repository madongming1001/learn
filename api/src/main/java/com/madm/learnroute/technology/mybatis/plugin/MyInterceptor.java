package com.madm.learnroute.technology.mybatis.plugin;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;

import java.util.Properties;

/**
 * @author dongming.ma
 * @date 2022/6/12 19:35
 */
@Intercepts({@Signature(type = Executor.class,  //确定要拦截的对象
        method = "update",        //确定要拦截的方法
        args = {MappedStatement.class, Object.class}   //拦截方法的参数
)})
@Slf4j
public class MyInterceptor implements Interceptor {

    private long time;


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //通过StatementHandler获取执行的sql
        //如果当前代理的是一个非代理对象，那么就会调用真实拦截对象的方法
        //如果不是它就会调用下个插件代理对象的invoke方法
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();

        long start = System.currentTimeMillis();
        Object proceed = invocation.proceed();
        long end = System.currentTimeMillis();
        if ((end - start) > time) {
            log.info("本次数据库操作是慢查询，sql是:" + sql);
        }
        return proceed;
    }

    //获取到拦截的对象，底层也是通过代理实现的，实际上是拿到一个目标代理对象
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    //获取设置的阈值等参数
    @Override
    public void setProperties(Properties properties) {
        this.time = Long.parseLong(properties.getProperty("time"));
    }
}