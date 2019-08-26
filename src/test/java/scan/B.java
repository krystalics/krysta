package scan;

import com.krysta.ioc.annotation.Autowired;
import com.krysta.ioc.annotation.container.Component;

@Component
public class B{

    @Autowired
    C c;

    public String print(){
        System.out.println("I'm B");
        return "b";
    }

    public static void main(String[] args) {
        new B();
    }
}
