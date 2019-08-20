package classreader.constantinfo;

import classreader.ConstantInfo;
import classreader.basictype.U4;

import java.io.InputStream;

/**
 * Created by wangxiandeng on 2017/1/25.
 */
public class ConstantLong extends ConstantInfo {
    public long highValue;
    public long lowValue;

    public void read(InputStream in) {
        highValue = U4.read(in);
        lowValue = U4.read(in);
    }
}
