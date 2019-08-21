package classreader;

import classreader.basictype.U2;
import classreader.basictype.U4;
import classreader.constantinfo.ConstantClass;
import classreader.constantinfo.ConstantUtf8;
import jdk.internal.org.objectweb.asm.Type;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krysta on 2019/8/21.
 *
 * @description
 * @since ioc1.0
 */
public class ClassReader {
    public static ClassAnnotation read(String classPath) {
        File file = new File(classPath);
        try {
            FileInputStream in = new FileInputStream(file); //读取路径中class的输入流
            return read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ClassAnnotation read(InputStream in) {
        ClassFile classFile = new ClassFile(); //存储该class的信息
        List<String> annotations = new ArrayList<>();

        classFile.magic = U4.read(in);
        classFile.minorVersion = U2.read(in);
        classFile.majorVersion = U2.read(in);

        //解析常量池，类名以及各个方法名，描述符，annotation，public protected...字符串 final都存在常量池中
        classFile.constantPoolCount = U2.read(in);
        ConstantPool constantPool = new ConstantPool(classFile.constantPoolCount);
        constantPool.read(in);

        int index = -1;
        for (int i = 1; i < classFile.constantPoolCount; i++) {
            ConstantInfo constantInfo = constantPool.cpInfo[i];
            if (constantInfo instanceof ConstantUtf8) {
                String value = ((ConstantUtf8) constantInfo).value;
                if ("RuntimeVisibleAnnotations".equals(value)) { //注解在常量池的标注
                    index = i;
                    break;
                }
            }
        }

        //实际上一连串的UTF8不只是注解，还有类里定义的各个变量的全名，顺序不定
        //包括方法里定义的也在，但是我们只需要知道这段UTF8过后 注解已经扫完了就行，之后的判断不需要管
        if (index != -1) {
            for (int i = index + 1; i < classFile.constantPoolCount; i++) {
                ConstantInfo constantInfo = constantPool.cpInfo[i];
                if (constantInfo instanceof ConstantUtf8) {
                    //这里加异常捕捉却不处理是因为这里很可能越界，因为Type.getType对字符串有一定要求，但是这并不影响我们获得正确的注解
                    try {
                        annotations.add(Type.getType(((ConstantUtf8) constantInfo).value).getClassName());
                    } catch (Exception ignore) {
                    }
                } else {
                    break;
                }
            }
        }

        //获取类名，通过classIndex得到它在常量池中的位置
        classFile.accessFlag = U2.read(in);
        int classIndex = U2.read(in);
        ConstantClass clazz = (ConstantClass) constantPool.cpInfo[classIndex]; //里面是ConstantInfo，而ConstantClass是其子类
        ConstantUtf8 className = (ConstantUtf8) constantPool.cpInfo[clazz.nameIndex];
        classFile.className = className.value;

        return new ClassAnnotation(classFile.className, annotations);
    }
}
