import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassScanner {

    /*
    * 使用url 同时扫描file和jar，
    * */
    public static Set<Class<?>> getClazzFromPkg(String pkg) {
        Set<Class<?>> classes = new LinkedHashSet<>(); //按照添加顺序排序

        String pkgDirName = pkg.replace('.', '/'); //将包名中的.转为/ 变为目录
        try {
            // 这里用ClassScanner 的类加载器扫描这个包，这个类加载器如果没有自己写的话，一般最终会是有JVM提供的 AppClassLoader，
            Enumeration<URL> urls = ClassScanner.class.getClassLoader().getResources(pkgDirName);
            while(urls.hasMoreElements()){
                URL url=urls.nextElement();
                String protocol=url.getProtocol();
                if("file".equals(protocol)){
                    String filePath= URLDecoder.decode(url.getFile(),"UTF-8"); //获取包名的物理路径
                    findClassesByFile(pkg,filePath,classes);
                }else if("jar".equals(protocol)){
                    JarFile jar=((JarURLConnection)url.openConnection()).getJarFile();
                    findClassesByJar(pkg,jar,classes);
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

        String className = "";
        Class clazz;
        for (File file : dirfiles) {
            if (file.isDirectory()) {
                findClassesByFile(pkgName + "." + file.getName(), pkgPath + "/" + file.getName(), classes);
                continue;
            }

            // 将文件名去掉末尾的 .class 6个字符
            className = file.getName();
            className = className.substring(0, className.length() - 6);

            clazz = loadClass(pkgName + "." + className);
            if (clazz != null) {
                classes.add(clazz);
            }
        }

    }

    private static void findClassesByJar(String pkgName, JarFile jarFile, Set<Class<?>> classes) {
        String pkgDir = pkgName.replace(".", "/");
        Enumeration<JarEntry> entry = jarFile.entries();

        JarEntry jarEntry = null;
        String name = "", className = "";
        Class<?> clazz;
        while (entry.hasMoreElements()) {
            jarEntry = entry.nextElement();
            name = jarEntry.getName();
            if (name.charAt(0) == '/') {
                name = name.substring(1);
            }

            if (jarEntry.isDirectory() || !name.startsWith(pkgDir) || !name.endsWith(".class")) {
                continue;
            }


            className = name.substring(0, name.length() - 6);
            clazz = loadClass(className.replace("/", "."));
            if (clazz != null) {
                classes.add(clazz);
            }

        }

    }

    /*
    * 利用线程自带的ClassLoader来加载该类
    * */
    private static Class<?> loadClass(String fullClassName) {
        try{
            return Thread.currentThread().getContextClassLoader().loadClass(fullClassName);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
}
