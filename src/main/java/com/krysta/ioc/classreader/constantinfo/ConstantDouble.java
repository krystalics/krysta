package com.krysta.ioc.classreader.constantinfo;

import com.krysta.ioc.classreader.ConstantInfo;
import com.krysta.ioc.classreader.basictype.U4;

import java.io.InputStream;

/**
 * Created by wangxiandeng on 2017/1/25.
 */
public class ConstantDouble extends ConstantInfo {
    public long highValue;
    public long lowValue;
    /*
    * Double are 8 bytes
    * */
    public void read(InputStream in) {
        highValue = U4.read(in);
        lowValue = U4.read(in);
    }
}
