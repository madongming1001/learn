package com.madm.learnroute.jvm.juc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SimpleDateFormatNotSafe {
    static SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
    static String testdata[] = {"01-Jan-1999", "14-Feb-2001", "31-Dec-2007"};

    public static void main(String[] args) {
        Runnable r[] = new Runnable[testdata.length];
        for (int i = 0; i < r.length; i++) {
            final int i2 = i;
            r[i] = new Runnable() {
                public void run() {
                    try {
                        for (int j = 0; j < 1000; j++) {
                            String str = testdata[i2];
                            String str2 = null;
                            /* synchronized(df) */
                            {
                                Date d = sdf.parse(str);
                                str2 = sdf.format(d);
                                System.out.println("i: " + i2 + "\tj: " + j + "\tThreadID: "
                                        + Thread.currentThread().getId() + "\tThreadName: "
                                        + Thread.currentThread().getName() + "\t" + str + "\t" + str2);
                            }
                            if (!str.equals(str2)) {
                                throw new RuntimeException("date conversion failed after " + j
                                        + " iterations. Expected " + str + " but got " + str2);
                            }
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException("parse failed");
                    }
                }
            };
            new Thread(r[i]).start();
        }
    }
}
