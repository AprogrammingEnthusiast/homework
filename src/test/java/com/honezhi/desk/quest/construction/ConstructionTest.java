package com.honezhi.desk.quest.construction;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;

import java.nio.ByteBuffer;
import java.util.Random;

public class ConstructionTest {

    private static byte[][] bytes = new byte[699000][];

    //prefix为数组下标code为value
    public static int[] prefixArray = new int[700000];
    //code 为数组的下标
    public static String[] nameArray= new String[600];

    static {
        int bin = 0;
        for (int a = 1300000; a < 1999000; a++) {
            //控制最多产生500个随机数
            int random = new Random().nextInt(500) + 100;
            String msg = a + "\t" + random + "\t" + "测试地区." + random;
            bytes[bin] = msg.getBytes();
            bin ++;
        }
    }

    public static void main(String[] args) {

        construction();
        long prefix = ObjectSizeCalculator.getObjectSize(prefixArray) / 1024 / 1024;
        System.out.println("prefixArray：" + prefix + "M");
        long name = ObjectSizeCalculator.getObjectSize(nameArray) / 1024;
        System.out.println("nameArray：" + name + "kb");

    }

    public static void construction(){
        int length = bytes.length;
        //每一行
        ByteBuffer lineBuffer;
        //prefix+Code
        ByteBuffer prefixCode;
        ByteBuffer code;
        ByteBuffer name;
        for (int i = 0; i < length; i++) {
            lineBuffer = ByteBuffer.wrap(bytes[i]);
            prefixCode = ByteBuffer.allocate(7);
            code = ByteBuffer.allocate(3);
            name = ByteBuffer.allocate(16);
            int tableNum = 0;

            for (int j = 0; j < lineBuffer.limit(); j++) {
                byte b = lineBuffer.get();
                //'\t' 的ascii是9
                if(b == 9){
                    tableNum++;
                    continue;
                }

                //prefix+code
                if(tableNum == 0) {
                    prefixCode.put(b);
                }
                //单独获取code
                if(tableNum == 1){
                    code.put(b);
                }
                //要映射name
                if(tableNum == 2) {
                    name.put(b);
                }
            }
            //反转
            lineBuffer.flip();
            prefixCode.flip();
            code.flip();
            name.flip();
            //给其赋值
            int prefix = Integer.parseInt(new String(prefixCode.array()));
            Integer c = new Integer(new String(code.array()));
            prefixArray[getPrefixBin(prefix)] = c;
            if(nameArray[c] == null){
                nameArray[c] = new String(name.array());
            }
            //清理
            lineBuffer.clear();
            prefixCode.clear();
            code.clear();
            name.clear();

        }
    }

    public static int getPrefixBin(int prefix){
        return prefix - 1300000;
    }
}
