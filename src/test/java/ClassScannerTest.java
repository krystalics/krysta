import com.krysta.ioc.ClassScanner;
import com.krysta.ioc.annotation.Autowired;
import com.krysta.ioc.annotation.container.Component;
import com.krysta.ioc.factory.AbstractBeanRegistry;
import com.krysta.ioc.factory.BeanFactory;
import com.krysta.ioc.factory.BeanRegistry;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Krysta on 2019/8/20.
 *
 * @description
 * @since ioc1.0
 */
public class ClassScannerTest {

    @Before
    public void before(){
        BeanFactory beanFactory = new BeanFactory();
        List<String> packages = new ArrayList<>();
        packages.add("scan");
        beanFactory.setPackages(packages);
        beanFactory.init();

    }
    @Test
    public void scan()  {

        //类路径下的包(external libraries下)，java目录和test的java下的包可以扫描,
//        Set<Class<?>> classes = ClassScanner.getClazzFromPkg("scan");//注意在test中如果有同名的包也会扫描
//        Set<Class<?>> junit = ClassScanner.getClazzFromPkg("junit");
//

        System.out.println();

    }
}
