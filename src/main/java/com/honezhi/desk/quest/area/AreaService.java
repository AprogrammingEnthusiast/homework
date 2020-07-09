package com.honezhi.desk.quest.area;

import com.honezhi.desk.quest.constants.AreaConstants;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Service
public class AreaService {

    //prefix-1300000为数组下标code为value
    private static short[]  prefixArray = new short[700000];
    //code 为数组的下标 name 为 value
    private static String[] nameArray   = new String[1000];

    /**
     * 初始化文件内容至内存
     */
    static {
        initFile();
    }

    /**
     * @param mobile 手机号
     * @return 返回的地区，如果未查到，返回null
     */
    public Area getArea(String mobile) {
        int prefix = Integer.parseInt(mobile.substring(0, 7));
        if (prefix < AreaConstants.MIN_PERFIX || prefix > AreaConstants.MAX_PERFIX) {
            return null;
        }
        short code = prefixArray[getPrefixBin(prefix)];
        String name = nameArray[code];
        return new Area(prefix, code, name);
    }

    private static void initFile() {
        File file = new File("D:\\wjWorkHome\\homework\\data\\area.txt");
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert fileInputStream != null;
        FileChannel fileInputChannel = fileInputStream.getChannel();

        ByteBuffer bigBuffer = ByteBuffer.allocate((int) file.length());
        ByteBuffer prefix = ByteBuffer.allocate(7);
        ByteBuffer code = ByteBuffer.allocate(3);
        ByteBuffer name = ByteBuffer.allocate(19);

        byte b;

        try {

            //循环读取
            while (fileInputChannel.read(bigBuffer) > 0) {
                //大buffer反转
                bigBuffer.flip();
                int tableNum = 0;
                for (int i = 0; i < bigBuffer.limit(); i++) {
                    b = bigBuffer.get();
                    //'\n' 识别到换行符
                    if (b == AreaConstants.LINE_BREAK_ASCII) {
                        //说明需要换行，所以prefix,code,name buffer全部翻转->赋值->清空
                        //反转
                        prefix.flip();
                        code.flip();
                        name.flip();
                        //赋值
                        assign(prefix, code, name);
                        //清空
                        prefix.clear();
                        code.clear();
                        name.clear();
                        tableNum = 0;
                        continue;

                    }
                    //'\r' 回车的ascii是13
                    if (b == AreaConstants.ENTER_ASCII) {
                        continue;
                    }
                    //'\t' 的ascii是9
                    if (b == AreaConstants.TABLE_ASCII) {
                        tableNum++;
                        continue;
                    }
                    //prefix
                    if (tableNum == 0) {
                        prefix.put(b);
                    }
                    //单独获取code
                    if (tableNum == 1) {
                        code.put(b);
                    }
                    //要映射name
                    if (tableNum == 2) {
                        name.put(b);
                    }
                }
                bigBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 向内存映射
     * @param prefix
     * @param code
     * @param name      长短不一
     */
    private static void assign(ByteBuffer prefix, ByteBuffer code, ByteBuffer name) {
        int prefixBin = Integer.parseInt(new String(prefix.array()));
        //只存有效字段
        byte[] codes = new byte[code.limit()];
        code.get(codes);
        Short shortCode = new Short(new String(codes));
        prefixArray[getPrefixBin(prefixBin)] = shortCode;
        if (nameArray[shortCode] == null) {
            //只存有效字段
            byte[] array = new byte[name.limit()];
            name.get(array);
            nameArray[shortCode] = new String(array);
        }
    }

    /**
     * 获取prefix下标
     * @param prefix
     * @return
     */
    private static int getPrefixBin(int prefix) {
        return prefix - 1300000;
    }

    /**
     * Getter method for property <tt>prefixArray</tt>.
     *
     * @return property value of prefixArray
     */
    public static short[] getPrefixArray() {
        return prefixArray;
    }

    /**
     * Getter method for property <tt>nameArray</tt>.
     *
     * @return property value of nameArray
     */
    public static String[] getNameArray() {
        return nameArray;
    }
}
