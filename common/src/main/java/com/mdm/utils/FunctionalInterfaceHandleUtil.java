package com.mdm.utils;

import com.mdm.function.BranchHandle;
import com.mdm.function.PresentOrElseHandler;

public class FunctionalInterfaceHandleUtil {

    /**
     * 参数为true或false时，分别进行不同的操作
     *
     * @param b
     * @return com.example.demo.func.BranchHandle
     **/
    public static BranchHandle isTureOrFalse(boolean b) {
        String string = new String();
        String str = string;
        return (trueHandle, falseHandle) -> {
            if (b) {
                trueHandle.run();
            } else {
                falseHandle.run();
            }
        };
    }

    /**
     * 参数为true或false时，分别进行不同的操作
     *
     * @param str
     * @return com.example.demo.func.BranchHandle
     **/
    public static PresentOrElseHandler<?> isBlankOrNoBlank(String str) {
        return (consumer, runnable) -> {
//            if (StringUtils.isBlank(str)) {
//                consumer.accept(str);
//            } else {
//                runnable.run();
//            }
        };
    }

//    public static void main(String[] args) {
//        FunctionalInterfaceHandleUtil.isBlankOrNoBlank("")
//                .presentOrElseHandle(System.out::println, () -> System.out.println("空字符串"));
//    }
}
