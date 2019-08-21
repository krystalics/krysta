package scan;

import annotation.Component;
import annotation.Autowired;

@Component
public class A {

    @Autowired
    public static String s=new B().print();


}
