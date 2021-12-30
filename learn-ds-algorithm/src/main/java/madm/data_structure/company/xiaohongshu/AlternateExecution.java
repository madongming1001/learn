package madm.data_structure.company.xiaohongshu;

import java.util.HashMap;
import java.util.Map;

public class AlternateExecution {
    static volatile int maxNum = 100;
    public static void main(String[] args) {

        Map<Object, Object> map = new HashMap<>();
        for(int i = 0;i <= 100; i++){
            new Thread(()->{
                map.put(Thread.currentThread().getName(),1);
            },String.valueOf(i)).start();
        }
        System.out.println(map.size());

//        Thread threadA = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(maxNum > 0 && maxNum % 2 != 0){
//                    System.out.println(maxNum--);
//                }
//            }
//        },"thread A");
//        Thread threadB = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(maxNum > 0 && maxNum % 2 == 0){
//                    System.out.println(maxNum--);
//                }
//            }
//        },"thread B");
//        threadA.start();
//        threadB.start();
    }
}
