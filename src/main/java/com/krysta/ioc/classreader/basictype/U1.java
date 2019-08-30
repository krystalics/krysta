package com.krysta.ioc.classreader.basictype;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wanginbeijing on 2017/1/24.
 */
public class U1 {
    /*
    * unsigned one byte，transfer by short
    * this method is around the readDataInputStream.readUnsignedByte
    * */
    public static short read(InputStream in) {
        byte[] bytes = new byte[1];

        try {
            in.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (short) (bytes[0] & 0xFF);
    }
}
