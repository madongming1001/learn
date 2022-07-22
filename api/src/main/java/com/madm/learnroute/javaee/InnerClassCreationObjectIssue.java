package com.madm.learnroute.javaee;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author dongming.ma
 * @date 2022/7/22 23:33
 */
public class InnerClassCreationObjectIssue {
}

@Data
class TestA {
    private Integer a;
}

class Test {
    HashMap<String, Object> map = new HashMap<>();
    {
        map.put("random", new Random());
        map.put("this", this);
        System.out.println("对象创建");
    }
    public Integer value1 = 1;
    private ThreadLocal<Map<String, Object>> tl = new ThreadLocal<>();
    {
        value1 = 2;
        tl.set(map);
    }

    public static void main(String[] args) {
        Test test = new Test();
        TestA testA = new TestA() {{
            setA(((Test) test.tl.get().get("this")).value1);
        }};

        System.out.println(testA);
    }
}

