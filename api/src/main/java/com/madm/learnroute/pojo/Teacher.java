package com.madm.learnroute.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 筛选的条件是此对象是 否有必要执行finalize()方法。假如对象没有覆盖finalize()方法，或者finalize()方法已经被虚拟机调用 过，那么虚拟机将这两种情况都视为“没有必要执行”。
 * F-Queue队列里面
 */
@Data
@NoArgsConstructor
@ToString
public class Teacher {
    private String name;
    private Integer age;

    @Override
    protected void finalize() throws Throwable {
        System.out.println("finalize()方法执行-------------");
    }
}
