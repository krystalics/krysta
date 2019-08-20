package classreader;

import classreader.basictype.U1;
import classreader.constantinfo.ConstantClass;
import classreader.constantinfo.ConstantDouble;
import classreader.constantinfo.ConstantFloat;
import classreader.constantinfo.ConstantInteger;
import classreader.constantinfo.ConstantInvokeDynamic;
import classreader.constantinfo.ConstantLong;
import classreader.constantinfo.ConstantMemberRef;
import classreader.constantinfo.ConstantMethodHandle;
import classreader.constantinfo.ConstantMethodType;
import classreader.constantinfo.ConstantNameAndType;
import classreader.constantinfo.ConstantString;
import classreader.constantinfo.ConstantUtf8;

import java.io.InputStream;

/**
 * Created by wanginbeijing on 2017/1/24.
 */
public abstract class ConstantInfo {
    /*
    * 常量池中每一项的数据通用格式如下
    * cp_info{
    *    u1 tag;
    *    u1 info[];
    * }
    * 下面一系列都是不同数据类型 tag的大小
    * 在ConstantPool中 根据tag值的大小，返回一个ConstantInfo的子类
    * */
    public static final short CONSTANT_Class = 7;
    public static final short CONSTANT_Fieldref = 9;
    public static final short CONSTANT_Methodref = 10;
    public static final short CONSTANT_InterfaceMethodref = 11;
    public static final short CONSTANT_String = 8;
    public static final short CONSTANT_Integer = 3;
    public static final short CONSTANT_Float = 4;
    public static final short CONSTANT_Long = 5;
    public static final short CONSTANT_Double = 6;
    public static final short CONSTANT_NameAndType = 12;
    public static final short CONSTANT_Utf8 = 1;
    public static final short CONSTANT_MethodHandle = 15;
    public static final short CONSTANT_MethodType = 16;
    public static final short CONSTANT_InvokeDynamic = 18;

    public short tag;

    public abstract void read(InputStream in);

    public static ConstantInfo getConstantInfo(short tag) {
        switch (tag) {
            case CONSTANT_Class:
                return new ConstantClass();
            case CONSTANT_Fieldref:
            case CONSTANT_Methodref:
            case CONSTANT_InterfaceMethodref:
                return new ConstantMemberRef();
            case CONSTANT_Long:
                return new ConstantLong();
            case CONSTANT_Double:
                return new ConstantDouble();
            case CONSTANT_String:
                return new ConstantString();
            case CONSTANT_Integer:
                return new ConstantInteger();
            case CONSTANT_Float:
                return new ConstantFloat();
            case CONSTANT_NameAndType:
                return new ConstantNameAndType();
            case CONSTANT_Utf8:
                return new ConstantUtf8();
            case CONSTANT_MethodHandle:
                return new ConstantMethodHandle();
            case CONSTANT_MethodType:
                return new ConstantMethodType();
            case CONSTANT_InvokeDynamic:
                return new ConstantInvokeDynamic();
            //这里是不是有个缺陷？？
        }
        return null;
    }
}
