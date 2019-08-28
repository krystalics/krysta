package scan;

import com.krysta.ioc.ScopeType;
import com.krysta.ioc.annotation.Autowired;
import com.krysta.ioc.annotation.Scope;
import com.krysta.ioc.annotation.container.Component;

@Component
@Scope(type = ScopeType.PROTOTYPE)
public class D {



    public D(int s) {

    }

    public D() {

    }
}
