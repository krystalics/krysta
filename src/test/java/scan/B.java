package scan;

import com.krysta.ioc.annotation.Component;

@Component
public class B {
    public String print(){
        System.out.println("I'm B");
        return "b";
    }
}
