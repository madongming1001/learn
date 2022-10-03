package com.madm.learnroute.technology.io.nio;

import cn.hutool.core.io.FileUtil;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static java.nio.file.StandardOpenOption.READ;

/**
 * 零拷贝：CPU不需要将数据从一个存储区域复制到另一个存储区域，从而可以减少上下文切换以及CPU的拷贝时间。它是一种I/O操作优化技术 mmap sendfile slice
 * kafka producer使用Mmap+顺序写入的方式写入到kafka，达到快速写入的目的
 * consumer和broker通过sendfile直接发送数据给消费者
 * netty
 */
public class MMapTest {

    public static void main(String[] args) {
        learnFileChannel();
    }

    private static void learnFileChannel() {
        try (RandomAccessFile file = new RandomAccessFile("niodata.text", "rw"); FileChannel inChannel = file.getChannel()) {
            //分配缓冲区
            //此方法将数据从FileChannel读入缓冲区。 read()方法返回的int指示缓冲区中有多少个字节。 如果返回-1，则到达文件结尾。
            ByteBuffer buf = ByteBuffer.allocate(1024);
            int bytesRead = inChannel.read(buf);

            System.out.println(bytesRead + "=====" + new String(buf.array()));
            String newData = "New String to write to file..." + System.currentTimeMillis();
            buf.clear();
            buf.put(newData.getBytes());
            buf.flip();
            while (buf.hasRemaining()) {
                inChannel.write(buf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void learnFileChannel2() {
        try (FileChannel readChannel = FileChannel.open(Paths.get("./jay.txt"), READ); FileChannel writeChannel = FileChannel.open(Paths.get("./siting.txt"), StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
//            readChannel.transferFrom();使用的是sendfile技术 2.1 2.6.17新出的slice
//            readChannel.transferTo();使用的是sendfile技术
            MappedByteBuffer data = readChannel.map(FileChannel.MapMode.READ_ONLY, 0, 1024 * 1024 * 40);
            //数据传输
            writeChannel.write(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
