package com.krysta.ioc.classreader.constantinfo;

import com.krysta.ioc.classreader.ConstantInfo;
import com.krysta.ioc.classreader.basictype.U2;

import java.io.InputStream;

/**
 * Created by wangxiandeng on 2017/1/25.
 */
public class ConstantNameAndType extends ConstantInfo {
    public int nameIndex;
    public int descIndex;

    public void read(InputStream in) {
        nameIndex = U2.read(in);
        descIndex = U2.read(in);
    }
}
