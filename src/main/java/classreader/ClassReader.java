package classreader;

import classreader.basicinfo.MemberInfo;
import classreader.basicinfo.attribute.CodeAttribute;
import classreader.basictype.U2;
import classreader.basictype.U4;
import classreader.constantinfo.ConstantClass;
import classreader.constantinfo.ConstantUtf8;
import com.sun.org.apache.bcel.internal.classfile.Code;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by wanginbeijing on 2017/1/24.
 */
public class ClassReader {
    public static void read(String classPath) {
        File file = new File(classPath);
        try {
            FileInputStream in = new FileInputStream(file); //读取路径中class的输入流
            ClassFile classFile = new ClassFile(); //存储该class的信息

            classFile.magic = U4.read(in);
            classFile.minorVersion = U2.read(in);
            classFile.majorVersion = U2.read(in);

            //解析常量池
            classFile.constantPoolCount = U2.read(in);
            ConstantPool constantPool = new ConstantPool(classFile.constantPoolCount);
            constantPool.read(in);

            //获取类信息
            classFile.accessFlag = U2.read(in);
            int classIndex = U2.read(in);
            ConstantClass clazz = (ConstantClass) constantPool.cpInfo[classIndex]; //里面是ConstantInfo，而ConstantClass是其子类
            ConstantUtf8 className = (ConstantUtf8) constantPool.cpInfo[clazz.nameIndex];
            classFile.className = className.value;

            //获取父类信息
            int superIndex = U2.read(in);
            ConstantClass superClazz = (ConstantClass) constantPool.cpInfo[superIndex]; //里面是ConstantInfo，而ConstantClass是其子类
            ConstantUtf8 superClassName = (ConstantUtf8) constantPool.cpInfo[superClazz.nameIndex];
            classFile.superClass = superClassName.value;

            //获取接口信息
            classFile.interfaceCount = U2.read(in);
            classFile.interfaces = new String[classFile.interfaceCount];
            for (int i = 0; i < classFile.interfaceCount; i++) {
                int interfaceIndex = U2.read(in);
                ConstantClass interfaceClazz = (ConstantClass) constantPool.cpInfo[interfaceIndex]; //里面是ConstantInfo，而ConstantClass是其子类
                ConstantUtf8 interfaceName = (ConstantUtf8) constantPool.cpInfo[interfaceClazz.nameIndex];
                classFile.interfaces[i] = interfaceName.value;
            }

            //获取字段信息 Field
            classFile.fieldCount = U2.read(in);
            classFile.fields = new MemberInfo[classFile.fieldCount];
            for (int i = 0; i < classFile.fieldCount; i++) {
                MemberInfo fieldInfo = new MemberInfo(constantPool);
                fieldInfo.read(in);
            }

            //获取方法信息
            classFile.methodCount = U2.read(in);
            classFile.methods = new MemberInfo[classFile.methodCount];
            for (int i = 0; i < classFile.methodCount; i++) {
                MemberInfo methodInfo = new MemberInfo(constantPool);
                methodInfo.read(in);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
