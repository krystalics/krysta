package com.krysta.ioc.classreader.constantinfo;

import com.krysta.ioc.classreader.ConstantInfo;
import com.krysta.ioc.classreader.basictype.U2;

import java.io.InputStream;

/**
 * Created by wangxiandeng on 2017/1/25.
 */
public class ConstantClass extends ConstantInfo {
    /*
    *
    * CONSTANT_Class_info{
    *   u1 tag;
    *   u2 name_index;
    * }
    * tag is in the ConstantInfo
    * */
    public int nameIndex;

    public void read(InputStream in) {
        nameIndex = U2.read(in);
    }
}
