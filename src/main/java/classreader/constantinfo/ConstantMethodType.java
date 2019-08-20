package classreader.constantinfo;

import classreader.ConstantInfo;
import classreader.basictype.U2;

import java.io.InputStream;

/**
 * Created by wangxiandeng on 2017/1/25.
 */
public class ConstantMethodType extends ConstantInfo {
    public int descType;

    public void read(InputStream in) {
        descType= U2.read(in);
    }
}
