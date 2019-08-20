package classreader.basicinfo.attribute;

import classreader.basictype.U2;

import java.io.InputStream;

/**
 * Created by wangxiandeng on 2017/1/26.
 */
public class ExceptionTable {
    public int startPc;
    public int endPc;
    public int handlerPc;
    public int catchType;

    public void read(InputStream in) {
        startPc = U2.read(in);
        endPc = U2.read(in);
        handlerPc = U2.read(in);
        catchType = U2.read(in);
    }
}
