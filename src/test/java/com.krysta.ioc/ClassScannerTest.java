package com.krysta.ioc;

import com.krysta.ioc.scan.A;
import com.krysta.ioc.scan.B;
import com.krysta.ioc.scan.D;
import com.krysta.ioc.scan.E;
import org.junit.Before;
import org.junit.Test;

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
        packages.add("com.krysta.ioc.scan");
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
