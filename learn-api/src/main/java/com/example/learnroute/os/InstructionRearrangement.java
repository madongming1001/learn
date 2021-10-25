package com.example.learnroute.os;

import com.oracle.tools.packager.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InstructionRearrangement {

    private static int x = 0, y = 0;
    private static int a = 0, b = 0;

    public static void main(String[] args) throws InterruptedException {
        int i = 0;
        for (; ; ) {
            i++;
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    shortWait(10000);
                    a = 1;
                    x = b;
                }
            });
            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    b = 1;
                    y = a;
                }
            });
            t1.start();
            t2.start();
            t1.join();
            t2.join();

            String result = "第" + i + "次 (" + x + "," + y + ")";
            if(x == 0 && y == 0){
                System.out.println(result);
                break;
            }else {
                Log.info(result);
            }
        }
    }

    private static void shortWait(int i) {

    }
}
