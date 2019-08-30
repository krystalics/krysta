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

    /**
     * use url to scan file and jar
     */
    private static Set<Class<?>> getClazzFromPkg(String pkg) {
        Set<Class<?>> classes = new LinkedHashSet<>();

        String pkgDirName = pkg.replace('.', '/');
        try {
            // this place use ClassScanner's classLoader ==>AppClassLoader
            Enumeration<URL> urls = ClassScanner.class.getClassLoader().getResources(pkgDirName);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
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
     * @param pkgName
     * @param pkgPath
     * @param classes
     * */
    private static void findClassesByFile(String pkgName, String pkgPath, Set<Class<?>> classes) {
        File dir = new File(pkgPath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

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
     * class.forName() will load the .class file to jvm and run the static code，static method etc.
     * classLoader just load .class file into jvm ,only in newInstance() to run static code，static method etc.
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
