## Apache Kafka

![Architecture](https://raw.githubusercontent.com/matebence/spring/kafka/docs/apache_kafka.png)

### Kafka Topics

- Its like a table in the database
- We can habe as many topics we want
- Topics are indentified by name

#### Topics paritions

- Topics are split to paritions
- From 0 to x
- Each message in the paritions gets a incremental id, which in kafka is called **offset**
- Offset only have meaning for a specific parition
- Order is guaranteed only in the parition
- Offset 3 in parition 0 doesnt represent the same data in parition 1
- Once the data is written to a partition, it cant be changed
- Data is assigned random to a parititon unless a key is provided

### Topic replication factor

- Topics should have a replication factor useally betwen 2 and 3    

### Kafka brokers

- A kafka brokers holds the topics
- A kafka cluster is composed of multiple brokers(server)
- Each broker is identfied with its id (integer)

Example:

    Topic A with 3 paritions
    Topic B with 2 paritions

                Broker 101
                    Topic A -> Parition 0
                    Topic B -> Parition 1
                Broker 102
                    Topic A -> Parition 2
                    Topic B -> Parition 0
                Broker 103
                    Topic A -> Parition 1

#### Concept of Leader for a Parition

- At any time only **ONE** broker can be a leader for a given partition
- Only that leader can receive and server data for a partition
- The other brokers will synchronize the data      

#### Kafka Broker Discovery

- Every Kafka broker is also called a bootstrap server
- That means that you only need to connec to one broker, and you will be

### Producers

- Producers write data to topics (which is made of partitions)
- Producers automatically know to which broker and parition to write to
- In case of broker failures Producers with automatically recover
    
**Producers can choose to recive acknowledgment of data writers:**

- acks = 0 - Producer wont wait for ackowledgment (possible data loss)
- acks = 1 - Producer will wait for leader acknowledgement (limited data loss)
- acks = all - Leader + replicas acknowledgment (no data loss)
        
**Producers: Message keys:**

- Producers can choose to send a key with the message (string, number, etc ...)
- If key is null, data is sent to round robin
- If key is sent then all messager for that key will alwas go the same parition

### Consumer

- Consumers read data from a topic
- Consumers know which broker to read from
- In case of broker failures, consumer know how to recover
- Data is read in order within each partitions

#### Consumers Groups

- Consumer read data in consumer groups
- Each consumer within a group read from exclusive partitions
- If you have more consumers than paritions, some consumers will be inactive

Example

    Topic A parititon 0
    Topic A parititon 1
    Topic A parititon 2

    consumer-group-application-1
        - consumer 1
        - consumer 2
    consumer-group-application-2
        - consumer 1
        - consumer 2
        - consumer 3
    consumer-group-3
        - consumer-1

#### Consumer Offsets

- Kafka stores the offsets at which a consumer has been reading 
- Its like bookmarking

**Consumers choose when to commit offset, there are 3 delivery semantics:**
- At most once
    - Offsets are committed as soon as the message is recived
    - If the processing goes wrong, the message will be lost
- At leas once (recommened)
    - Offsets are commited after the message is processed
    - If the processing goes wrong, the message will be read again
    - This can be result in duplicate processing of messages. Make sure your processing is idempotent
- Exactly once:
    - Can be achived for Kafka to Kafka, using Kafka Streams API
    
#### Additional client besides Consumer & Producer

**Kafka Streams** provides an API for message streaming that incorporates a framework for processing, enriching, and transforming messages. It is a fully scalable, reliable, and maintainable library enabling fast processing of unbounded datasets in real-time with low latency. 

**Kafka Connect** is a framework for connecting Kafka with external systems such as databases, key-value stores, search indexes, and file systems, using so-called **Connectors**.

Kafka Connectors are ready-to-use components, which can help us to import data from external systems into Kafka topics and export data from Kafka topics into external systems. We can use existing connector implementations for common data sources and sinks or implement our own connectors.

A source connector collects data from a system. Source systems can be entire databases, streams tables, or message brokers. A source connector could also collect metrics from application servers into Kafka topics, making the data available for stream processing with low latency.

A **Kafka sink connector** delivers data from Kafka topics into other systems, which might be indexes such as Elasticsearch, batch systems such as Hadoop, or any kind of database.

### Zookeper

- Manages brokers (keeps a list of them)
- Zookeeper helps in performing leader election for partitions
- Send notfication to kafka in case of changes: new topic, broker dies etc ...

### Setting up environment

```bash
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk version


sdk list java
sdk install java 11.0.20-tem

java -version

wget https://downloads.apache.org/kafka/3.5.1/kafka-3.5.1-src.tgz
tar -xf kafka-3.5.1-src.tgz

cd kafka-3.5.1-src/
./gradlew jar -PscalaVersion=2.13.10

mkdir data
mkdir data/zookeeper
mkdir data/kafka

# We set the following keys in the properties file
# log.dirs
# dataDir
cd config
nano zookeeper.properties
nano server.properties

# Start server and zookeper
cd bin
./zookeeper-server-start.sh ../config/zookeeper.properties
./kafka-server-start.sh ../config/server.properties

# Multi cluster setup

cd config

mv server.properties server-0.properties
cp server-0.properties server-1.properties

# log.dirs=\data\kafka-0
# listeners=PLAINTEXT://:9092
# broker.id=0

vi server-0.properties

# log.dirs=\data\kafka-1
# listeners=PLAINTEXT://:9093
# broker.id=1

vi server-1.properties

cd data
mv kafka kafka-0
mkdir kafka-1
```

### Create topics

```bash
# Show all options
./kafka-topics.sh

# List topics
./kafka-topics.sh --bootstrap-server localhost:9092 --list

# Create topics
./kafka-topics.sh --bootstrap-server localhost:9092 --topic first_topic --create --partitions 3 --replication-factor 1
   
# Describe topics
./kafka-topics.sh --bootstrap-server localhost:9092 --topic first_topic --describe

# Delete topic
./kafka-topics.sh --bootstrap-server localhost:9092 --topic second_topic --create --partitions 3 --replication-factor 1
./kafka-topics.sh --bootstrap-server localhost:9092 --list
./kafka-topics.sh --bootstrap-server localhost:9092 --topic second_topic --delete
./kafka-topics.sh --bootstrap-server localhost:9092 --list
```

### Sending messages

```bash
./kafka-console-producer.sh --broker-list localhost:9092 --topic first_topic --producer-property acks=all

./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic first_topic --from-beginning

./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic first_topic --group my-first-application
```

### Java console app

```xml
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka_2.13</artifactId>
    <version>3.4.0</version>
</dependency>

<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.6</version>
</dependency>

<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-simple</artifactId>
    <version>2.0.6</version>
</dependency>
```

### Simple producere

```java
public class Producer {

    private final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String args[]) {
        //create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create the producer
        // the key and the value is string
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        ProducerRecord<String, String> record = new ProducerRecord<>("first_topic", "Hello world");

        // with callback
        producer.send(record, (recordMetadata, e) -> {
            if (e == null) {
                logger.info("Received new metadata");
                logger.info("Topic" + recordMetadata.topic());
                logger.info("Partition" + recordMetadata.partition());
                logger.info("Offset" + recordMetadata.offset());
                logger.info("Timestamp" + recordMetadata.timestamp());
            } else {
                logger.error("Failed", e);
            }
        });

        producer.flush();
        producer.close();
    }
}
```

#### Producer with specified key

```java
public class Producer {

    private final static Logger logger = LoggerFactory.getLogger(Consumer.class);

    public static void main(String args[]) {
        //create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create the producer
        // the key and the value is string
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

      /* if we provide a key the value will always go to the same partion, no matter how offen we rerun the app
            id_0 is going to parition 1
            id_1 is going to parition 2
            id_2 is going to parition 0
            id_3 is going to parition 2
            id_4 is going to parition 0
            id_5 is going to parition 2
            id_6 is going to parition 0
            id_7 is going to parition 2
        */
        for (int i = 0; i < 10; i++) {
            String topic = "first_topic";
            String value = "Hello world";
            String key = "Key" + i;
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);

            // with callback
            producer.send(record, (recordMetadata, e) -> {
                if (e == null) {
                    logger.info("Received new metadata");
                    logger.info("Topic" + recordMetadata.topic());
                    logger.info("Partition" + recordMetadata.partition());
                    logger.info("Offset" + recordMetadata.offset());
                    logger.info("Timestamp" + recordMetadata.timestamp());
                } else {
                    logger.error("Failed", e);
                }
            });
        }

        producer.flush();
        producer.close();
    }
}
```

### Simple consumer

```java
public class Consumer {

    private final static Logger logger = LoggerFactory.getLogger(Consumer.class);

    public static void main(String[] args) {
        //create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "my-fourth-application");
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(List.of("first_topic"));

        while(true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                logger.info("Key" + record.key());
                logger.info("Value" + record.value());
                logger.info("Offset" + record.offset());
                logger.info("Partition" + record.partition());
            }
        }
    }
}
```

#### Using threads

```java
public class Consumer {

    private final static Logger logger = LoggerFactory.getLogger(Consumer.class);

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ConsumerThread consumerThread = new ConsumerThread(latch);
        Thread thread = new Thread(consumerThread);
        thread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(consumerThread::shutdown));
        latch.await();
    }

    public static class ConsumerThread implements Runnable {

        private CountDownLatch latch;
        private KafkaConsumer<String, String> consumer;

        public ConsumerThread(CountDownLatch latch) {
            this.latch = latch;

            //create Producer properties
            Properties properties = new Properties();
            properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "my-fourth-application");
            properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

            consumer = new KafkaConsumer<>(properties);
            consumer.subscribe(List.of("first_topic"));
        }

        @Override
        public void run() {
            try {
                while(true) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                    for (ConsumerRecord<String, String> record : records) {
                        logger.info("Key" + record.key());
                        logger.info("Value" + record.value());
                        logger.info("Offset" + record.offset());
                        logger.info("Partition" + record.partition());
                    }
                }
            } catch (WakeupException ignored) {
            } finally {
                consumer.close();
                latch.countDown();
            }
        }

        public void shutdown() {
            consumer.wakeup();
        }
    }
}
```

#### Replay data or fetch a specific message

```java
public class Consumer {

    private final static Logger logger = LoggerFactory.getLogger(Consumer.class);

    public static void main(String args[]) {
        //create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        long offsetToReadFrom = 15L;
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        TopicPartition partitionToReadFrom = new TopicPartition("first_topic", 0);

        consumer.assign(List.of(partitionToReadFrom));
        consumer.seek(partitionToReadFrom, offsetToReadFrom);

        boolean keepOnReading = true;

        int numberOfMessagesToRead = 5;
        int numberOfMessagesReadSoFar = 0;

        while(keepOnReading) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                numberOfMessagesReadSoFar++;

                logger.info("Key" + record.key());
                logger.info("Value" + record.value());
                logger.info("Offset" + record.offset());
                logger.info("Partition" + record.partition());

                if(numberOfMessagesReadSoFar >= numberOfMessagesToRead) {
                    keepOnReading = false;
                    break;
                }
            }
        }
    }
}
```

### Example projects

![Social](https://raw.githubusercontent.com/matebence/spring/kafka/docs/driver.png)

![Driver](https://raw.githubusercontent.com/matebence/spring/kafka/docs/social.png)


### Spring project snippets

#### Important producer configurations

- acks
  - 0 - Guarantees message is written to the leader (Default)
  - 1 - No guarantee (Not recommended) 
  - all - Guarantees message is wirtten to leader and to all the replicas
- retries - Attempting to retry a failed request to a given topic partition
- retry.backoff.ms - The time to wait before attempting to retry a failed request to a given topic partition

```bash
.\zookeeper-server-start.bat ..\..\config\zookeeper.properties
.\kafka-server-start.bat ..\..\config\server-0.properties
.\kafka-server-start.bat ..\..\config\server-1.properties
```

```bash
.\kafka-topics.bat --bootstrap-server localhost:9092 --topic library-events.RETRY --create --partitions 3 --replication-factor 1
.\kafka-topics.bat --bootstrap-server localhost:9092 --topic library-events.DLT --create --partitions 3 --replication-factor 1
.\kafka-topics.bat --bootstrap-server localhost:9092 --topic library-events --create --partitions 3 --replication-factor 1
.\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic library-events --from-beginning
```

#### POST WITH-NULL-LIBRARY-EVENT-ID

```bash
curl -i \
-d '{"libraryEventId":null,"book":{"bookId":456,"bookName":"Kafka Using Spring Boot","bookAuthor":"Dilip"}}' \
-H "Content-Type: application/json" \
-X POST http://localhost:8080/v1/libraryevent
```

#### PUT WITH ID - 1

```bash
curl -i \
-d '{"libraryEventId":1,"book":{"bookId":456,"bookName":"Kafka Using Spring Boot 2.X","bookAuthor":"Dilip"}}' \
-H "Content-Type: application/json" \
-X PUT http://localhost:8080/v1/libraryevent
```

```bash
curl -i \
-d '{"libraryEventId":2,"book":{"bookId":456,"bookName":"Kafka Using Spring Boot 2.X","bookAuthor":"Dilip"}}' \
-H "Content-Type: application/json" \
-X PUT http://localhost:8080/v1/libraryevent
```

#### PUT WITH ID

```bash
curl -i \
-d '{"libraryEventId":123,"book":{"bookId":456,"bookName":"Kafka Using Spring Boot","bookAuthor":"Dilip"}}' \
-H "Content-Type: application/json" \
-X PUT http://localhost:8080/v1/libraryevent
```

```bash
curl -i \
-d '{"libraryEventId":999,"book":{"bookId":456,"bookName":"Kafka Using Spring Boot","bookAuthor":"Dilip"}}' \
-H "Content-Type: application/json" \
-X PUT http://localhost:8080/v1/libraryevent
```

```bash
curl -i \
-d '{"libraryEventId":2,"book":{"bookId":456,"bookName":"Kafka Using Spring Boot","bookAuthor":"Dilip"}}' \
-H "Content-Type: application/json" \
-X PUT http://localhost:8080/v1/libraryevent
```

#### PUT WITHOUT ID

```bash
curl -i \
-d '{"libraryEventId":null,"book":{"bookId":456,"bookName":"Kafka Using Spring Boot","bookAuthor":"Dilip"}}' \
-H "Content-Type: application/json" \
-X PUT http://localhost:8080/v1/libraryevent
```

```bash
# PW: springkafka
# Generate keystore
keytool -keystore server.keystore.jks -alias localhost -validity 365 -genkey -keyalg RSA

# Read the keystore
keytool -list -v -keystore server.keystore.jks

# Generating CA
openssl req -new -x509 -keyout ca-key -out ca-cert -days 365 -subj "/CN=local-security-CA"

# Certificate Signing Request(CSR)
keytool -keystore server.keystore.jks -alias localhost -certreq -file cert-file

# Signing the certificate
openssl x509 -req -CA ca-cert -CAkey ca-key -in cert-file -out cert-signed -days 365 -CAcreateserial -passin pass:springkafka

#  Adding the Signed Cert in to the KeyStore file
keytool -keystore server.keystore.jks -alias CARoot -import -file ca-cert
keytool -keystore server.keystore.jks -alias localhost -import -file cert-signed

# Verify we should see 'Your keystore contains 2 entries'
keytool -list -v -keystore server.keystore.jks

# Copy the path into server=*.properties

# listeners=PLAINTEXT://:9093,SSL://:9094
# ssl.truststore.location=<location>/server.keystore.jks
# ssl.keystore.password=springkafka
# ssl.key.password=springkafka
# ssl.endpoint.identification.algorithm=

# Add client-ssl properties

cd kafka/config
touch client-ssl.properties

# security.protocol=SSL
# ssl.truststore.location=<location>/client.truststore.jks
# ssl.truststore.password=password
# ssl.truststore.type=JKS

# Start consumer and producer

./kafka-console-producer.sh --broker-list localhost:9092 --topic first_topic --producer-property acks=all --producer.config ../config/client-ssl.properties
./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic first_topic --from-beginning --consumer.config ../config/client-ssl.properties
```

### Kafka Design patterns

```bash
docker run -d -p 3306:3306 --name mysql-docker-container -e MYSQL_ROOT_PASSWORD=streaming -e MYSQL_DATABASE=streaming -e MYSQL_USER=streaming -e MYSQL_PASSWORD=streaming mysql/mysql-server:latest

./kafka-topics.sh --bootstrap-server localhost:9092 --topic streaming.orders.input --create --partitions 3 --replication-factor 1

./kafka-topics.bat --bootstrap-server localhost:9092 --topic streaming.alerts.critical --create --partitions 3 --replication-factor 1
./kafka-topics.bat --bootstrap-server localhost:9092 --topic streaming.alerts.highvolume --create --partitions 3 --replication-factor 1
./kafka-topics.bat --bootstrap-server localhost:9092 --topic streaming.alerts.input --create --partitions 3 --replication-factor 1
```

```sql
CREATE TABLE streaming.order_summary (
	INTERVAL_TIMESTAMP varchar(100) NOT NULL,
	PRODUCT varchar(100) NOT NULL,
	TOTAL_VALUE DOUBLE NOT NULL,
	ID BIGINT auto_increment NOT NULL,
	CONSTRAINT order_summary_PK PRIMARY KEY (ID)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;
```

```xml
<dependencies>
    <!--Commons-->
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <version>1.18.28</version>
      <scope>provided</scope>
    </dependency>

    <dependency>
      <groupId>commons-io</groupId>
      <artifactId>commons-io</artifactId>
      <version>2.4</version>
    </dependency>

    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
      <version>2.10.0</version>
    </dependency>

    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>gson</artifactId>
      <version>2.8.6</version>
    </dependency>

    <!--Kafka-->
    <dependency>
      <groupId>org.apache.kafka</groupId>
      <artifactId>kafka-streams</artifactId>
      <version>2.6.0</version>
    </dependency>

    <dependency>
      <groupId>org.apache.kafka</groupId>
      <artifactId>kafka-clients</artifactId>
      <version>2.6.0</version>
    </dependency>

    <!--SQL & NOSQL-->
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>8.0.21</version>
    </dependency>

    <dependency>
      <groupId>redis.clients</groupId>
      <artifactId>jedis</artifactId>
      <version>3.3.0</version>
    </dependency>

    <!--Http server-->
    <dependency>
      <groupId>org.apache.httpcomponents</groupId>
      <artifactId>httpclient</artifactId>
      <version>4.5.10</version>
    </dependency>

    <!--Prediction-->
    <dependency>
      <groupId>edu.stanford.nlp</groupId>
      <artifactId>stanford-corenlp</artifactId>
      <version>4.0.0</version>
    </dependency>

    <dependency>
      <groupId>edu.stanford.nlp</groupId>
      <artifactId>stanford-corenlp</artifactId>
      <version>4.0.0</version>
      <classifier>models</classifier>
    </dependency>
</dependencies>
```

#### Streaming analytics

![Analytics](https://raw.githubusercontent.com/matebence/spring/kafka/docs/analytics.png)


```java
public class ClassDeSerializer<T> implements Deserializer<T> {

    private final Gson gson =
            new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();

    private Class<T> destinationClass;
    private Type reflectionTypeToken;

    /** Default constructor needed by Kafka */
    public ClassDeSerializer(Class<T> destinationClass) {
        this.destinationClass = destinationClass;
    }

    public ClassDeSerializer(Type reflectionTypeToken) {
        this.reflectionTypeToken = reflectionTypeToken;
    }

    @Override
    public void configure(Map<String, ?> props, boolean isKey) {}

    @Override
    public T deserialize(String topic, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        Type type = destinationClass != null ? destinationClass : reflectionTypeToken;
        return gson.fromJson(new String(bytes), type);
    }

    @Override
    public void close() {}
}
```

```java
public class ClassSerializer<T> implements Serializer<T> {

    private final Gson gson =
            new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();

    public ClassSerializer() {}

    @Override
    public void configure(Map<String, ?> props, boolean isKey) {}

    @Override
    public byte[] serialize(String topic, T type) {
        return gson.toJson(type).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void close() {}
}
```

```java
public class KafkaOrdersDataGenerator implements Runnable {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_BLUE = "\u001B[34m";

    //Kafka topic to publish to
    public static final String TOPIC = "streaming.orders.input";

    public static void main(String[] args) {
        KafkaOrdersDataGenerator kodg = new KafkaOrdersDataGenerator();
        kodg.run();
    }

    public void run() {
        try {
            System.out.println("Starting Kafka Orders Generator..");
            //Wait for the main flow to be setup.
            Thread.sleep(5000);

            //Setup Kafka Client
            Properties kafkaProps = new Properties();
            kafkaProps.put("bootstrap.servers","localhost:9092");

            kafkaProps.put("key.serializer",
                    "org.apache.kafka.common.serialization.StringSerializer");
            kafkaProps.put("value.serializer",
                    "org.apache.kafka.common.serialization.StringSerializer");

            Producer<String,String> myProducer = new KafkaProducer<>(kafkaProps);

            //Define list of Products
            List<String> products = new ArrayList<String>();
            products.add("Keyboard");
            products.add("Mouse");
            products.add("Monitor");

            //Define list of Prices. Matches the corresponding products
            List<Double> prices = new ArrayList<>();
            prices.add(25.00);
            prices.add(10.5);
            prices.add(140.00);

            //Define a random number generator
            Random random = new Random();

            ObjectMapper mapper = new ObjectMapper();

            //Capture current timestamp
            String currentTime = String.valueOf(System.currentTimeMillis());

            //Create order ID based on the timestamp
            int orderId = (int)Math.floor(System.currentTimeMillis()/1000);

            //Generate 100 sample order records
            for(int i=0; i < 100; i++) {

                SalesOrder so = new SalesOrder();
                so.setOrderId(orderId);
                orderId++;

                //Generate a random product
                int randval=random.nextInt(products.size());
                so.setProduct(products.get(randval));

                //Get product price
                so.setPrice(prices.get(randval));

                //Generate a random value for number of quantity
                so.setQuantity(random.nextInt(4) + 1);

                String recKey = String.valueOf(so.getOrderId());
                String value = mapper.writeValueAsString(so);

                //Create a Kafka producer record
                ProducerRecord<String, String> record =
                        new ProducerRecord<String, String>(
                                TOPIC,
                                recKey,
                                value );

                RecordMetadata rmd = myProducer.send(record).get();

                System.out.println(ANSI_PURPLE +
                        "Kafka Orders Stream Generator : Sending Event : "
                        + String.join(",", value)  + ANSI_RESET);

                //Sleep for a random time ( 1 - 3 secs) before the next record.
                Thread.sleep(random.nextInt(2000) + 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

```java
public class MariaDBManager implements Runnable, Serializable {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_BLUE = "\u001B[34m";

    private Connection conn;

    public static void main(String[] args) {
        System.out.println("Starting MariaDB DB Manager");
        MariaDBManager sqldbm = new MariaDBManager();
        sqldbm.setUp();
        sqldbm.insertSummary("2020-08-20 00:00:00","Mouse",46.00);
        sqldbm.run();
    }

    public void setUp() {
        System.out.println("Setting up MariaDB Connection");
        String url = "jdbc:mysql://localhost:3306/streaming";
        try {
            conn = DriverManager.getConnection(url,"streaming","streaming");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void teardown() {
        try {
            conn.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    //Used by StreamingAnalytics for writing data into MariaDB
    public void insertSummary(String timestamp, String product, Double value) {
        try{
            String sql = "INSERT INTO order_summary "
                    + "(INTERVAL_TIMESTAMP, PRODUCT, TOTAL_VALUE) VALUES "
                    +"( '" + timestamp + "',"
                    +" '" + product + "',"
                    + value + ")";
            //System.out.println(sql);
            conn.createStatement().execute(sql);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    //Use to print 5 second statistics from the MariaDB Database
    @Override
    public void run() {
        try {
            //Find latest ID
            int latestId=0;

            String idQuery="SELECT IFNULL(MAX(ID),0) as LATEST_ID FROM order_summary";
            ResultSet rsLatest = conn.createStatement().executeQuery(idQuery);

            while(rsLatest.next()) {
                latestId = rsLatest.getInt("LATEST_ID");
            }

            //SQL for periodic stats
            String selectSql = "SELECT count(*) as TotalRecords, "
                    + " sum(TOTAL_VALUE) as TotalValue"
                    + " FROM order_summary"
                    + " WHERE ID > " + latestId ;
            System.out.println("Periodic Check Query : " + selectSql);

            while(true) {
                //Sleep for 5 seconds and then query summary
                Thread.sleep(5000);
                ResultSet rs = conn.createStatement().executeQuery(selectSql);

                while (rs.next()) {
                    System.out.println(ANSI_BLUE
                            +"---------------------------------------------------------------\n "
                            +"DB Summary : "
                            + "Records = " + rs.getInt("TotalRecords") + ", "
                            + "Value = " + rs.getDouble("TotalValue") + "\n"
                            +"---------------------------------------------------------------"
                            + ANSI_RESET);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
```

```java
@ToString
@NoArgsConstructor
public class OrderAggregator {

    @Getter
    @Setter
    private Double totalValue = 0.0;

    public OrderAggregator add(Double value) {
        totalValue += value;
        return this;
    }
}
```

```java
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrder {

    @Getter
    @Setter
    private int orderId;

    @Getter
    @Setter
    private String product;

    @Getter
    @Setter
    private int quantity;

    @Getter
    @Setter
    private double price;
}
```

```java
public class StreamingAnalytics {

    public static void main (String args[]) {
        //Initiate MariaDB DB Tracker and start the thread to print
        //summaries every 5 seconds
        MariaDBManager dbTracker = new MariaDBManager();
        dbTracker.setUp();
        Thread dbThread = new Thread(dbTracker);
        dbThread.start();

        //Create another MariaDB Connection to update data
        MariaDBManager dbUpdater = new MariaDBManager();
        dbUpdater.setUp();

        //Initiate the Kafka Orders Generator
        KafkaOrdersDataGenerator ordersGenerator = new KafkaOrdersDataGenerator();
        Thread genThread = new Thread(ordersGenerator);
        genThread.start();

        System.out.println("******** Starting Streaming  *************");
        try {
            final Serde<String> stringSerde = Serdes.String();
            final Serde<Long> longSerde = Serdes.Long();

            final Serde<SalesOrder> orderSerde
                    = Serdes.serdeFrom(new ClassSerializer<>(),
                    new ClassDeSerializer<>(SalesOrder.class));
            final Serde<OrderAggregator> aggregatorSerde
                    = Serdes.serdeFrom(new ClassSerializer<>(),
                    new ClassDeSerializer<>(OrderAggregator.class));

            Properties props = new Properties();
            props.put(StreamsConfig.APPLICATION_ID_CONFIG,
                    "streaming-analytics-pipe");
            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
                    "localhost:9092");
            props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
                    Serdes.String().getClass());
            props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
                    Serdes.String().getClass());
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

            //For immediate results during testing
            props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
            props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 100);

            //Initiate the Kafka Streams Builder
            final StreamsBuilder streamsBuilder = new StreamsBuilder();
            final ObjectMapper objectMapper = new ObjectMapper();

            //Create the source node for Orders
            KStream<String, String> orderConsumer = streamsBuilder.stream(KafkaOrdersDataGenerator.TOPIC, Consumed.with(stringSerde, stringSerde));

            //Convert input json to SalesOrder object using Object Mapper
            KStream<String,SalesOrder> salesOrderObjects = orderConsumer.mapValues((key, value) -> {
                try {
                    return objectMapper.readValue(value, SalesOrder.class);
                } catch (JsonProcessingException e) {
                    System.out.println("ERROR : Cannot convert JSON " + value);
                    return null;
                }
            });

            //Print objects received
            salesOrderObjects.peek((key, value) -> {
               System.out.println("Received Order : " + value);
            });

            //Create a window of 5 seconds
            TimeWindows timeWindows = TimeWindows.of(Duration.ofSeconds(5)).grace(Duration.ZERO);

            //Initializer creates a new aggregator for every
            //Window & Product combination
            Initializer<OrderAggregator> orderAggregatorInitializer = OrderAggregator::new;

            //Aggregator - Compute total value and call the aggregator
            Aggregator<String, SalesOrder, OrderAggregator> orderAggregator = (key, value, aggregator) -> aggregator.add(value.getPrice() * value.getQuantity());

            //Perform Aggregation
            KTable<Windowed<String>,OrderAggregator> productSummary = salesOrderObjects.groupBy((key, value) -> value.getProduct(), Grouped.with(stringSerde, orderSerde))
                    .windowedBy(timeWindows)
                    .aggregate(orderAggregatorInitializer,orderAggregator,  //Store output in a materialized store
                            Materialized.<String, OrderAggregator, WindowStore<Bytes, byte[]>>as("time-windowed-aggregate-store")
                                    .withValueSerde(aggregatorSerde))
                    .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded().shutDownWhenFull()));

            productSummary.toStream() //convert KTable to KStream
                    .foreach( (key, aggregation) -> {
                                System.out.println("Received Summary :" +
                                        " Window = " + key.window().startTime() +
                                        " Product =" + key.key() +
                                        " Value = " + aggregation.getTotalValue());

                                //Write to order_summary table
                                dbUpdater.insertSummary(
                                        key.window().startTime().toString(),
                                        key.key(),
                                        aggregation.getTotalValue()
                                );
                            }
                    );

            //Create final topology and print
            final Topology topology = streamsBuilder.build();
            System.out.println(topology.describe());

            //Setup Stream
            final KafkaStreams streams = new KafkaStreams(topology, props);

            final CountDownLatch latch = new CountDownLatch(1);

            // attach shutdown handler to catch control-c
            Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
                @Override
                public void run() {
                    System.out.println("Shutdown called..");
                    streams.close();
                    latch.countDown();
                }
            });

            //Start the stream
            streams.start();
            //Await termination
            latch.await();
        } catch(Exception e) {
            System.out.println("Exception at" + e);
        }
    }
}
```

#### Streaming alert

![Alert](https://raw.githubusercontent.com/matebence/spring/kafka/docs/alert.png)

```java
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @Getter
    @Setter
    private Timestamp timestamp;

    @Getter
    @Setter
    private String level;

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String mesg;
}
```

```java
public class ClassDeSerializer<T> implements Deserializer<T> {

    private final Gson gson =
            new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();

    private Class<T> destinationClass;
    private Type reflectionTypeToken;

    /** Default constructor needed by Kafka */
    public ClassDeSerializer(Class<T> destinationClass) {
        this.destinationClass = destinationClass;
    }

    public ClassDeSerializer(Type reflectionTypeToken) {
        this.reflectionTypeToken = reflectionTypeToken;
    }

    @Override
    public void configure(Map<String, ?> props, boolean isKey) {}

    @Override
    public T deserialize(String topic, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        Type type = destinationClass != null ? destinationClass : reflectionTypeToken;
        return gson.fromJson(new String(bytes), type);
    }

    @Override
    public void close() {}
}
```

```java
public class ClassSerializer<T> implements Serializer<T> {

    private final Gson gson =
            new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();

    public ClassSerializer() {}

    @Override
    public void configure(Map<String, ?> props, boolean isKey) {}

    @Override
    public byte[] serialize(String topic, T type) {
        return gson.toJson(type).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void close() {}
}
```

```java
public class KafkaAlertsDataGenerator implements Runnable {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public static final String TOPIC = "streaming.alerts.input";
    public static final String TOPIC_CRITICAL = "streaming.alerts.critical";

    public static void main(String[] args) {
        KafkaAlertsDataGenerator kodg = new KafkaAlertsDataGenerator();
        kodg.run();
    }

    public void run() {
        try {

            System.out.println("Starting Kafka Alerts Generator..");
            //Wait for the main flow to be setup.
            Thread.sleep(5000);

            //Setup Kafka Client
            Properties kafkaProps = new Properties();
            kafkaProps.put("bootstrap.servers","localhost:9092");

            kafkaProps.put("key.serializer",
                    "org.apache.kafka.common.serialization.StringSerializer");
            kafkaProps.put("value.serializer",
                    "org.apache.kafka.common.serialization.StringSerializer");

            Producer<String,String> myProducer
                    = new KafkaProducer<>(kafkaProps);

            //Define list of Exception Levels
            List<String> levels = new ArrayList<>();
            levels.add("CRITICAL");
            levels.add("HIGH");
            levels.add("ELEVATED");

            //Define list of exception codes
            List<String> codes = new ArrayList<>();
            codes.add("100");
            codes.add("200");
            codes.add("300");
            codes.add("400");

            //Define a random number generator
            Random random = new Random();

            //Create a record key using the system timestamp.
            int recKey = (int)Math.floor(System.currentTimeMillis()/1000);

            //Generate 100 sample exception messages
            for(int i=0; i < 100; i++) {

                recKey++;

                //Capture current timestamp
                Timestamp currTimeStamp = new Timestamp(System.currentTimeMillis());
                //Get a random Exception Level
                String thisLevel = levels.get(random.nextInt(levels.size()));
                //Get a random Exception code
                String thisCode = codes.get(random.nextInt(codes.size()));

                //Form a CSV. Use a dummy exception message
                String value= "\"" + currTimeStamp + "\","
                        +  "\"" + thisLevel + "\","
                        +  "\"" + thisCode + "\","
                        +  "\"This is a " + thisLevel + " alert\"" ;

                System.out.println(value);

                //Create the producer record
                ProducerRecord<String, String> record =
                        new ProducerRecord<>(
                                TOPIC,
                                String.valueOf(recKey),
                                value);

                //Send data to Kafka
                RecordMetadata rmd = myProducer.send(record).get();

                System.out.println(ANSI_PURPLE +
                        "Kafka Orders Stream Generator : Sending Event : "
                        + String.join(",", value)  + ANSI_RESET);

                //Sleep for a random time ( 1 - 2 secs) before the next record.
                Thread.sleep(random.nextInt(1000) + 1000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

```java
public class StreamingThresholdsAndAlerts {

    public static void main(String args[]) {
        //Initiate the Kafka Alerts Generator
        KafkaAlertsDataGenerator alertsGenerator = new KafkaAlertsDataGenerator();
        Thread genThread = new Thread(alertsGenerator);
        genThread.start();

        System.out.println("******** Starting Streaming  *************");

        try {
            //Setup Serializer / DeSerializer for used Data types
            final Serde<Long> longSerde = Serdes.Long();
            final Serde<String> stringSerde = Serdes.String();
            final Serde<Alert> alertSerde = Serdes.serdeFrom(new ClassSerializer<>(), new ClassDeSerializer<>(Alert.class));

            //Setup Properties for the Kafka Input Stream
            Properties props = new Properties();
            props.put(StreamsConfig.APPLICATION_ID_CONFIG,
                    "alerts-and-thresholds-pipe");
            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
                    "localhost:9092");
            props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
                    Serdes.String().getClass());
            props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
                    Serdes.String().getClass());
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

            //For immediate results during testing
            props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
            props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 100);

            //Initiate the Kafka Streams Builder
            final StreamsBuilder streamsBuilder = new StreamsBuilder();

            //Create the source node for Alerts
            KStream<String, String> alertInput = streamsBuilder.stream(KafkaAlertsDataGenerator.TOPIC, Consumed.with(stringSerde, stringSerde));

            //Convert value to an Alert Object
            KStream<String, Alert> alertObject = alertInput.mapValues(value -> {
                String[] values = value
                        .replaceAll("\"","")
                        .split(",");

                Alert alert = new Alert();

                alert.setTimestamp(Timestamp.valueOf(values[0]));
                alert.setLevel(values[1]);
                alert.setCode(values[2]);
                alert.setMesg(values[3]);

                System.out.println("Received Alert :" + alert);

                return alert;
            });

            //Filter Critical Alerts and Publish to an outgoing topic
            alertObject.filter((key, value) -> value.getLevel().equals("CRITICAL"))
                    .mapValues(alert -> "\"" + alert.getTimestamp() + "\"," +
                            "\"" + alert.getCode() + "\"," +
                            "\"" + alert.getMesg() + "\"")
                    .to(KafkaAlertsDataGenerator.TOPIC_CRITICAL);

            //Create a tumbling window of 10 seconds
            TimeWindows tumblingWindow = TimeWindows
                    .of(Duration.ofSeconds(10))
                    .grace(Duration.ZERO);

            //Aggregate by Code and window
            KTable<Windowed<String>,Long> codeCounts
                    = alertObject.groupBy( //Group by Code
                            (key,value) -> value.getCode(),
                            Grouped.with(stringSerde,alertSerde))
                    .windowedBy(tumblingWindow)
                    .count(Materialized.as("code-counts")) //Count Records
                    .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded().shutDownWhenFull()));

            codeCounts.toStream()
                    .peek((key,value) -> System.out.println("Summary record :" + key + " = " + value))
                    .filter((key, value)  -> value > 2) //Filter for high volume alerts
                    .map(  //Convert key and value to String for publishing
                            new KeyValueMapper<Windowed<String>,
                                    Long, KeyValue<String, String>>() {
                                @Override
                                public KeyValue<String, String>
                                apply(Windowed<String> key, Long value) {

                                    String returnKey = key.toString();
                                    String returnVal = "\"" + key.window().startTime() + "\"," +
                                            "\"" + key.key() + "\"," +
                                            "\"" + value.toString() + "\"";

                                    System.out.println("High Volume Alert : "
                                            + returnVal);
                                    return new KeyValue<>(returnKey, returnVal);
                                }
                            }
                    ).to("streaming.alerts.highvolume"); //Publish to outgoing topic.

            /**************************************************
             * Create a pipe and execute
             **************************************************/
            // Create final topology and print
            final Topology topology = streamsBuilder.build();
            System.out.println(topology.describe());

            // Setup Stream
            final KafkaStreams streams = new KafkaStreams(topology, props);

            // Reset for the example. Not recommended for production
            final CountDownLatch latch = new CountDownLatch(1);

            // attach shutdown handler to catch control-c
            Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
                @Override
                public void run() {
                    System.out.println("Shutdown called..");
                    streams.close();
                    latch.countDown();
                }
            });

            //Start the stream
            streams.start();
            //Await termination
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### Transition to Spring

```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
    <version>2.7.8</version>
</dependency>
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-streams</artifactId>
    <version>2.7.1</version>
</dependency> 
```

```java
@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    KafkaStreamsConfiguration kStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(APPLICATION_ID_CONFIG, "streams-app");
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        return new KafkaStreamsConfiguration(props);
    }

    // other config
}
```

```java
@Component
public class WordCountProcessor {

    private static final Serde<String> STRING_SERDE = Serdes.String();

    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder) {
        KStream<String, String> messageStream = streamsBuilder
          .stream("input-topic", Consumed.with(STRING_SERDE, STRING_SERDE));

        KTable<String, Long> wordCounts = messageStream
          .mapValues((ValueMapper<String, String>) String::toLowerCase)
          .flatMapValues(value -> Arrays.asList(value.split("\\W+")))
          .groupBy((key, word) -> word, Grouped.with(STRING_SERDE, STRING_SERDE))
          .count();

        wordCounts.toStream().to("output-topic");
    }
}
```