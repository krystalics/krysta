package scan;

import com.krysta.ioc.annotation.Autowired;
import com.krysta.ioc.annotation.container.Component;

@Component(value = "good")
public class B extends A{
    @Autowired
    int s;

    public String print(){
        System.out.println("I'm B");
        return "b";
    }
}
