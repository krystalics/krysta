import com.krysta.ioc.annotation.Autowired;
import com.krysta.ioc.annotation.Component;
import com.krysta.ioc.ClassScanner;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;

public class ClassScannerTest {
    @Test
    public void scan() throws IllegalAccessException, InstantiationException {

        //类路径下的包(external libraries下)，java目录和test的java下的包可以扫描,
        Set<Class<?>> classes = ClassScanner.getClazzFromPkg("scan");//注意在test中如果有同名的包也会扫描
        Set<Class<?>> junit = ClassScanner.getClazzFromPkg("junit");

        for (Class<?> aClass : classes) {
            Component component=aClass.getAnnotation(Component.class);
            if(component!=null){
                Field[] fields = aClass.getDeclaredFields();

                for (Field field : fields) {

                    Autowired autowired=field.getAnnotation(Autowired.class);
                    if(autowired!=null){
                        System.out.println(field.getName());
                    }
                }
                System.out.println(aClass.getName());
                System.out.println(Arrays.toString(aClass.getAnnotations()));
            }

        }
    }
}
