## Import vs ComponentScan

Simply put, we can reach the same result with both annotations. So, is there any difference between them? To answer this question, let's remember that Spring generally promotes the convention-over-configuration approach. Making an analogy with our annotations, @ComponentScan is more like convention, while @Import looks like configuration.

- Typically, we start our applications using @ComponentScan in a root package so it can find all components for us. If we're using Spring Boot, then @SpringBootApplication already includes @ComponentScan, and we're good to go. This shows the power of convention.

- Now, let's imagine that our application is growing a lot. Now we need to deal with beans from all different places, like components, different package structures, and modules built by ourselves and third parties. 

- In this case, adding everything into the context risks starting conflicts about which bean to use. Besides that, we may get a slow start-up time.

- On the other hand, we don't want to write an @Import for each new component because doing so is counterproductive.


Let's assume that we already have prepared three beans – Bird, Cat, and Dog – each with its own configuration class.

```java
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { BirdConfig.class, CatConfig.class, DogConfig.class })
class ConfigUnitTest {

    @Autowired
    ApplicationContext context;

    @Test
    void givenImportedBeans_whenGettingEach_shallFindIt() {
        assertThatBeanExists("dog", Dog.class);
        assertThatBeanExists("cat", Cat.class);
        assertThatBeanExists("bird", Bird.class);
    }

    private void assertThatBeanExists(String beanName, Class<?> beanClass) {
        Assertions.assertTrue(context.containsBean(beanName));
        Assertions.assertNotNull(context.getBean(beanClass));
    }
}
```

### Grouping Configurations with @Import

```java
@Configuration
@Import({ DogConfig.class, CatConfig.class })
class MammalConfiguration {
}
```

```java
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { MammalConfiguration.class })
class ConfigUnitTest {

    @Autowired
    ApplicationContext context;

    @Test
    void givenImportedBeans_whenGettingEach_shallFindOnlyTheImportedBeans() {
        assertThatBeanExists("dog", Dog.class);
        assertThatBeanExists("cat", Cat.class);

        Assertions.assertFalse(context.containsBean("bird"));
    }

    private void assertThatBeanExists(String beanName, Class<?> beanClass) {
        Assertions.assertTrue(context.containsBean(beanName));
        Assertions.assertNotNull(context.getBean(beanClass));
    }
}
```

```java
@Configuration
@Import({ MammalConfiguration.class, BirdConfig.class })
class AnimalConfiguration {
}
```

```java
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AnimalConfiguration.class })
class AnimalConfigUnitTest {
    // same test validating that all beans are available in the context
}
```

## Logging

Frameworks:
- **Java Util**
- **Log4J2**
- **SLF4J**
- **Logback**

Useally we have the following types of logging:
- **FATAL** - crucial error, and application stops
- **ERROR** - crucial error, and application stops
- **WARN** - error but the application will run
- **INFO** - any information we want to print to the console
- **DEBUG** - web application is in debug mode
- **TRACE** - for every minor logs


The way logging works is, if we enable **TRACE** then all above severities like **DEBUG** **INFO** **WARN** and **ERROR** are also printed.
Suppose if we enable **ERROR** logging only, then all lower severities like **WARN** **INFO** **DEBUG** **TRACE** will not be logged.


**APPLICATION LOGS**

```properties
debug=true
```

**MORE LOG DETAILS**

```properties
trace=true
```

**SHOWING LOGS**

```properties
logging.level.root=INFO 							
logging.level.com.ioc.* = ERROR 				
logging.level.com.ioc.controller = ERROR
logging.level.com.ioc.aspect = TRACE 			
```

**GROUPING**

```properties
logging.group.eazy_school_error=com.ioc.aspects, com.ioc.controller
logging.level.eazy_school_error=ERROR
```

**COLORING**

```properties
spring.output.ansi.enabled=ALWAYS
```


## Actuator & Spring Boot Admin

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

```properties
management.endpoints.web.base-path=/ioc/actuator
management.endpoints.web.exposure.include=*
```

```xml
<dependency>
	<groupId>de.codecentric</groupId>
	<artifactId>spring-boot-admin-starter-client</artifactId>
</dependency>
```

```java
@EnableAdminServer
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
```