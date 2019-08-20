package classreader;

import classreader.basictype.U1;

import java.io.InputStream;

/**
 * Created by wanginbeijing on 2017/1/24.
 */
public class ConstantPool {
    public int constant_pool_count; //常量池容量
    public ConstantInfo[] cpInfo; //cpInfo[]下标从1开始，0无效，且真正的常量池大小为constant_pool_count-1。

    public ConstantPool(int count) {
        this.constant_pool_count = count;
        cpInfo = new ConstantInfo[constant_pool_count];
    }

    public void read(InputStream in) {
        for (int i = 1; i < constant_pool_count; i++) {
            short tag = U1.read(in);
            ConstantInfo constantInfo = ConstantInfo.getConstantInfo(tag);
            constantInfo.read(in);
            cpInfo[i] = constantInfo;
            if (tag == ConstantInfo.CONSTANT_Double || tag == ConstantInfo.CONSTANT_Long) {
                i++;
            }
        }
    }

}
