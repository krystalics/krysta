package com.krysta.ioc.scan;

import com.krysta.ioc.annotation.Autowired;
import com.krysta.ioc.annotation.Qualifier;
import com.krysta.ioc.annotation.container.Component;

@Component
public class E extends A {
    @Autowired
    @Qualifier(name = "goof")
    public SuperA a;
}
