package com.madm.design.strategy.enumlambda;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 用java 8的函数式接口和泛型 优化策略模式（简化写法）
 * 进而优化if else逻辑
 *
 * @className: FunctionStrategy
 * @author: woniu
 * @date: 2023/2/11
 **/
public interface FunctionStrategy<P, T, R> {

    /**
     * 暴露当前策略的钩子
     *
     * @return 判断钩子
     */
    Predicate<P> predicate();

    /**
     * 暴露当前策略的生产逻辑
     *
     * @return 消费逻辑
     */
    Function<T, R> function();
}
