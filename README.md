这是一个简单的IoC容器，在容器外通过getBean来获得容器中的对象，在容器内部使用`@Autowired`(目前只支持Field)来注入对象。

提供`@Component`，`@Application`注解作为容器的注解，只要标记了这两个注解的就是容器里的类，默认是单例模式，如果要多例就再加一个注解`@Scope(type=ScopeType.PROTOTYPE)` ，实际上目前多例还不支持。

```java
//容器内部

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
//容器外部 
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
        System.out.println();
    }
}
```





上面只是简单的例子，API和更多例子后续提供。

