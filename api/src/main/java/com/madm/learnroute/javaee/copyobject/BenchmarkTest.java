package com.madm.learnroute.javaee.copyobject;

/**
 * @author dongming.ma
 * @date 2022/7/6 17:57
 */
public class BenchmarkTest {
    private int count;

    public BenchmarkTest(int count) {
        this.count = count;
        System.out.println("性能测试" + this.count + "==================");
    }

    public void benchmark(IMethodCallBack m, FromBean frombean) {
        try {
            long begin = new java.util.Date().getTime();
            ToBean tobean = null;
            System.out.println(m.getMethodName() + "开始进行测试");
            for (int i = 0; i < count; i++) {

                tobean = m.callMethod(frombean);

            }
            long end = new java.util.Date().getTime();
            System.out.println(m.getMethodName() + " 耗时: " + (end - begin) + System.lineSeparator());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
