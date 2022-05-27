package com.madm.learnroute.javaee;

import sun.misc.Service;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @Author MaDongMing
 * @Date 2022/3/31 3:13 PM
 */
public interface SPIService {
    void execute();
}

class SpiImpl1 implements SPIService {
    public void execute() {
        System.out.println("SpiImpl1.execute()");
    }
}

class SpiImpl2 implements SPIService {
    public void execute() {
        System.out.println("SpiImpl2.execute()");
    }
}

class SPIPractice{
    public static void main(String[] args) {
        Iterator<SPIService> providers = Service.providers(SPIService.class);
        ServiceLoader<SPIService> load = ServiceLoader.load(SPIService.class);

        while(providers.hasNext()) {
            SPIService ser = providers.next();
            ser.execute();
        }
        System.out.println("--------------------------------");
        Iterator<SPIService> iterator = load.iterator();
        while(iterator.hasNext()) {
            SPIService ser = iterator.next();
            ser.execute();
        }
    }
}