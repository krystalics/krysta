package scan;

import com.krysta.ioc.annotation.Autowired;
import com.krysta.ioc.annotation.Qualifier;
import com.krysta.ioc.annotation.container.Application;

@Application(value = "good")
public class A implements SuperA {
    @Autowired
    B b;

}
