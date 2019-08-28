package com.krysta.ioc;

import com.krysta.ioc.classreader.ClassAnnotation;
import com.krysta.ioc.classreader.ClassReader;
import com.krysta.ioc.util.AnnotationUtil;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Krysta on 2019/8/20.
 *
 * @description
 * @since ioc1.0
 */
public class ClassScanner {

    public static Set<Class<?>> scan(String[] packages) {
        Set<Class<?>> classes = new HashSet<>();
        for (String aPackage : packages) {
            classes.addAll(getClazzFromPkg(aPackage));
        }
        return classes;
    }

    /*
     * 使用url 同时扫描file和jar，
     * */
    public static Set<Class<?>> getClazzFromPkg(String pkg) {
        Set<Class<?>> classes = new LinkedHashSet<>(); //按照添加顺序排序

        String pkgDirName = pkg.replace('.', '/'); //将包名中的.转为/ 变为目录
        try {
            // 这里用ClassScanner 的类加载器扫描这个包，这个类加载器如果没有自己写的话，一般最终会是有JVM提供的 AppClassLoader，
            Enumeration<URL> urls = ClassScanner.class.getClassLoader().getResources(pkgDirName);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8"); //获取包名的物理路径
                    findClassesByFile(pkg, filePath, classes);
                } else if ("jar".equals(protocol)) {
                    JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    findClassesByJar(pkg, jar, classes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

    /*
     *  扫描包路径下的所有class文件
     *
     * @param pkgName 包名
     * @param pkgPath 包对应的绝对地址
     * @param classes 保存包路径下class的集合
     * */
    private static void findClassesByFile(String pkgName, String pkgPath, Set<Class<?>> classes) {
        File dir = new File(pkgPath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        // 过滤器为 directory或者class文件才可以通过
        File[] dirfiles = dir.listFiles(pathname -> pathname.isDirectory() || pathname.getName().endsWith("class"));
        if (dirfiles == null || dirfiles.length == 0) {
            return;
        }

        for (File file : dirfiles) {
            if (file.isDirectory()) {
                findClassesByFile(pkgName + "." + file.getName(), pkgPath + "/" + file.getName(), classes);
                continue;
            }

            ClassAnnotation classAnnotation = ClassReader.read(file.getPath());

            Class<?> aClass = loadClass(classAnnotation);

            if (aClass != null) {
                classes.add(aClass);
            }
        }

    }

    private static void findClassesByJar(String pkgName, JarFile jarFile, Set<Class<?>> classes) {
        String pkgDir = pkgName.replace(".", "/");
        Enumeration<JarEntry> entry = jarFile.entries();

        JarEntry jarEntry;
        String name = "";

        while (entry.hasMoreElements()) {
            jarEntry = entry.nextElement();
            name = jarEntry.getName();
            if (name.charAt(0) == '/') {
                name = name.substring(1);
            }

            if (jarEntry.isDirectory() || !name.startsWith(pkgDir) || !name.endsWith(".class")) {
                continue;
            }

            try {
                ClassAnnotation classAnnotation = ClassReader.read(jarFile.getInputStream(jarEntry));
                Class<?> aClass = loadClass(classAnnotation);
                if (aClass != null) {
                    classes.add(aClass);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    /*
     * 使用当前线程的ClassLoader来加载该类，
     * class.forName()前者除了将类的.class文件加载到jvm中之外，还会对类进行解释，执行类中的static 变量赋值，static方法等。
     * 而classLoader只干一件事情，就是将.class文件加载到jvm中，不会执行static中的内容,只有在newInstance才会去执行static块。
     * 亲测，的确如此
     * */

    public static Class<?> loadClass(ClassAnnotation classAnnotation) {
        if (classAnnotation == null) {
            return null;
        }

        String fullClassName = classAnnotation.getClassName().replace('/', '.');
        for (String annotation : classAnnotation.getAnnotations()) {
            for (String containerAnnotation : AnnotationUtil.annotationNames) {
                if (containerAnnotation.equals(annotation)) {
                    try {
                        return Thread.currentThread().getContextClassLoader().loadClass(fullClassName);
//            return Class.forName(fullClassName);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
