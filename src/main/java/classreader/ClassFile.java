package classreader;

import classreader.basicinfo.MemberInfo;

/**
 * Created by wanginbeijing on 2017/1/24.
 */
public class ClassFile {
    /*
     * 这里存储Class文件的二进制结构
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
