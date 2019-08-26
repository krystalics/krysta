package com.krysta.ioc;

import com.krysta.ioc.annotation.container.Component;
import com.krysta.ioc.annotation.container.Application;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Krysta on 2019/8/22.
 *
 * @description 用于增加需要扫描的注解，用于细化模块，比如@Service ,@Controller等
 * @since ioc1.0
 */

public class AnnotationLoader {

    public static List<String> annotationNames = new ArrayList<>();
    public static List<Class> annotations = new ArrayList<>();

    static {
        annotationNames.add(Component.class.getName());
        annotationNames.add(Application.class.getName());
    }

    static {
        annotations.add(Component.class);
        annotations.add(Application.class);
    }

    public static String getName(Class<?> clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        String beanName = "";
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(Component.class)) {
                Component component = clazz.getAnnotation(Component.class);
                if ("".equals(component.value())) {
                    beanName = clazz.getSimpleName();
                } else {
                    beanName = component.value();
                }

            }

            if (annotation.annotationType().equals(Application.class)) {
                Application application = clazz.getAnnotation(Application.class);
                if ("".equals(application.value())) {
                    beanName = clazz.getSimpleName();
                } else {
                    beanName = application.value();
                }
            }
        }


        return beanName;
    }

    public static boolean inContainer(Class<?> clazz) {
        Component component = clazz.getAnnotation(Component.class);
        Application krystaApplication = clazz.getAnnotation(Application.class);

        return component != null || krystaApplication != null;
    }
}
