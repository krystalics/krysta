package scan;

import com.krysta.ioc.annotation.Autowired;
import com.krysta.ioc.annotation.container.Component;

@Component
public class B extends A{


    public String print(){
        System.out.println("I'm B");
        return "b";
    }

    public static void main(String[] args) {
        new B();
    }
}
