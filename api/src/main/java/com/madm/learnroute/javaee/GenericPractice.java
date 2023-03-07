package com.madm.learnroute.javaee;

import lombok.SneakyThrows;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * generic范型 获取范型信息
 *
 * @author dongming.ma
 * @date 2022/11/15 14:54
 */
public class GenericPractice {
    @SneakyThrows
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        //list.add(123);//编译错误，因为泛型是String类型
        list.getClass()
                .getDeclaredMethod("add", Object.class)
                .invoke(list, 123);
        System.out.println(list);
        //上述代码编译通过，因为反射是运行时行为，且编译时已经擦除成了Object类型。
        System.out.println(list.get(0));
        //上述代码运行时错误,java.lang.Integer cannot be cast to java.lang.String
        /**
         但需要注意的是用list.get(0)时会报类型转换异常，因为list集合被声明为<String>泛型，所以进行获取时会按照String进行强制转型，
         但是加进去的是Integer类型，所以Integer转String会失败（因为Java泛型是伪泛型，详看Java泛型原理）
         */

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