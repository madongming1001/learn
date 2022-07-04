package com.madm.learnroute.javaee;

import cn.hutool.json.JSONUtil;
import lombok.Data;

/**
 * 浅拷贝
 * 1、implements Cloneable 参考 Cloneable 接口的源码注释部分，如果一个类实现了 Cloneable 接口，那么 Object 的 clone 方法将返回该对象的逐个属性（field-by-field）拷贝
 *
 * 深拷贝
 * 1、序列化（serialization）方式
 *      先对对象进行序列化，再进行反序列化，得到一个新的深拷贝的对象
 * 2、二次调用 clone 方式
 *      先调用对象的 clone() 方法对对象的引用类型的属性值，继续调用 clone() 方法进行拷贝
 *
 * @author dongming.ma
 * @date 2022/7/4 20:28
 */
public class ClonePractice {
    public static void main(String[] args) throws CloneNotSupportedException {
        testShallowCopy();
    }

    public static void testShallowCopy() throws CloneNotSupportedException {
        Address address = new Address();
        address.setName("北京天安门");
        CustomerUser customerUser = new CustomerUser();
        customerUser.setAddress(address);
        customerUser.setLastName("李");
        customerUser.setFirstName("雷");
        String[] cars = new String[]{"别克", "路虎"};
        customerUser.setCars(cars);

        //浅拷贝
        CustomerUser customerUserCopy = (CustomerUser) customerUser.clone();

        customerUserCopy.setFirstName("梅梅");
        customerUserCopy.setLastName("韩");
        customerUserCopy.getAddress().setName("北京颐和园");
        customerUserCopy.getCars()[0] = "奥迪";

        System.out.println("customerUser: " + JSONUtil.toJsonStr(customerUser));
        System.out.println("customerUserCopy: " + JSONUtil.toJsonStr(customerUserCopy));
    }
}


@Data
class Address implements Cloneable {
    private String name;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

@Data
class CustomerUser implements Cloneable {
    private String firstName;
    private String lastName;
    private Address address;
    private String[] cars;

    @Override
    public Object clone() throws CloneNotSupportedException {
        CustomerUser customerUserDeepCopy = (CustomerUser) super.clone();
        //二次调用clone方法
//        customerUserDeepCopy.address = (Address) address.clone();
//        customerUserDeepCopy.cars = cars.clone();
        return customerUserDeepCopy;
    }
}