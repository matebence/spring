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
