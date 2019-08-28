import com.krysta.ioc.ApplicationContext;
import com.krysta.ioc.annotation.container.Application;
import com.krysta.ioc.factory.BeanFactory;
import org.junit.Before;
import org.junit.Test;
import scan.A;
import scan.B;
import scan.D;
import scan.E;

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
    public void before() {
        List<String> packages = new ArrayList<>();
        packages.add("scan");
        ApplicationContext.getContext().setPackages(packages);
        ApplicationContext.getContext().init();

    }

    @Test
    public void scan() {
        A a = ApplicationContext.getContext().getBean("E",A.class);
        B b = ApplicationContext.getContext().getBean(B.class);
        D d = ApplicationContext.getContext().getBean("D", D.class);
        E e = ApplicationContext.getContext().getBean(E.class);
        System.out.println();
    }
}
