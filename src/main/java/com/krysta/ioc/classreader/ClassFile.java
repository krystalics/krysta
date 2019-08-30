package com.krysta.ioc.classreader;

import com.krysta.ioc.classreader.basicinfo.MemberInfo;

/**
 * Created by wanginbeijing on 2017/1/24.
 */
public class ClassFile {
    /*
     * Class file
     * */
    public long magic;
    public int minorVersion;
    public int majorVersion;
    public int constantPoolCount;
    public ConstantPool constantPool;
    public int accessFlag;
    public String className;
    public String superClass;
    public int interfaceCount;
    public String[] interfaces;
    public int fieldCount;
    public MemberInfo[] fields;
    public int methodCount;
    public MemberInfo[] methods;
}
