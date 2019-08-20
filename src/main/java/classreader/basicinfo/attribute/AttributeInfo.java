package classreader.basicinfo.attribute;

import classreader.ConstantPool;
import classreader.basicinfo.BasicInfo;
import classreader.basictype.U1;
import classreader.basictype.U2;
import classreader.basictype.U4;
import classreader.constantinfo.ConstantUtf8;

import java.io.InputStream;

/**
 * Created by wangxiandeng on 2017/1/25.
 */
public class AttributeInfo extends BasicInfo {
    public int nameIndex;
    public long length;
    public short[] info;

    public static final String CODE = "Code";

    public AttributeInfo(ConstantPool cp, int nameIndex) {
        super(cp);
        this.nameIndex = nameIndex;
    }

    public void read(InputStream in) {
        length = (int) U4.read(in);
        info = new short[(int) length];
        for (int i = 0; i < length; i++) {
            info[i] = U1.read(in);
        }
    }

    public static AttributeInfo getAttribute(ConstantPool cp, InputStream in) {
        int nameIndex = U2.read(in);
        String name = ((ConstantUtf8) cp.cpInfo[nameIndex]).value;
        if (CODE.equals(name)) { //如果是CODE属性，里面存储的指令集，由另一个类管理
            return new CodeAttribute(cp, nameIndex);
        }
        return new AttributeInfo(cp, nameIndex);
    }
}
