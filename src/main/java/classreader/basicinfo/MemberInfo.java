package classreader.basicinfo;

import classreader.ConstantPool;
import classreader.basicinfo.attribute.AttributeInfo;
import classreader.basictype.U2;

import java.io.InputStream;

/**
 * Created by wangxiandeng on 2017/1/25.
 */
public class MemberInfo extends BasicInfo{
    /*
    * MemberInfo 实际上包含了以下三种类型的数据
    * CONSTANT_Fieldref,CONSTANT_Methodref,CONSTANT_InterfaceMethodref
    * 它们的数据类型基本一致，所以放到一起
    * */

    public int accessFlags; //访问标志
    public int nameIndex; //方法名索引
    public int descriptorIndex; //方法描述符
    public int attributesCount;
    public AttributeInfo[] attributes; //属性数组，里面的Code属性存储JVm指令

    public MemberInfo(ConstantPool cp){
        super(cp);
    }
    public void read(InputStream in) {
        accessFlags= U2.read(in);
        nameIndex= U2.read(in);
        descriptorIndex= U2.read(in);
        attributesCount= U2.read(in);
        for (int i = 0; i < attributesCount; i++) {
            AttributeInfo attributeInfo=AttributeInfo.getAttribute(mCp,in);
            attributeInfo.read(in);
            attributes[i]=attributeInfo;
        }
    }
}
