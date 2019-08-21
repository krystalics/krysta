package classreader.constantinfo;

import classreader.ConstantInfo;
import classreader.basictype.U2;

import java.io.InputStream;

/**
 * Created by wangxiandeng on 2017/1/25.
 */
public class ConstantMemberRef extends ConstantInfo {
    public int classIndex;
    public int nameAndTypeIndex;

    public void read(InputStream in) {
        classIndex= U2.read(in);
        nameAndTypeIndex=U2.read(in);
    }
}
