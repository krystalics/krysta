package scan;

import com.krysta.ioc.annotation.Autowired;
import com.krysta.ioc.annotation.Scope;
import com.krysta.ioc.annotation.container.KrystaApplication;

@KrystaApplication(value = "good")

public class A implements SuperA {
    @Autowired
    D d;
}
