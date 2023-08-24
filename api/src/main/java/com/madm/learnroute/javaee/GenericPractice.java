package com.madm.learnroute.javaee;

import cn.hutool.core.util.RandomUtil;
import com.mdm.pojo.User;
import lombok.SneakyThrows;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * generic范型 获取范型信息
 * 1. Class类的泛型方法
 * getGenericSuperclass()
 * Type[] getGenericInterfaces()
 * 2. Field类的泛型方法
 * getGenericType()
 * 3. Method类的泛型方法
 * getGenericReturnType()
 * Type[] getGenericParameterTypes()
 * Type[] getGenericExceptionTypes()
 * 如果父类或者其声明的有泛型 那么返回的就是Type类型的子接口
 * ParameterizedType
 * Type[] getActualTypeArguments()：获取实际类型参数的Type集合
 * Type getRawType()：获取声明此类型的类或接口的Type
 * Type getOwnerType()：如果声明此类型的类或接口为内部类，这返回的是该内部类的外部类的Type（也就是该内部类的拥有者）
 * new Object() {}`表示创建一个继承自`Object`类的匿名内部类的实例对象
 * 参考地址：https://blog.csdn.net/zhangjunli/article/details/131895775
 *
 * @author dongming.ma
 * @date 2022/11/15 14:54
 */
public class GenericPractice<T> extends TestClass<String> implements TestInterface1<Integer>, TestInterface2<Long> ,OutputInterface{

    private List<Integer> list;

    private Map<Integer, String> map;

    private final Type type;

    public List<String> aa() {
        return null;
    }

    public void bb(List<Long> list) {
    }

    public GenericPractice() {
        System.out.println(this.getClass());
        Type superclass = getClass().getGenericSuperclass();
        if (!(superclass instanceof ParameterizedType)) {
            throw new IllegalArgumentException("无泛型类型信息");
        }
        type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }

    public static <T> T get(GenericPractice<T> tTestClass2) throws IllegalAccessException, InstantiationException {
        System.out.println(tTestClass2.getClass());
        Type type = tTestClass2.getType();
        Class clazz = (Class) type;
        return (T) clazz.newInstance();
    }

    public Type getType() {
        return type;
    }


    @SneakyThrows
    public static void main(String[] args) {
        System.out.println(TestInterface1.class);

        OutputInterface oi =  new GenericPractice<String>() {
            public String innerMethod() {
                return "1";
            }
        };
        System.out.println(oi.getType()+ "---------------");
        System.out.println(oi.innerMethod()+ "---------------");


        List<String> list = new ArrayList<>();
        //list.add(123);//编译错误，因为泛型是String类型
        list.getClass().getDeclaredMethod("add", Object.class).invoke(list, 123);
        System.out.println(list);
        //上述代码编译通过，因为反射是运行时行为，且编译时已经擦除成了Object类型。
        //System.out.println(list.get(0) + ""); 发生错误是因为内部get的时候需要转换为异常
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

        System.out.println("======================================= 泛型类声明的泛型类型 =======================================");
        ParameterizedType pt = (ParameterizedType) GenericPractice.class.getGenericSuperclass();
        System.out.println(pt.getTypeName() + "--------->" + pt.getActualTypeArguments()[0].getTypeName());

        Type[] types = GenericPractice.class.getGenericInterfaces();
        for (Type type : types) {
            ParameterizedType typ = (ParameterizedType) type;
            System.out.println(typ.getTypeName() + "--------->" + typ.getActualTypeArguments()[0].getTypeName());
        }

        System.out.println("======================================= 成员变量中的泛型类型 =======================================");
        ParameterizedType parameterizedType1 = (ParameterizedType) GenericPractice.class.getDeclaredField("list").getGenericType();
        System.out.println(parameterizedType1.getTypeName() + "--------->" + parameterizedType1.getActualTypeArguments()[0].getTypeName());

        ParameterizedType parameterizedType2 = (ParameterizedType) GenericPractice.class.getDeclaredField("map").getGenericType();
        System.out.println(parameterizedType2.getTypeName() + "--------->" + parameterizedType2.getActualTypeArguments()[0].getTypeName() + "," + parameterizedType2.getActualTypeArguments()[1].getTypeName());

        System.out.println("======================================= 方法参数中的泛型类型 =======================================");
        ParameterizedType parameterizedType3 = (ParameterizedType) GenericPractice.class.getMethod("aa").getGenericReturnType();
        System.out.println(parameterizedType3.getTypeName() + "--------->" + parameterizedType3.getActualTypeArguments()[0].getTypeName());
        System.out.println(parameterizedType3.getTypeName() + "--------->" + parameterizedType3.getRawType().getTypeName());

        System.out.println("======================================= 方法返回值中的泛型类型 =======================================");
        Type[] types1 = GenericPractice.class.getMethod("bb", List.class).getGenericParameterTypes();
        for (Type type : types1) {
            ParameterizedType typ = (ParameterizedType) type;
            System.out.println(typ.getTypeName() + "--------->" + typ.getActualTypeArguments()[0].getTypeName());
        }
    }

}

class TestClass<T> {

}

interface TestInterface1<T> {

}

interface TestInterface2<T> {

}

abstract class SuperClass<T> {
}

interface ServiceA<T> {
}

interface ServiceB<T> {
}

class ServiceImpl implements ServiceA<EntityA>, ServiceB<EntityB> {

    public static void main(String[] args) {
        Type supperClass = ServiceImpl.class.getGenericSuperclass();
        Type[] supperInterfaces = ServiceImpl.class.getGenericInterfaces();
        //ParameterizedTypeImpl
        if (supperInterfaces.getClass().isAssignableFrom(ParameterizedType[].class)) {
            for (Type genericInterface : supperInterfaces) {
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

    public static Function<Integer, Integer> pow() {
        return x -> (int) Math.pow(x, 10);
    }

}

class EntityA {
}

class EntityB {
}

interface OutputInterface {
    default String innerMethod() {
        return null;
    }

    Type getType();
}