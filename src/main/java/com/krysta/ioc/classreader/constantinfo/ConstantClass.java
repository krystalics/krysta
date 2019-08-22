package com.krysta.ioc.classreader.constantinfo;

import com.krysta.ioc.classreader.ConstantInfo;
import com.krysta.ioc.classreader.basictype.U2;

import java.io.InputStream;

/**
 * Created by wangxiandeng on 2017/1/25.
 */
public class ConstantClass extends ConstantInfo {
    /*
    * 这一项存储的是类或者接口的符号引用：
    * CONSTANT_Class_info{
    *   u1 tag;
    *   u2 name_index;
    * }
    * 这里tag已经在ConstantInfo中统一定义了
    * 而每个类型里的细节都在 java虚拟机规范中详细描述了，像ConstantClass里的nameIndex和tag
    * */
    public int nameIndex;

    public void read(InputStream in) {
        nameIndex = U2.read(in);
    }
}
