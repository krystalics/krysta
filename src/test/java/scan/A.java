package scan;

import com.krysta.ioc.annotation.Component;
import com.krysta.ioc.annotation.Autowired;

@Component
public class A {

    @Autowired
    public static String s=new B().print();


}
