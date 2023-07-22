## Setting up MySQL Container

```bash
docker run -d -p 3306:3306 --name mysql-docker-container -e MYSQL_ROOT_PASSWORD=spring -e MYSQL_DATABASE=spring -e MYSQL_USER=spring -e MYSQL_PASSWORD=spring mysql/mysql-server:latest
```

## Events

Spring events are one of the core capabilities provided by the Spring Framework.

The core of the events are:
- Events
- Publishers
- Listeners
    
Its based on the Observer Pattern
- Observers (Gof) -> Listeners  (In Spring)
- Subject (Gof) -> Publishers (In Spring)
        
The flow:
- A application event is a simple POJO class that holds data and exchange between Publisher and Listener
- Publisher construct the event object and sends the event
- They can be implemented via interface or annotation

**Interface**

```java
public class Listener implements ApplicationListener<ApplicationEvent> {

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        // process the event here
    }
}
```

**Annotation**

```java
@EventListener
public void onOne(Foo event)

@EventListener({Foo.class, Bar.class})
public void onMultiple(Object event)

@EventListener
public void onAll()
```

```java
// If the return type is not void then its send a new event
@EventListener
public Foo onEvent() {
    // process event
    return new Foo()
}

// We are able to set the order via
@Order 
@EventListener
public Collection onEvent() {
    // all of them send individually as a new event
    return asList(new Foor(), new Bar(), new Baz())
}
```

Spring allows us to bound the transaction to our events:
- BEFORE_COMMIT
- AFTER_COMMIT
- AFTER ROLLBACK
- AFTER COMPLETATION
                                                                        
Predfined events:
   
**Application**
- ApplicationStartingEvent
- ApplicationEnvironmentPreparedEvent
- ApplicationContextInitializedEvent
- ApplicationPreparedEvent
- ApplicationStartedEvent
	
**Context**
- ContextRefreshedEvent
- ContextStartedEvent
- ContextStoppedEvent
- ContextClosedEvent
            
Filtering events via expression language:

```java                
// newsletter is a boolean attribute in the customer object
@EventListener(condition = "#event.customer.newsletter")
public void onRegistrationEvent (CustomerRegisteredEvent event) {
    promotionService.applyPromotion(event.getCustomer());
}
```
                
### Async Support 

We'll do this by adding the @EnableAsync to a configuration class:

```java
@Configuration
@EnableAsync
public class SpringAsyncConfig { ... }
```

**The @Async Annotation**

First, let's go over the rules. @Async has two limitations:

- It must be applied to public methods only.
- Self-invocation — calling the async method from within the same class — won't work.

The reasons are simple: The method needs to be public so that it can be proxied. And self-invocation doesn't work because it bypasses the proxy and calls the underlying method directly.

This is the simple way to configure a method with void return type to run asynchronously:

```java
@Async
public void asyncMethodWithVoidReturnType() {
    System.out.println("Execute method asynchronously. " 
      + Thread.currentThread().getName());
}
```

We can also apply @Async to a method with return type by wrapping the actual return in the Future:

```java
@Async
public Future<String> asyncMethodWithReturnType() {
    System.out.println("Execute method asynchronously - " 
      + Thread.currentThread().getName());
    try {
        Thread.sleep(5000);
        return new AsyncResult<String>("hello world !!!!");
    } catch (InterruptedException e) {
        //
    }

    return null;
}
```

**The Executor**

By default, Spring uses a SimpleAsyncTaskExecutor to actually run these methods asynchronously. But we can override the defaults at two levels: the application level or the individual method level.

Override the Executor at the Method Level

```java
@Configuration
@EnableAsync
public class SpringAsyncConfig {
    
    @Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }
}
```

**Exception Handling**

When a method return type is a Future, exception handling is easy. Future.get() method will throw the exception.

But if the return type is void, exceptions will not be propagated to the calling thread. So, we need to add extra configurations to handle exceptions.

```java
public class CustomAsyncExceptionHandler
  implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(
      Throwable throwable, Method method, Object... obj) {
 
        System.out.println("Exception message - " + throwable.getMessage());
        System.out.println("Method name - " + method.getName());
        for (Object param : obj) {
            System.out.println("Parameter value - " + param);
        }
    }
    
}
```
