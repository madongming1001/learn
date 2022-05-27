package com.madm.learnroute.javaee;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.concurrent.CopyOnWriteArrayList;

public class DirectMemoryTest {

    public static void heapAccess() {
        long startTime = System.currentTimeMillis();
        //分配堆内存
        ByteBuffer buffer = ByteBuffer.allocate(1000);
        for (int i = 0; i < 100000; i++) {
            for (int j = 0; j < 200; j++) {
                buffer.putInt(j);
            }
            buffer.flip();
            for (int j = 0; j < 200; j++) {
                buffer.getInt();
            }
            buffer.clear();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("堆内存访问:" + (endTime - startTime) + "ms");
    }

    public static void directAccess() {
        long startTime = System.currentTimeMillis();
        //分配直接内存
        ByteBuffer buffer = ByteBuffer.allocateDirect(1000);
        for (int i = 0; i < 100000; i++) {
            for (int j = 0; j < 200; j++) {
                buffer.putInt(j);
            }
            buffer.flip();
            for (int j = 0; j < 200; j++) {
                buffer.getInt();
            }
            buffer.clear();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("直接内存访问:" + (endTime - startTime) + "ms");
    }

    public static void heapAllocate() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            ByteBuffer.allocate(100);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("堆内存申请:" + (endTime - startTime) + "ms");
    }

    public static void directAllocate() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            ByteBuffer.allocateDirect(100);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("直接内存申请:" + (endTime - startTime) + "ms");
    }

    public static void nettyDirectAllocatePitocin() {
        ByteBuf directBuffer = Unpooled.directBuffer(10);
        int writeAscii = ByteBufUtil.writeAscii(directBuffer, "79");
//        if (directBuffer.hasArray()) {
//            byte[] array = directBuffer.array();
//            int offset = directBuffer.arrayOffset() + directBuffer.readerIndex();
//            int length = directBuffer.readableBytes();
//            //0,0
//            log.info("offset:{},length:{}", offset, length);
//        }
    }

    public static void byteBufferPractice() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        System.err.println("startPosition: " + byteBuffer.position() + ",limit: " + byteBuffer.limit() +
                ",capacity: " + byteBuffer.capacity());
        byteBuffer.put("abc".getBytes());
        System.err.println("writePosition: " + byteBuffer.position() + ",limit: " + byteBuffer.limit() +
                ",capacity: " + byteBuffer.capacity());
        byteBuffer.flip();
        System.err.println("readPosition: " + byteBuffer.position() + ",limit: " + byteBuffer.limit() +
                ",capacity: " + byteBuffer.capacity());
    }

    public static void main(String args[]) {
//        for (int i = 0; i < 10; i++) {
//            heapAccess();
//            directAccess();
//        }
//
//        System.out.println();
//
//        for (int i = 0; i < 10; i++) {
//            heapAllocate();
//            directAllocate();
//        }
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("ma");
//        byteBufferPractice();
    }

    public static int Fibonacci(int n) {
        if (n < 0) {
            return 0;
        }
        return (n - 2) + (n - 1);
    }

}
