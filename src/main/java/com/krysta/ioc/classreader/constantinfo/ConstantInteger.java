package com.krysta.ioc.classreader.constantinfo;

import com.krysta.ioc.classreader.ConstantInfo;
import com.krysta.ioc.classreader.basictype.U4;

import java.io.InputStream;

/**
 * Created by wanginbeijing on 2017/1/24.
 */
public class ConstantInteger extends ConstantInfo {
    public long value;

    public void read(InputStream in) {
        value= U4.read(in);
    }
}
