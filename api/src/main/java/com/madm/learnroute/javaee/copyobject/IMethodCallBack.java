package com.madm.learnroute.javaee.copyobject;

/**
 * @author dongming.ma
 * @date 2022/7/6 17:57
 */
public interface IMethodCallBack {

    String getMethodName();

    ToBean callMethod(FromBean frombean) throws Exception;

}