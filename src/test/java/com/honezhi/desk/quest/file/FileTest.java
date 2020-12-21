package com.honezhi.desk.quest.file;

import io.netty.util.CharsetUtil;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import sun.nio.ch.ChannelInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileTest {

    /**
     * FileUtils    objectSize：55M
     * FileUtils    time：203ms
     * Nio          objectSize：55M
     * Nio          time：352ms
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        //FileUtilsTest();
        //NioTest();
        //a();
    }

    /**
     * FileUtils 使用的是FileInputStream 和 InputStreamReader
     *
     * @throws IOException
     */
    public static void FileUtilsTest() throws IOException {
        File file = new File("D:\\project\\homework\\data\\area.txt");
        LineIterator it = FileUtils.lineIterator(file, "UTF-8");
        String[] list = new String[700000];
        int num = 0;

        long begin = System.currentTimeMillis();
        while (it.hasNext()) {
            String line = it.nextLine();
            // do something with line
            list[num] = line;
            num++;
        }
        long end = System.currentTimeMillis();

        LineIterator.closeQuietly(it);

        long objectSize = ObjectSizeCalculator.getObjectSize(list) / 1024 / 1024;
        System.out.println("FileUtils objectSize：" + objectSize + "M");
        System.out.println("FileUtils time：" + (end - begin) + "ms");
    }

    public static void NioTest() throws IOException {
        File file = new File("D:\\project\\homework\\data\\area.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileInputChannel = fileInputStream.getChannel();

        ByteBuffer bigBuffer = ByteBuffer.allocate((int) file.length());
        ByteBuffer lineBuffer = ByteBuffer.allocate(128);

        String[] list = new String[700000];
        int lineNumber = 0;
        byte b;

        long begin = System.currentTimeMillis();
        byte[] tbyte;
        //循环读取
        while (fileInputChannel.read(bigBuffer) > 0) {
            //大buffer反转
            bigBuffer.flip();
            for (int i = 0; i < bigBuffer.limit(); i++) {
                b = bigBuffer.get();
                lineBuffer.put(b);
                if (b == 10) {
                    //小buffer反转
                    lineBuffer.flip();
                    //直接使用sortBuffer有无效字段，-1是为了去掉回车
                    tbyte = new byte[lineBuffer.limit() - 1];
                    lineBuffer.get(tbyte);
                    list[lineNumber] = new String(tbyte, CharsetUtil.UTF_8);
                    //清理小buffer
                    lineBuffer.clear();
                    // 行数++
                    lineNumber++;
                }
            }
            bigBuffer.clear();
        }
        long end = System.currentTimeMillis();

        fileInputStream.close();

        long objectSize = ObjectSizeCalculator.getObjectSize(list) / 1024 / 1024;
        System.out.println("Nio objectSize：" + objectSize + "M");
        System.out.println("Nio time：" + (end - begin) + "ms");

    }

    public static void a() throws IOException {
        ChannelInputStream fileInputChannel = (ChannelInputStream) Files.newInputStream(Paths.get("D:\\project\\homework\\data\\area.txt"), StandardOpenOption.READ);
        int size = fileInputChannel.available();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            int read = fileInputChannel.read();
        }
        long end = System.currentTimeMillis();
        System.out.println("Nio time：" + (end - begin) + "ms");
    }
}
