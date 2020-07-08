package com.honezhi.desk.quest.file;

import io.netty.util.CharsetUtil;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

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
        NioTest();
    }

    /**
     * FileUtils 使用的是FileInputStream 和 InputStreamReader
     * @throws IOException
     */
    public static void FileUtilsTest() throws IOException {
        File file = new File("C:\\Users\\Administrator.FYYX-2019MKBIOI\\Desktop\\学习\\homework\\data\\area.txt");
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
        File file = new File("C:\\Users\\Administrator.FYYX-2019MKBIOI\\Desktop\\学习\\homework\\data\\area.txt");
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
}
