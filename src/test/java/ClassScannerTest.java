import com.krysta.ioc.ClassScanner;
import com.krysta.ioc.annotation.Autowired;
import com.krysta.ioc.annotation.container.Component;
import com.krysta.ioc.factory.BeanRegistry;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by Krysta on 2019/8/20.
 *
 * @description
 * @since ioc1.0
 */
public class ClassScannerTest {
    @Test
    public void scan() throws IllegalAccessException, InstantiationException {

        //类路径下的包(external libraries下)，java目录和test的java下的包可以扫描,
        Set<Class<?>> classes = ClassScanner.getClazzFromPkg("scan");//注意在test中如果有同名的包也会扫描
        Set<Class<?>> junit = ClassScanner.getClazzFromPkg("junit");

        BeanRegistry registry=new BeanRegistry();

        for (Class<?> aClass : classes) {
            registry.getBeanName(aClass);
            Field[] fields = aClass.getDeclaredFields();
            Component component = aClass.getAnnotation(Component.class);

            if (component != null)
                System.out.println(component.value());
            for (Field field : fields) {

                Autowired autowired = field.getAnnotation(Autowired.class);
                if (autowired != null) {
                    System.out.println(field.getName());
                }
            }
            System.out.println(aClass.getName());
            System.out.println(Arrays.toString(aClass.getAnnotations()));

        }

        registry.registerClasses(classes);
        System.out.println();
    }
}
