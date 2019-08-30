package com.krysta.ioc.classreader;

import com.krysta.ioc.classreader.basictype.U2;
import com.krysta.ioc.classreader.basictype.U4;
import com.krysta.ioc.classreader.constantinfo.ConstantClass;
import com.krysta.ioc.classreader.constantinfo.ConstantUtf8;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krysta on 2019/8/21.
 *
 * @since ioc1.0
 */
public class ClassReader {
    public static ClassAnnotation read(String classPath) {
        File file = new File(classPath);
        try {
            FileInputStream in = new FileInputStream(file);
            return read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ClassAnnotation read(InputStream in) {
        ClassFile classFile = new ClassFile();
        List<String> annotations = new ArrayList<>();

        classFile.magic = U4.read(in);
        classFile.minorVersion = U2.read(in);
        classFile.majorVersion = U2.read(in);

        classFile.constantPoolCount = U2.read(in);
        ConstantPool constantPool = new ConstantPool(classFile.constantPoolCount);
        constantPool.read(in);

        int index = -1;
        for (int i = 1; i < classFile.constantPoolCount; i++) {
            ConstantInfo constantInfo = constantPool.cpInfo[i];
            if (constantInfo instanceof ConstantUtf8) {
                String value = ((ConstantUtf8) constantInfo).value;
                if ("RuntimeVisibleAnnotations".equals(value)) { //annotation‘s life cycle in constantPool's mark
                    index = i;
                    break;
                }
            }
        }

        //after these UTF8 ,annotation is also over there
        if (index != -1) {
            for (int i = index + 1; i < classFile.constantPoolCount; i++) {
                ConstantInfo constantInfo = constantPool.cpInfo[i];
                if (constantInfo instanceof ConstantUtf8) {
                    //it will happen exception but not effected the analyse
                    try {
                        annotations.add(Type.getType(((ConstantUtf8) constantInfo).value).getClassName());
                    } catch (Exception ignore) {
                    }
                } else {
                    break;
                }
            }
        }


        classFile.accessFlag = U2.read(in);
        int classIndex = U2.read(in);
        ConstantClass clazz = (ConstantClass) constantPool.cpInfo[classIndex];
        ConstantUtf8 className = (ConstantUtf8) constantPool.cpInfo[clazz.nameIndex];
        classFile.className = className.value;

        return new ClassAnnotation(classFile.className, annotations);
    }
}
