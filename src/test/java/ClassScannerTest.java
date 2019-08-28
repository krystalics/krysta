import com.krysta.ioc.ApplicationContext;
import com.krysta.ioc.annotation.container.Application;
import com.krysta.ioc.factory.BeanFactory;
import org.junit.Before;
import org.junit.Test;
import scan.A;
import scan.D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krysta on 2019/8/20.
 *
 * @description
 * @since ioc1.0
 */
public class ClassScannerTest {

    @Before
    public void before(){
//        BeanFactory beanFactory = new BeanFactory();

        List<String> packages = new ArrayList<>();
        packages.add("scan");
        ApplicationContext.getContext().setPackages(packages);
        ApplicationContext.getContext().init();

    }
    @Test
    public void scan()  {

        //类路径下的包(external libraries下)，java目录和test的java下的包可以扫描,
//        Set<Class<?>> classes = ClassScanner.getClazzFromPkg("scan");//注意在test中如果有同名的包也会扫描
//        Set<Class<?>> junit = ClassScanner.getClazzFromPkg("junit");
//

        System.out.println();
        D d = ApplicationContext.getContext().getBean("D", D.class, 3);

        System.out.println();
    }
}
