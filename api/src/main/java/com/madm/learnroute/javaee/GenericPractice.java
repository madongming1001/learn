package com.madm.learnroute.javaee;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * generic范型 获取范型信息
 * @author dongming.ma
 * @date 2022/11/15 14:54
 */
public class GenericPractice {
    public static void main(String[] args) {
        // 获取 Main 的超类 SuperClass 的签名(携带泛型). 这里为: xxx.xxx.xxx.SuperClass<xxx.xxx.xxx.User>
        Type genericSuperclass = GenericPractice.class.getGenericSuperclass();
        // 强转成 参数化类型 实体.
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        System.out.println(parameterizedType);
        // 获取超类的泛型类型数组. 即SuperClass<User>的<>中的内容, 因为泛型可以有多个, 所以用数组表示
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Type genericType = actualTypeArguments[0];
        Class<User> clazz = (Class<User>) genericType;
        System.out.println(clazz);
    }
}


abstract class SuperClass<T> {
}

class User {
}

interface ServiceA<T> {
}

interface ServiceB<T> {
}

class ServiceImpl implements ServiceA<EntityA>, ServiceB<EntityB> {

    public static void main(String[] args) {
        Type[] genericInterfaces = ServiceImpl.class.getGenericInterfaces();
        if (genericInterfaces.getClass().isAssignableFrom(ParameterizedType[].class)) {
            for (Type genericInterface : genericInterfaces) {
                ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                Type type = actualTypeArguments[0];
                if (type instanceof Class) {
                    Class<?> clazz = (Class<?>) type;
                    System.out.println(clazz);
                }
            }
        }
    }

}

class EntityA {
}

class EntityB {
}