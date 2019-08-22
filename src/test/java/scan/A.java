package scan;

import com.krysta.ioc.annotation.Autowired;
import com.krysta.ioc.annotation.Scope;
import com.krysta.ioc.annotation.container.KrystaApplication;

@KrystaApplication

public class A {

    @Autowired
    public static String s=new B().print();

    @Autowired
    int a;
}
