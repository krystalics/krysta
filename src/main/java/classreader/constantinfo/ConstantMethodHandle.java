package classreader.constantinfo;

import classreader.ConstantInfo;
import classreader.basictype.U1;
import classreader.basictype.U2;

import java.io.InputStream;

/**
 * Created by wangxiandeng on 2017/1/25.
 */
public class ConstantMethodHandle extends ConstantInfo {
    public short referenceKind;
    public int referenceIndex;


    public void read(InputStream in) {
        referenceKind = U1.read(in);
        referenceIndex = U2.read(in);
    }
}
