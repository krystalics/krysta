package com.krysta.ioc.classreader.basictype;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wanginbeijing on 2017/1/24.
 */
public class U2 {
    /*
     * unsigned two bytes，can transfer by int
     * = DataInputStream.readUnsignedShort()
     * */
    public static int read(InputStream in) {
        byte[] bytes = new byte[2];

        try {
            in.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int num = 0;
        for (byte aByte : bytes) {
            num <<= 8;
            num |= (aByte & 0xFF);
        }

        return num;
    }
}
