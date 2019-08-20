package classreader.basicinfo.attribute;

import classreader.ConstantPool;
import classreader.basictype.U1;
import classreader.basictype.U2;
import classreader.basictype.U4;

import java.io.InputStream;

/**
 * Created by wangxiandeng on 2017/1/25.
 */
public class CodeAttribute extends AttributeInfo {
    public int maxStack; //最大虚拟机栈数量
    public int maxLocals; //最大本地机栈数量
    public long codeLength; //指令的字节长度，即code数组的长度
    public short[] code; //
    public int exceptionTableLength;
    public ExceptionTable[] exceptionTables;
    public int attributes_count;
    public AttributeInfo[] attributes;

    public CodeAttribute(ConstantPool cp, int nameIndex) {
        super(cp, nameIndex);
    }

    @Override
    public void read(InputStream in) {
        length = (int) U4.read(in);

        maxStack = U2.read(in);
        maxLocals = U2.read(in);
        codeLength = (int) U4.read(in);
        code = new short[(int) codeLength];
        for (int i = 0; i < codeLength; i++) {
            code[i] = U1.read(in);
        }
        exceptionTableLength = U2.read(in);
        exceptionTables = new ExceptionTable[exceptionTableLength];
        for (int i = 0; i < exceptionTableLength; i++) {
            ExceptionTable table = new ExceptionTable();
            table.read(in);
            exceptionTables[i] = table;
        }

        attributes_count = U2.read(in);
        attributes = new AttributeInfo[attributes_count];
        for (int i = 0; i < attributes_count; i++) {
            AttributeInfo attributeInfo = AttributeInfo.getAttribute(mCp, in);
            attributeInfo.read(in);
            attributes[i] = attributeInfo;
        }

    }
}
