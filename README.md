### Docker containers

```bash
docker run -d --name activemq -p 61616:61616 -p 8161:8161 webcenter/activemq
```

### Spring JMS

- JMS Specification and MOM
- Set of interfaces and terms implemented by a provider
- Message oriented middleware (MOM)

There are two types:
- Point to Point
- msg is publish to queue by a producer
- msg is read and removed by consumer


- Publish and subscribe
- msg is published to a topic by a producer
- msg is read by one or more subscribers


- Spring JMS API is not a message oriented middleware provider
- Spring JMS API is an abastraction layer to JMS 1.1
- Spring JMS API need a MOM API to communicate to a MOM


Using the JMS 1.1 Specification:
- Create the JDNI inital context
- Obtain a ConnectionFacotry from the JDNI context
- Obtain a queueu from the ConnectionFacotry
- Create a session object
- Create a sender or reciver
- Sender or recvers reads the object
- It closes everything


**All the above steps are wrapped up via spring jms api called JMS template**


JMS **basics** mesasge converter:
- Streams - Stream messages
- Maps - MapMessages
- Text - TextMessages
- Object - ObjectMessages
- Bytes - BytesMessages


SimpleMessageConverter from **Spring JMS** can handle
- TextMessages
- BytesMessages
- ObjectMessages
- MapMessages


**JmsTemplate.convertAndSend method's default conversion strategy is SimpleMessageConverter. We can define our own like MarshallingMessageConverter, MappingJackson2MessageConverter**


What if we want a custom converter ??
- then we implement MessaageConverter Interface
- and implement the following methods toMessage and fromMessage


```java
@EnableJms
@SpringBootApplication
public class Application {

	public static void main(String[] args) {		
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        
        Sender sender = context.getBean(Sender.class);
        sender.sendMessage("order-queue", "Hello");  
	}
	
    @Bean
    public JmsListenerContainerFactory warehouseFactory(ConnectionFactory factory, DefaultJmsListenerContainerFactoryConfigurer configurer){
        DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();
        configurer.configure(containerFactory, factory);

        return containerFactory;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        return factory;
    }
    
    @Bean 
    public JmsTemplate jmsTemplate(){
        return new JmsTemplate(connectionFactory());
    }

    public ActiveMQConnectionFactory connectionFactory(){
    	return new ActiveMQConnectionFactory("admin","admin","tcp://localhost:61616");
    }
}
```

```java
@Slf4j
@Component
public class Receiver {

	// because we defined the bean DefaultJmsListenerContainerFactory, we dont need the containerFactory attribute
	// @JmsListener(destination = "order-queue", containerFactory = "warehouseFactory")  
	@JmsListener(destination = "order-queue")  
    public void receiveMessage(String order){
		log.info("Order received is: {}", order);
    }
}
```

```java
@Component
public class Sender {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(String destination, String message){
        jmsTemplate.convertAndSend(destination, message);
    }
}
```


### Type of factories

**SingleConnectionFactory** 
- Returns same connection from createConnection() calls
- Ignores calls to connection close
- Is thread safe

```java
@Bean
public SingleConnectionFactory connectionFactory(){

    SingleConnectionFactory factory = new SingleConnectionFactory(new ActiveMQConnectionFactory(user, password, brokerUrl));
    factory.setReconnectOnException(true);
    factory.setClientId("StoreFront"); 
    
    return factory;
}
```

**CachingConnectionFactory** 
- Returns same connection from createConnection() calls
- Is thread safe
- Ignores calls to connection close
- Default to cache one sesion, but sessioncachesize can be changed to cache more
- Used when there is a big load

```java
@Bean
public CachingConnectionFactory connectionFactory() {

    CachingConnectionFactory factory = new CachingConnectionFactory(new ActiveMQConnectionFactory(user, password, brokerUrl));
    factory.setClientId("StoreFront");
    factory.setSessionCacheSize(100);
    return factory;
}
```