package com.krysta.ioc.scan;

import com.krysta.ioc.annotation.Autowired;
import com.krysta.ioc.annotation.container.Component;

@Component
public class C{
    @Autowired
    public D d;
}
