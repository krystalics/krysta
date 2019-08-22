package com.krysta.ioc.classreader.constantinfo;

import com.krysta.ioc.classreader.ConstantInfo;
import com.krysta.ioc.classreader.basictype.U2;

import java.io.IOException;
import java.io.InputStream;
import java.io.UTFDataFormatException;

/**
 * Created by wangxiandeng on 2017/1/25.
 */
public class ConstantUtf8 extends ConstantInfo {
    public String value;

    public void read(InputStream in) {
        int length = U2.read(in);
        byte[] bytes = new byte[length];

        try {
            in.read(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            value = readUtf8(bytes);
        } catch (UTFDataFormatException e) {
            e.printStackTrace();
        }
    }

    /*
    * 以下部分 copy 自 java.io.DataInputStream.readUTF()
    * */
    private String readUtf8(byte[] bytes) throws UTFDataFormatException {
        int c, char2, char3;
        int count = 0;
        int chars_count = 0;
        char[] chars = new char[bytes.length];

        //将bytes转为chars
        while (count < bytes.length) {
            c = (int) bytes[count] & 0xFF;
            if (c > 127) break;
            count++;
            chars[chars_count++] = (char) c;
        }

        //将bytes中大于127（即不能转换成char的部分）转为int之后 右移4位继续转为char
        while (count < bytes.length) {
            c = (int) bytes[count] & 0xFF;
            switch (c >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    /*0xxxxxxx*/
                    count++;
                    chars[chars_count++] = (char) c;
                    break;
                case 12:
                case 13:
                    /*110x xxxx  10xx xxxx*/
                    count += 2;
                    if (count > bytes.length) {
                        throw new UTFDataFormatException("malformed input: partial character at end");
                    }
                    char2 = (int) bytes[count - 1];
                    if ((char2 & 0xC0) != 0x80) {
                        throw new UTFDataFormatException(
                                "malformed input around byte " + count);
                    }
                    chars[chars_count++] = (char) (((c & 0x1F) << 6) | (char2 & 0x3F));
                    break;
                case 14:
                    /* 1110 xxxx  10xx xxxx  10xx xxxx */
                    count += 3;
                    if (count > bytes.length)
                        throw new UTFDataFormatException(
                                "malformed input: partial character at end");
                    char2 = (int) bytes[count - 2];
                    char3 = (int) bytes[count - 1];
                    if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
                        throw new UTFDataFormatException(
                                "malformed input around byte " + (count - 1));
                    chars[chars_count++] = (char) (((c & 0x0F) << 12) |
                            ((char2 & 0x3F) << 6) |
                            ((char3 & 0x3F) << 0));
                    break;
                default:
                    /* 10xx xxxx,  1111 xxxx */
                    throw new UTFDataFormatException(
                            "malformed input around byte " + count);
            }
        }
        // 字符数可能小于 utf的长度
        return new String(chars, 0, chars_count);
    }
}
