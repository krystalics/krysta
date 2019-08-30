package com.krysta.ioc.scan;

import com.krysta.ioc.annotation.Autowired;
import com.krysta.ioc.annotation.container.Application;

@Application
public class A implements SuperA {
    @Autowired
    public B b;
}
