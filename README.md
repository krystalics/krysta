This is a very simple IoC framework，support `@Component` and `@Application` these two annotation as mark to put class into the container permanently. 

##### About the Limit

The class which want to join the container, must have a empty constructor.  

If a class isn't in the container but its subclass in, you can also use it like it is in container. It is like the component which does not have beanname .

##### Maven :

```xml
<dependency>
  <groupId>com.github.krystalics</groupId>
  <artifactId>krysta</artifactId>
  <version>1.0</version>
</dependency>
```

##### Gradle :

```groovy
implementation 'com.github.krystalics:krysta:1.0'
```

another tool can see this -> [sonatype 's detail](https://search.maven.org/artifact/com.github.krystalics/krysta/1.0/jar) 

##### Examples

Just support singleton, not prototype. As below example, I deal with the circle dependency.

```java
//In the Container

@Component
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
    //SuperA has two beanName even though it is not in the container,  A and E
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
        //you can add annother package
        ApplicationContext.getContext().setPackages(packages);
        ApplicationContext.getContext().init();
    }

    @Test
    public void scan() {
        A a = ApplicationContext.getContext().getBean("E",A.class);
        B b = ApplicationContext.getContext().getBean(B.class);
        D d = ApplicationContext.getContext().getBean("D", D.class);
        E e = ApplicationContext.getContext().getBean(E.class);
        // use these component to start the program
    }
}
```

##### About BeanName

default is the simple class name，you can define by yourself . like below

```java
@Component(value="myBeanName")
public class A{}
```

when a class has some subclasses in the container, it'll has multiple beanname which contains its subclasses's beanname. So when you want to autowired it , you must use @Qualifier to specific the beanname. 

```java
@Component
public class B extends A{
    
}

@Component
public class Main{
    @Autowird 
    public A a; //it is wrong
    
    // A has two beanName : A , B so we must specific it
    @Autowird
    @Qualifier(name="A") //name="B" can also do
    public A a;
}
```

##### API

scan packges to start the container.

```java
KrystaApplication.run(packages); // List<String> packages
KrystaApplication.run("one package");
```

getBean() only by beanName , need to convert type forcely.

```java
A a=(A)ApplicationContext.getContext().getBean("good");
```

getBean() by beanName and type

```java
//actually a is B's object , because B extends A, so A has B's beanname
A a = ApplicationContext.getContext().getBean("B",A.class);  
A a2 = ApplicationContext.getContext().getBean("A",A.class);  
A a3 = ApplicationContext.getContext().getBean("B",B.class);  //error , the type must be equal to a3's type
```

getBean() only by type , need class just has one beanName

```java
A a = ApplicationContext.getContext().getBean(A.class); //error , because A has two beanName ,so you must select one of them
A b = ApplicationContext.getContext().getBean("A",A.class);

B b = ApplicationContext.getContext().getBean(B.class); //right, because B just has one beanName
```







