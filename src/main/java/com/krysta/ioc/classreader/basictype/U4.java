package com.krysta.ioc.classreader.basictype;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wanginbeijing on 2017/1/24.
 */
public class U4 {
    /*
     * unsigned 4 bytes Integer，can transfer by long
     * = DataInputStream.readInt()
     * */
    public static long read(InputStream in) {
        byte[] bytes = new byte[4];

        try {
            in.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long num = 0;
        for (byte aByte : bytes) {
            num <<= 8;
            num |= (aByte & 0xFF);
        }
        return num;
    }
}
