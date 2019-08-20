package classreader.basictype;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wanginbeijing on 2017/1/24.
 */
public class U2 {
    /*
     * 只读两个字节的无符号整数，可以通过 int 转换
     * 相当于 DataInputStream.readUnsignedShort()
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
            num <<= 8; //num向左移动8位再和一个字节相或，相当于将该字节装进了num中
            num |= (aByte & 0xFF);
        }
        // 最终在num中是16位表示数值的无符号整数，
        return num;
    }
}
