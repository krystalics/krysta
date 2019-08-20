package classreader.basictype;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wanginbeijing on 2017/1/24.
 */
public class U1 {
    /*
    * 只读一个字节的无符号整数，可以通过 short转换
    * 相当于 DataInputStream.readUnsignedByte
    * */
    public static short read(InputStream in) {
        byte[] bytes = new byte[1];

        try {
            in.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //如果不做&运算，转为short时 最高位为1就会被转为负数。因为short是有符号的
        return (short) (bytes[0] & 0xFF);
    }
}
