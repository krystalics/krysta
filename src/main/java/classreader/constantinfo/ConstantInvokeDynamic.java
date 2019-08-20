package classreader.constantinfo;

import classreader.ConstantInfo;
import classreader.basictype.U2;

import java.io.InputStream;

/**
 * Created by wangxiandeng on 2017/1/25.
 */
public class ConstantInvokeDynamic extends ConstantInfo {
    public int bootstrapMethodAttrIndex;
    public int nameAndTypeIndex;

    public void read(InputStream in) {
        bootstrapMethodAttrIndex = U2.read(in);
        nameAndTypeIndex = U2.read(in);
    }
}
