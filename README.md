This is a very simple IoC framework，support `@Component` and `@Application` these two annotation as mark to put class into the container. 

Just support singleton, not prototype. As below example, I deal with the circle dependency.

```java
//In the Container

@Application
public class A implements SuperA {
    @Autowired
    public B b;
}

@Component
public class B{
    public  @Autowired
    C c;
}

@Component
public class C{
    @Autowired
    public D d;
}

@Component
public class E extends A {
    @Autowired
    @Qualifier(name = "A")
    public SuperA a;
}

public interface SuperA extends TopSuperA{
}
public interface TopSuperA {
}

```

```java
//out of the Container 
public class ClassScannerTest {
    @Before
    public void before() {
        List<String> packages = new ArrayList<>();
        packages.add("com.krysta.ioc.scan");
        ApplicationContext.getContext().setPackages(packages);
        ApplicationContext.getContext().init();
    }

    @Test
    public void scan() {
        A a = ApplicationContext.getContext().getBean("E",A.class);
        B b = ApplicationContext.getContext().getBean(B.class);
        D d = ApplicationContext.getContext().getBean("D", D.class);
        E e = ApplicationContext.getContext().getBean(E.class);
    }
}
```

##### About BeanName

default is the simple class name，you can define by yourself . like below

```java
@Component(value="myBeanName")
public class A{}
```

##### About the Limit

The class which want to join the container, must have a empty constructor. 



