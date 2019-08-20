package classreader.constantinfo;

import classreader.ConstantInfo;
import classreader.basictype.U4;

import java.io.InputStream;

public class ConstantFloat extends ConstantInfo {
    public long value;

    /*
    * 用long来表示4个字节的无符号整数
    * */
    public void read(InputStream in) {
        value= U4.read(in);
    }
}
