这是一个简单的IoC容器，在容器外通过getBean来获得容器中的对象，在容器内部使用`@Autowired`(目前只支持Field)来注入对象。

提供`@Component`，`@Application`注解作为容器的注解，只要标记了这两个注解的就是容器里的类，默认是单例模式，如果要多例就再加一个注解`@Scope(type=ScopeType.PROTOTYPE)` ，实际上目前多例还不支持。

```java
@Component
public class A{}

@Component
public class B{
    @Autowied
    public A a;
}
/// 容器外部
ApplicationContext.getContext().getBean('A',A.class);
```

上面只是简单的例子，API和更多例子后续提供。

