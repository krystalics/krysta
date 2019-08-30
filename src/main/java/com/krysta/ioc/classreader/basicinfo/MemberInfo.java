package com.krysta.ioc.classreader.basicinfo;

import com.krysta.ioc.classreader.ConstantPool;
import com.krysta.ioc.classreader.basicinfo.attribute.AttributeInfo;
import com.krysta.ioc.classreader.basictype.U2;

import java.io.InputStream;

/**
 * Created by wangxiandeng on 2017/1/25.
 */
public class MemberInfo extends BasicInfo{
    /*
    * MemberInfo contains these three
    * CONSTANT_Fieldref,CONSTANT_Methodref,CONSTANT_InterfaceMethodref
    *
    * */

    public int accessFlags;
    public int nameIndex;
    public int descriptorIndex;
    public int attributesCount;
    public AttributeInfo[] attributes; //Code attribute store the Jvm instruct

    public MemberInfo(ConstantPool cp){
        super(cp);
    }

    public void read(InputStream in) {
        accessFlags= U2.read(in);
        nameIndex= U2.read(in);
        descriptorIndex= U2.read(in);
        attributesCount= U2.read(in);
        attributes = new AttributeInfo[attributesCount];
        for (int i = 0; i < attributesCount; i++) {
            AttributeInfo attributeInfo=AttributeInfo.getAttribute(mCp,in);
            attributeInfo.read(in);
            attributes[i]=attributeInfo;
        }
    }
}
