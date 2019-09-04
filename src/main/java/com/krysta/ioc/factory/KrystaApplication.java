package com.krysta.ioc.factory;

import java.util.ArrayList;
import java.util.List;

public class KrystaApplication {

    public static void run(String pkg) {
        List<String> pkgs=new ArrayList<>();
        pkgs.add(pkg);
        run(pkgs);
    }

    public static void run(List<String> pkgs){
        BeanFactory factory=new BeanFactory();
        factory.setPackages(pkgs);
        factory.init();
    }
}
