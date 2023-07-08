## Setting up MySQL Container

```bash
docker run -d -p 3306:3306 --name mysql-docker-container -e MYSQL_ROOT_PASSWORD=spring -e MYSQL_DATABASE=spring -e MYSQL_USER=spring -e MYSQL_PASSWORD=spring mysql/mysql-server:latest
```

```sql
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(10,0) NOT NULL,
  `placed_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf32;
```

## Concurrency and Parallelims API's in Java
	
- Java 1 (Single Core)
    - Threads API
- Java 5 (MultiCore)
    - ThreadPool, ExecutorService, Futures, ConcurrentCollections etc...
    - Deadlock issues
- Java 7 (MultiCore)
    - Data Parallelism
    - Forj/Join Framework
- Java 8 (MultiCore)
    - Lambda Stream API
    - Parallel Stream Completable Futures
- Java 9(Flow Api)
    - Publish Subscribe logic

## Terminology

- Concurrency is a concept where two or more task can run simultaneously
- Parrallelism is a concept in which tasks are literally going to run in parrallel

## Thread

A thread is a separate flow of execution. A thread is capable of performing the tasks that we have assigned to it independently. A Java program can have multiple threads to perform multi-tasking. Threads are independent, meaning When a Java program uses multi-threads, if an error occurs in one thread, it will not affect other threads. each thread will perform the tasks assigned to it simultaneously.

- By default programing languages are sequential
- In single threaded application these method will be called one by one	
- Multithreading is the ability of the CPU to execute multiple processes or thread concurrently

**Time slicing algorithm** -  Processing time for a single core is shared among proecsses and threads. This is called time-slicing

		_____			_____			_____			thread #1
	_____		_____			_____					thread #2


|Process based                                                                                                                                     |Thread based                                                                  |
|--------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------|
|Example: Operating system                                                                                                                         |Example: A Java Application whihc uses 2 threads                              |
|An operating system can handle multiple processes at the same time                                                                                |A Java Application is capable to handle multiple threads simultaneously       |
|Each process uses a seprate memory area                                                                                                           |All thread of an application is belongs to one process                        |
|Processes are independent of other processes, meaning if we change anything in one process, it will not affect other processes                    |We can call a thread as sub-process                                           |
|Example: We can perform three tasks(Processes simultaneously, such as writing a blog while listening to music and file download in the background)|Threads belongs to one application share the same memory area                 |
|-                                                                                                                                                 |Threads are independent of other threds                                       |
|-                                                                                                                                                 |Exmaple: a thread can access a databse while other threads is sorting an array|

![Threads VS Processes in OS](https://raw.githubusercontent.com/matebence/spring/parallel/docs/threads_vs_process_in_os.webp)

Memory management
- **Stack memory** - the local variables and method arguments as well as method call are stored on the stack
- **Heap memory** - objects are stored on the heap memory and live as long as it is references from somewhere in the application

**Every thread has its own stack memory but all threads share the HEAP memory**

### Purpose of using a Thread

Consider the following scenario, an application must perform 10 tasks. If it takes 10 seconds to complete the execution using 1 thread, Can you reduce the execution time to 1 second by using 10 threads?

It depends on the implementation. If those tasks are independent of each other, Meaning no tasks wait until another task to complete, then we can assign a task per thread. Therefore those 10 tasks can execute parallel at the same time and we can cut off some time.

But if those tasks are dependent on other tasks, Meaning if a task should wait for other tasks to complete its execution, then we can’t execute those dependent tasks parallelly. Therefore we can’t reduce the time by simply increase the number of threads.

### Thread life cycle

![Thread life cycle](https://raw.githubusercontent.com/matebence/spring/parallel/docs/thread_lifecycle.webp)

- **New state** - Before calling the start() method on the newly created thread instance, the thread is in a “new state”.
- *Runnable state** - The thread is in a “runnable state” after calling the start() method, but it has not been selected as the running thread by the thread scheduler.
- **Running state** - If the thread scheduler has selected it as a running thread, it is on the “running state”. A thread will only execute its task when it is on the “running state”.
- **Waiting state** - When a thread is still alive but not eligible to run, then the thread is in the “waiting state”. A thread can move from “running state” to “waiting state” by certain conditions ex: sleep(), wait(). Same as that a thread can move from “waiting state” to “runnable state” by certain conditions ex: notify(), interrupt().
- **Death state** - When a thread completes all of the tasks assigned to it, or when its run() method exits, it will become a dead thread.

### Thread scheduler

Just execute the above code multiple times. For each execution you get different output, right? It happens because of the order of execution. In multi-threading “Thread scheduler” decides which thread should execute first and which is next. Thread scheduler decides the execution order based on the mechanism which is specified in its JRE. Different JRE can have different mechanism to decide the order of execution.

### Thread Priority

In multi-threading “Thread scheduler” assigns a thread to a processor depending on thread’s priority. The thread with the highest priority would have a chance to execute before the others.

The main thread’s priority is set to 5 by default. When we create new threads, they will inherit the priority of their parent thread.

### Types of Thread

|Non-Daemon threads               |Daemon thread                       |
|---------------------------------|------------------------------------|
|High priority threads            |Low prioty threads                  |
|Always run on foreground         |Run on background                   |
|Designed to do some specific task|Designed to support the user threads|

### Important methods and keywords

- .join() — the current thread waits for the specified thread to die
- .yield() - the yield method gives a hint to the scheduler that the current thread is willing to give chance to other thread
- .sleep() - the current running thread will go to sleep (wait state) for the specified time
- .interrupt() - this method can interrupt a thread which is in waiting state ( sleep() , wait() , join() )
- .wait() - if we invoke the wait() method, current thread will release the lock and go to Wait state. It will come back to Runnable state when either another thread invokes the notify() method or the notifyAll() method for this object
- notify() - when we invoke notify() method, one of the thread that waiting on the specific object will come back to Runnable state
- synchronize - if we use synchronize method, only one thread at a time can access the method within the same object. When a thread invokes a synchronized method, the object of the method is automatically get locked and released when the thread completes its task.

### Implementing options

- extend **Thread** class
- implement **Runnable** interface
- implement **Callable** interface (it has a return value)

```java
public class HeavyTask extends Thread {

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
            System.out.println("This is a long running task (HeavyTask)");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

public class ComplexTask implements Runnable {

    @Override
    public void run() {
        try {
            Thread.yield();
            Thread.sleep(10000);
            System.out.println("This is a long running task (ComplexTask)");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

public class LongTask implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        Thread.sleep(2000);
        return new Integer(21);
    }
}

public class App {

    public static void main(String args[]) throws InterruptedException {
        Runnable runnable = new ComplexTask();
        Thread first = new Thread(runnable);
        Thread second = new HeavyTask();

        System.out.println("Executing ComplexTask");
        first.start();
        // first.join(); it waits for the first thread to die
        System.out.println("Executing HeavyTask");
        second.start();
        second.wait();

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Callable<Integer> callable = new LongTask();
        Future<Integer> integerFuture =  executorService.submit(callable);
        integerFuture.get(2, TimeUnit.SECONDS);
        executorService.shutdown();
    }
}
```


```java
public class JavaInterruptExp2 extends Thread {
    public void run() {
        try {
            //Here current threads goes to sleeping state  
            // Another thread gets the chance to execute  
            Thread.sleep(500);
            System.out.println("javatpoint");
        } catch (InterruptedException e) {
            System.out.println("Exception handled " + e);
        }
        System.out.println("thread is running...");
    }

    public static void main(String args[]) {
        JavaInterruptExp2 t1 = new JavaInterruptExp2();
        JavaInterruptExp2 t2 = new JavaInterruptExp2();
        // call run() method   
        t1.start();
        // interrupt the thread   
        t1.interrupt();
    }
}     
```

```java
class Notify1 extends Thread {
    public void run() {
        synchronized (this) {
            System.out.println("Starting of " + Thread.currentThread().getName());
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "...notified");
        }
    }
}

class Notify2 extends Thread {
    Notify1 notify1;

    Notify2(Notify1 notify1) {
        this.notify1 = notify1;
    }

    public void run() {
        synchronized (this.notify1) {
            System.out.println("Starting of " + Thread.currentThread().getName());
            try {
                this.notify1.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "...notified");
        }
    }
}

class Notify3 extends Thread {
    Notify1 notify1;

    Notify3(Notify1 notify1) {
        this.notify1 = notify1;
    }

    public void run() {
        synchronized (this.notify1) {
            System.out.println("Starting of " + Thread.currentThread().getName());
            // call the notify() method  
            this.notify1.notify();
            System.out.println(Thread.currentThread().getName() + "...notified");
        }
    }
}

public class JavaNotifyExp {
    public static void main(String[] args) throws InterruptedException {
        Notify1 notify1 = new Notify1();
        Notify2 notify2 = new Notify2(notify1);
        Notify3 notify3 = new Notify3(notify1);

        // creating the threads   
        Thread t1 = new Thread(notify1, "Thread-1");
        Thread t2 = new Thread(notify2, "Thread-2");
        Thread t3 = new Thread(notify3, "Thread-3");

        // call run() method  
        t1.start();
        t2.start();
        Thread.sleep(100);
        t3.start();
    }
} 
```

Problems with synchronization

- every object in Java has a so-called intrinsic lock
- a thread that needs exclusive and consistent access to an objects fields has to acquire the objects intrinsic lock before accessing them, and then release the intrinsic lock when its done
- the lock is put on the Object in our case App.class (we should prefer the sync block)

    The problem is that every object has a single monitor lock. If we have 2 independent synchronized method than thee threads have to wait for each other to release the lock

```java
public class App {

    private static int counter1 = 0;
    private static int counter2 = 0;

    public static synchronized void increment1() {
        //synchronized(this)
        synchronized(App.class) {
            counter1++;
        }
    }

    public static synchronized void increment2() {
        //synchronized(this)
        synchronized(App.class) {
            counte21++l
        }
    }

    public static void process() {
        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i< 100; i++) {
                    increment1();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i< 100; i++) {
                    increment2();
                }
            }
        });

        t1.start()
        t2.start()
        t1.join()
        t2.join()

        System.out.println("The coutner is" + counter)
    }

    public static void main(String args[]) {
        process()
    }
}
```

Solving the problem with two independent sync block

```java
public class App {

    private static int counter1 = 0;
    private static int counter2 = 0;

    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static synchronized void increment1() {
        //synchronized(this)
        synchronized(lock1) {
            counter1++;
        }
    }

    public static synchronized void increment2() {
        //synchronized(this)
        synchronized(lock2) {
            counte21++l
        }
    }

    public static void process() {
        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i< 100; i++) {
                    increment1();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i< 100; i++) {
                    increment2();
                }
            }
        });

        t1.start()
        t2.start()
        t1.join()
        t2.join()

        System.out.println("The coutner is" + counter)
    }

    public static void main(String args[]) {
        process()
    }
}
```

### Common tread issues

RetrantLock, it has the same behvaior as the synchronized approach. Of course it has some additional features

```java
new ReetrantLock(boolean fairness)
```

If the fairness parameter is set to be TRUE then the longest waiting thread will get the lock

- **Starvation** - is said to occur when a particular thread does not get access to the object or the resource which leads to an increase in waiting and execution time.

```java
public class Worker implements Runnable {

    //it will be stored in the main memory
    private volatile boolean terminated;

    @Override
    public void run() {
        while(!isterminated) {
            System.out.println("Worker is running");
            Thread.sleep(3000);
        }
    }

    public boolean isTerminated() {
        return terminated;
    }

    public void setTerminated(boolean terminated) {
        this.terminated = terminated;
    }
}

public class Volatile {

    public static void main(String args[]) {
        Worker worker = new Worker();
        Thread t1 = new Thread(worker)
        t1.start();

        worker.setTerminated(true)

    }
}
```

- **Deadlock** - occurs when two or more threads wait forever for a lock or resource held by another of the threads

```java
public class Deadlock {

    private Lock lock1 = new ReetrantLock(true)
    private Lock lock2 = new ReetrantLock(true)

    public void worker1() {
        lock1.lock();
        System.out.println("Worker1 acquires the lock1...");

        try {
            Thread.sleep(300)
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        lock2.lock();
        System.out.println("Worker1 acquired the lock2...")

        lock1.unlock();
        lock2.unlock();
    }

    public void worker1() {
        lock2.lock();
        System.out.println("Worker1 acquires the lock2...");

        try {
            Thread.sleep(300)
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        lock1.lock();
        System.out.println("Worker1 acquired the lock1...")

        lock1.unlock();
        lock2.unlock();
    }

    public static void main(String args[]) {

        Deadlock deadlock = new Deadlock();
        new Thread(deadlock::worker1, "worker1").start();
    }
}
```

- **Livelock** - threads are unable to make fursther progress. However, the threads are not blocked: they simpley to busy responding to each other to resume work

```java
public class LiveLock {

    private Lock lock1 = new ReetrantLock(true);
    private Lock lock2 = new ReetrantLock(true);

    public void worker1() {
        while(true) {
            lock1.tryLock(50, TimeUnit.MILLISECONDS);
            System.out.println("Worker1 acquires the lock1...");

            if (lock2.tryLock()) {
                System.out.println("Worker1 acquires the lock2...");
                lock2.unlock()
            } else {
                System.out.println("Worker1 can not acquires the lock2...");
            }
            break;
        }
        lock1.unlock()
        lock2.unlock()
    }

    public void worker2() {
        while(true) {
            lock2.tryLock(50, TimeUnit.MILLISECONDS);
            System.out.println("Worker1 acquires the lock1...");

            if (lock1.tryLock()) {
                System.out.println("Worker2 acquires the lock2...");
                lock1.unlock()
            } else {
                System.out.println("Worker2 can not acquires the lock2...");
            }
            break;
        }
        lock1.unlock()
        lock2.unlock()

    }

    public static void main(String args[]) {

        LiveLock livelock = new LiveLock();
        new Thread(livelock::worker1, "worker1").start();
    }
}
```

### Atomic Variables

```java
public class Counter {
    int counter; 
 
    public void increment() {
        counter++;
    }
}
```

In the case of a single-threaded environment, this works perfectly; however, as soon as we allow more than one thread to write, we start getting inconsistent results.

This is because of the simple increment operation (counter++), which may look like an atomic operation, but in fact is a combination of three operations: obtaining the value, incrementing, and writing the updated value back.

**Atomic Operations**

There is a branch of research focused on creating non-blocking algorithms for concurrent environments. These algorithms exploit low-level atomic machine instructions such as compare-and-swap (CAS), to ensure data integrity.

A typical CAS operation works on three operands:
- The memory location on which to operate (M)
- The existing expected value (A) of the variable
- The new value (B) which needs to be set

The CAS operation updates atomically the value in M to B, but only if the existing value in M matches A, otherwise no action is taken.
In both cases, the existing value in M is returned. This combines three steps – getting the value, comparing the value, and updating the value – into a single machine level operation.


The most commonly used atomic variable classes in Java are AtomicInteger, AtomicLong, AtomicBoolean, and AtomicReference. These classes represent an int, long, boolean, and object reference respectively which can be atomically updated. The main methods exposed by these classes are:
- get() – gets the value from the memory, so that changes made by other threads are visible; equivalent to reading a volatile variable
- incrementAndGet() – Atomically increments by one the current value
- set() – writes the value to memory, so that the change is visible to other threads; equivalent to writing a volatile variable
- lazySet() – eventually writes the value to memory, maybe reordered with subsequent relevant memory operations. One use case is nullifying references, for the sake of garbage collection, which is never going to be accessed again. In this case, better performance is achieved by delaying the null volatile write
- compareAndSet() – same as described in section 3, returns true when it succeeds, else false
- weakCompareAndSet() – same as described in section 3, but weaker in the sense, that it does not create happens-before orderings. This means that it may not necessarily see updates made to other variables. As of Java 9, this method has been deprecated in all atomic implementations in favor of weakCompareAndSetPlain(). The memory effects of weakCompareAndSet() were plain but its names implied volatile memory effects. To avoid this confusion, they deprecated this method and added four methods with different memory effects such as weakCompareAndSetPlain() or weakCompareAndSetVolatile()

```java
public class SafeCounterWithoutLock {
    private final AtomicInteger counter = new AtomicInteger(0);
    
    int getValue() {
        return counter.get();
    }
    
    void increment() {
        counter.incrementAndGet();
    }
}
```

### Creating Threads with executors

Wit the increase in the number of cores avaible in the processor nowadays multithreading is getting more and more crucial. Java provides its own multi threading framework the so called Executor Framework. Why to use it ?? Creating and managing threads is expensive.

    Threads pools can reuse threads in an extremely efficent manner by keeping the threads alive and reusing them

- SingleThreadExecutor - This executor has a single thread so we can execute processes in a sequential manner. Every process is executed by a new thread
- ScheduledExecutor - We can execute a given operation at regular intervals or we can use this executor if we wish to delay a certain task
- FixedThreadPool(n) - This is how we can create a thread pool with n threads. Useally n is the number of cores in the CPU
- CachedThreaddPool - The number of threads is not bounded. If all the threads are busy executing some tasks and a new task comes the pool will create and add a new thread to the executor

```java
public class App {

    public static void main(String args[]) {
        List<Integer> list = new ArrayList<>();
        final Object lock = new Object();

        Consumer consumer = new Consumer(list, lock);
        Producer producer = new Producer(list, lock);

        consumer.setProducer(producer);
        producer.setConsumer(consumer);

         Executor executor = Executors.newFixedThreadPool(2);
        // Executor executor = Executors.newScheduledThreadPool(2);
        // Executor executor = Executors.newSingleThreadExecutor();
        // Executor executor = Executors.newCachedThreadPool();

        executor.execute(producer);
        executor.execute(consumer);

        executor.shutdown();
        try {
            if (!executor.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterrruptedException e) {
            executor.shutdownNow();
        }
        
        // new Thread(producer).start();
        // new Thread(consumer).start();
    }
}
```

### Concurrent collection

- **CountDown latch** - a single thread can wait for other threads to fnish with their operations. They have to be independent tasks.

```java
public class ComplexTask implements Runnable {

    private final CountDownLatch countDownLatch;

    public ComplexTask(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Counting down");
        countDownLatch.countDown();
    }
}

public class LongTask implements Runnable {

    private final CountDownLatch countDownLatch;

    public LongTask(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Counting down");
        countDownLatch.countDown();
    }
}

public class App {

    public static void main(String args[]) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        Runnable complextTask = new ComplexTask(countDownLatch);
        Runnable longTask = new LongTask(countDownLatch);

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(complextTask);
        executorService.execute(longTask);

		// if the latch is not 0 we will wait here
        countDownLatch.await();
        System.out.println("DONE");
    }
}
```

- **CyclicBarrier** - multiple threads can wait for each other

```java
public class LongTask implements Runnable {

    private final CyclicBarrier cyclicBarrier;

    public LongTask(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
            System.out.println("Long task is done");
            cyclicBarrier.await();
        } catch (BrokenBarrierException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

public class ComplexTask implements Runnable {

    private final CyclicBarrier cyclicBarrier;

    public ComplexTask(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(6000);
            System.out.println("ComplexTask is done");
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}

public class App {

    public static void main(String args[]) {
        final Integer NUMBER_OF_THREADS = 2;

        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(NUMBER_OF_THREADS, () -> {
            System.out.println("Everything is done");
        });

        Runnable complexTask = new ComplexTask(cyclicBarrier);
        Runnable longTask = new LongTask(cyclicBarrier);

        executorService.execute(complexTask);
        executorService.execute(longTask);
        executorService.shutdown();
    }
}
```

- **BlokingQuueue** - we wait until at least one is removed when we have full capacity

```java
public class FirstWorker implements Runnable {

    private final BlockingQueue<Integer> queueu;

    public FirstWorker(BlockingQueue<Integer> queueu) {
        this.queueu = queueu;
    }

    @Override
    public void run() {
        int counter = 0;
        while(true) {
            try {
                queueu.put(counter);
                counter++;
                Thread.sleep(6000);
                System.out.println("Putting " + counter);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class SecondWorker implements Runnable {

    private final BlockingQueue<Integer> queueu;

    public SecondWorker(BlockingQueue<Integer> queueu) {
        this.queueu = queueu;
    }

    @Override
    public void run() {
        int counter = 0;
        while(true) {
            try {
                counter = queueu.take();
                Thread.sleep(3000);
                System.out.println("Taking " + counter);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class App {

    public static void main(String args[]) {
        //we wait until at least one is removed when we have full capacity 10/10
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
        FirstWorker firstWorker = new FirstWorker(queue);
        SecondWorker secondWorker = new SecondWorker(queue);

        new Thread(firstWorker).start();
        new Thread(secondWorker).start();
    }
}
```

**DelayQueue** - keeps the element internally until a certain delay has expired

```java
public class DelayWorker implements Delayed {

    private final String message;
    private final long duration;

    public DelayWorker(String message, long duration) {
        this.message = message;
        this.duration = System.currentTimeMillis() + duration;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long diff = duration - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed obj) {
        return Long.compare(this.duration, ((DelayWorker) obj).getDuration());
    }

    public long getDuration() {
        return duration;
    }

    public String getMessage() {
        return message;
    }
}

public class App {

    public static void main(String args[]) throws InterruptedException {
        BlockingQueue<DelayWorker> queue = new DelayQueue<>();

        try {
            queue.put(new DelayWorker("This is the first message ...", 2000));
            queue.put(new DelayWorker("This is the second message ...", 10000));
            queue.put(new DelayWorker("This is the third message ...", 4500));
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(!queue.isEmpty()) {
            System.out.println(queue.take().getMessage());
        }
    }
}
```

**PriorityBlockingQueue** - it uses the same ordering rules as the PriorityQueue class and we have to implement the Comparable interface

```java
public class Person implements Comparable<Person> {

    private int age;
    private String name;

    public Person() {
    }

    public Person(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Person o) {
        return name.compareTo(o.getName());
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}

public class FirstWorker implements Runnable {

    private PriorityBlockingQueue<Person> queue;

    public FirstWorker(PriorityBlockingQueue<Person> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
            System.out.println(queue.take());
            System.out.println(queue.take());
            Thread.sleep(1000);
            System.out.println(queue.take());
            Thread.sleep(2000);
            System.out.println(queue.take());
            System.out.println(queue.take());
            System.out.println(queue.take());
            System.out.println(queue.take());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

public class SecondWorker implements Runnable {

    private final PriorityBlockingQueue<Person> queue;

    public SecondWorker(PriorityBlockingQueue<Person> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            queue.put(new Person(5, "E"));
            queue.put(new Person(4, "D"));
            queue.put(new Person(3, "C"));
            Thread.sleep(2000);
            queue.put(new Person(2, "B"));
            Thread.sleep(1000);
            queue.put(new Person(1, "A"));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

public class App {

    public static void main(String args[]) {
        PriorityBlockingQueue<Person> queue = new PriorityBlockingQueue<>();

        FirstWorker first = new FirstWorker(queue);
        SecondWorker second = new SecondWorker(queue);

        new Thread(first).start();
        new Thread(second).start();
    }
}
```

- **Concurrent map** - (if we use the map only for reading, this is not needed)

```java
public class FirstWorker implements Runnable {

    private final ConcurrentMap<String, Integer> map;

    public FirstWorker(ConcurrentMap<String, Integer> map) {
        this.map = map;
    }

    @Override
    public void run() {
        try {
            map.put("B", 12);
            Thread.sleep(1000);
            map.put("Z", 5);
            map.put("A", 25);
            Thread.sleep(2000);
            map.put("D", 25);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

public class SecondWorker implements Runnable {

    private ConcurrentMap<String, Integer> map;

    public SecondWorker(ConcurrentMap<String, Integer> map) {
        this.map = map;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
            System.out.println(map.get("A"));
            Thread.sleep(2000);
            System.out.println(map.get("Z"));
            System.out.println(map.get("B"));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

public class App {

    public static void main(String args[]) {
        ConcurrentMap<String, Integer> map = new ConcurrentHashMap<>();

        Runnable first = new FirstWorker(map);
        Runnable second = new SecondWorker(map);

        new Thread(first).start();
        new Thread(second).start();
    }
}
```

- **CopyOnWriteArray** - thread that change the value in the list make copy of the list. This is how update will be atomic - threads must wait for each other to update the list. (if we use the array only for reading, this is not needed)

```java
public class WriteTask implements Runnable {

    private final List<Integer> list;
    private final Random random;

    public WriteTask(List<Integer> list) {
        this.list = list;
        this.random = new Random();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list.set(random.nextInt(list.size()), random.nextInt(10));
        }
    }
}

public class ReadTask implements Runnable {

    private final List<Integer> list;

    public ReadTask(List<Integer> list) {
        this.list = list;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(list.get(list.size() - 1));
        }
    }
}

public class App {

    public static void main(String args[]) {
        List<Integer> list = new CopyOnWriteArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0));

        Thread t1 = new Thread(new WriteTask(list));
        Thread t2 = new Thread(new WriteTask(list));
        Thread t3 = new Thread(new WriteTask(list));
        Thread t4 = new Thread(new ReadTask(list));

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
```

- **Exchanger** - sharing information between threads

```java
public class FirstWorker implements Runnable {

    private int counter;
    private final Exchanger<Integer> exchanger;

    public FirstWorker(Exchanger<Integer> exchanger) {
        this.counter = 0;
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        try {
            while (true) {
                counter--;
                counter = exchanger.exchange(counter);
                System.out.println(counter);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

public class SecondWorker implements Runnable {

    private int counter;
    private final Exchanger<Integer> exchanger;

    public SecondWorker(Exchanger<Integer> exchanger) {
        this.counter = 0;
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        try {
            while (true) {
                counter++;
                counter = exchanger.exchange(counter);
                System.out.println(counter);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

public class App {

    public static void main(String args[]) {
        Exchanger<Integer> exchanger = new Exchanger<>();
        Runnable firstWorker = new FirstWorker(exchanger);
        Runnable secondWorker = new SecondWorker(exchanger);

        new Thread(firstWorker).start();
        new Thread(secondWorker).start();
    }
}
```

## Fork/Join Framework

```java
public class DataSet {
    
    public static List<String> namesList() {
        return List.of("Bob", "Jamie", "Jill", "Rick");
    }
}

public class ForkJoinUsingRecursion extends RecursiveTask<List<String>> {

    private List<String> inputList;

    public ForkJoinUsingRecursion(List<String> inputList) {
        this.inputList = inputList;
    }

    /**
     * Recursively split the list and runs each half as a ForkJoinTask
     * Right way of using Fork/Join Task
     */
    @Override
    protected List<String> compute() {

        //trivial conditions
        if (this.inputList.size() <= 1) {
            List<String> resultList = new ArrayList<>();
            inputList.forEach(name -> resultList.add(transform(name)));
            return resultList;
        }

        int midPoint = inputList.size() / 2;
        //left side of the list
        ForkJoinTask<List<String>> leftInputList = new ForkJoinUsingRecursion(inputList.subList(0, midPoint)).fork(); // 1. asynchronously arranges this task in the deque,
        inputList = inputList.subList(midPoint, inputList.size()); //right side of the list

        //this is where recursion happens
        List<String> rightResult = compute();
        List<String> leftResult = leftInputList.join();

        leftResult.addAll(rightResult);
        return leftResult;
    }
    
    private String transform(String name) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return name.length() + " - " + name;
    }
}

@Slf4j
public class App {

    private final static StopWatch stopWatch = new StopWatch("App");

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinUsingRecursion forkJoinExampleUsingRecursion = new ForkJoinUsingRecursion(DataSet.namesList());
        stopWatch.start();

        // Start things running and get the result back, This is blocked until the results are calculated.
        // invoke -> Add the task to the shared queue from which all the other qu
        List<String> resultList = forkJoinPool.invoke(forkJoinExampleUsingRecursion);

        log.info("resultList : " + resultList);
        // 11:42:58.105 [main] INFO com.bence.mate.App -- resultList : [3 - Bob, 5 - Jamie, 4 - Jill, 4 - Rick]
        // 11:42:58.118 [main] INFO com.bence.mate.App -- Total time taken : 540

        stopWatch.stop();
        log.info("Total time taken : " + stopWatch.getTime());
    }
}
```

## Parallel Streams

Replacment of Fork/Join. Allows your code to run in parallel. ParallelStream are designed to solve Data Parallelism.

- sequential() -> executes the stream in sequential
- parallel() -> executes the stream in parallel

Comparing Arraylist vs LinkedList ParallelStream performance
- ArrayList - almost the same the sequantial and paralle processing of arrylist
- LinkedList - parallel useage of here will slow down our program

```java
@Slf4j
public class App {

    private final static StopWatch stopWatch = new StopWatch("App");

    private static String toUpperWithDelay(String value) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return value.toUpperCase();
    }

    public static void main(String[] args) {
        List<String> input = List.of("Bob", "Jamie", "Jill", "Rick");

        stopWatch.start();
        List<String> outputSequential = input.stream().sequential().map(App::toUpperWithDelay).collect(Collectors.toList());
        log.info("{}", outputSequential);
        stopWatch.stop();
        log.info("Total time taken : " + stopWatch.getTime());

        stopWatch.reset();

        stopWatch.start();
        List<String> outputParallel = input.stream().parallel().map(App::toUpperWithDelay).collect(Collectors.toList());
        log.info("{}", outputSequential);
        stopWatch.stop();
        log.info("Total time taken : " + stopWatch.getTime());
    }
}
```

## Completable Future

- Is an async Reactive Functional Programming API

Features:
- Responsive
    - Fundementally async
    - Call returns immediately and the response wil be sent when its available
- Elastic
    - Async compputations normally run in pool of threads
    - Number of thread can go up and down based on the need
- Resilient
    - Exception on error wont crash the app or code
- Message Driven
    - Async computations interact with each through message in a event driven style

CompletableFuture API can be grouped into three categories:
- Factory methods
    - Initiate asynchrnous computation
- Completation stage methods
    - Chain asynchrnous computation
- Exception methods
    - Handle Exceptions in an Asynchrnous computation

```java
@Slf4j
public class HelloWorldService {

    public String helloWorld() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("inside helloWorld");
        return "hello world";
    }

    public String hello() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("inside hello");
        return "hello";
    }

    public String world() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("inside world");
        return " world!";
    }

    public CompletableFuture<String> worldFuture(String input) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return input + " world!";
        });
    }
}

@Slf4j
public class App {

    private final static StopWatch stopWatch = new StopWatch("App");

    public static void main(String[] args) {
        stopWatch.start();

        HelloWorldService hws = new HelloWorldService();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(hws::world);

        CompletableFuture<CompletableFuture<String>> first = hello
                .thenCombine(world, (h, w) -> h + w) // (first,second)
                .thenApply(hws::worldFuture); //CompletableFuture inside CompletableFuture

        log.info("result " + first.join());

        CompletableFuture<String> second = hello
                .thenCombine(world, (h, w) -> h + w) // (first,second)
                .thenApply(String::toUpperCase)
                .thenCompose(hws::worldFuture); // unwraps CompletableFuture like flatMap

        second.thenAccept(s -> log.info("result " + s)).join();

        stopWatch.stop();
        log.info("Total time taken : " + stopWatch.getTime());
    }
}
```

Exception handling CompletableFuture
- handle - catch exception and recover
- exceptionally - catch exception and recover
- whenComplete - catch exception and does not recover

```java
@Slf4j
public class App {

    private final static StopWatch stopWatch = new StopWatch("App");

    public static void main(String[] args) {
        stopWatch.start();

        HelloWorldService hws = new HelloWorldService();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(hws::world);

        CompletableFuture<CompletableFuture<String>> first = hello
                .whenComplete((s, throwable) -> {
                    if (throwable != null) {
                        log.info(throwable.getMessage());
                    }
                    log.info(s);
                })
                .thenCombine(world, (h, w) -> h + w)
                .exceptionally(throwable -> {
                    log.info(throwable.getMessage());
                    return "alternative";
                })
                .thenApply(hws::worldFuture)
                .handle((s, throwable) -> {
                    if (throwable != null) {
                        log.info(throwable.getMessage());
                    }
                    return CompletableFuture.supplyAsync(() -> "alternative");
                });

        log.info("result " + first.join().join());

        stopWatch.stop();
        log.info("Total time taken : " + stopWatch.getTime());
    }
}
```

The CompletableFuture behind the scnenes uses Fork/Join Pool, but we can apply our own Thread Pool, with the following risks:
- Thread being locked by a time consuming task
- Threads not avaiblable

```java
@Slf4j
public class App {

    private final static StopWatch stopWatch = new StopWatch("App");

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> this.hws.hello(), executorService);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> this.hws.world(), executorService);
    }
}
```

|Regular functions  |Async overload Functions             |
|-------------------|-------------------------------------|
|thenCombine()      |thenCombineAsync()                   |
|thenApply()        |thenApplyAsync()                     |
|thenCompose()      |thenComposeAsync()                   |
|thenAccept()       |thenAcceptAsync()                    |

- Using async functions allows us to change the thread of execution
- Use this when there are blocking operations in your CompletableFututre pipeline

## Handling multiple completablefutures via anyOf() and allOf()

- allOf()
    - Static method that part of CompletableFuture API
    - Use allOf() when you are dealing with Multiple CompletableFuture
- anyOf()
    - Static method that part of CompletableFuture API
    - Use anyOf() when you are dealing with retrieving data from multiple Data Sources
    - We will get back the data which returns faster (DB or SOAP or REST)

## Reactive programming (Flux)

Reactive types:
- **Flux** - 0 to N elements
- **Mono** - 0 to 1 elements

Reactive Stream specification, it has four interfaces:
- **Publisher** - one which provides data

```xml
<dependency>
    <groupId>io.projectreactor</groupId>
    <artifactId>reactor-core</artifactId>
    <version>3.4.9</version>
</dependency>
```

```java
public interface Publisher<T> {
    
    public void subscribe(Subscriber<? super T> s)
}
```

- **Subscriber** - one which recives data

```java
public interface SubScriber<T> {

    public void onSubscribe(Subscription s);
    public void onNext(T t);
    public void onError(Throwable t);
    public void onComplete();
}
```

- **Subscription** - represent on-to-one relationship between publisher and subscriber

```java
public interface Subscription {
    
    public void request(long g);
    public void cancel();
}
```

- **Processor** - represent processing stage for Publisher and Subscriber

```java
public interface Processor<T, R> extends Subscriber<T>, Publisher<R> {
}
```

### just() defer() fromSupplier() fromCallable()

```java
@Override
public void run(String... args) throws Exception {
    // Lazy vs Eager instation
    
    // Basically, this is the main difference between a Mono.just() 
    // and the rest of the methods we’ll compare in this article.
    // It is a hot publisher and the value has been captured at the instantiation time.
    Mono<LocalDateTime> monoJust = Mono.just(getLocalDateTime()).log();
    monoJust.subscribe(System.out::println);
    monoJust.subscribe(System.out::println);
    monoJust.subscribe(System.out::println);
    
    // This time, we’ve achieved a real laziness- a successfulDateFetching() 
    // was triggered each time a new Subscriber was registered.
    Mono<LocalDateTime> monoDefer = Mono.defer(() -> Mono.just(getLocalDateTime())).log();
    monoDefer.subscribe(System.out::println);
    monoDefer.subscribe(System.out::println);
    monoDefer.subscribe(System.out::println);
    
    // Similarly to defer(), we can delay the data evaluation with Mono.fromSupplier() case
    // If the Supplier resolves to null, the resulting Mono completes empty.
    Mono<LocalDateTime> monoFromSupplier = Mono.fromSupplier(() -> getLocalDateTime()).log();
    monoFromSupplier.subscribe(System.out::println);
    monoFromSupplier.subscribe(System.out::println);
    monoFromSupplier.subscribe(System.out::println);
    
    Mono<LocalDateTime> monoFromCallable = Mono.fromCallable(() -> getLocalDateTime()).log();
    monoFromCallable.subscribe(System.out::println);
    monoFromCallable.subscribe(System.out::println);
    monoFromCallable.subscribe(System.out::println);
}

public LocalDateTime getLocalDateTime() {
    try {
        Thread.sleep(1000);	
    } catch (InterruptedException ignored) {}
    System.out.println("GETTING DATE");
    return LocalDateTime.now();
}
```

### From source

```java
@Override
public void run(String... args) throws Exception {
    List<String> namesList = List.of("Taylor", "Steve");
    Flux<String> fluxFromIterable = Flux.fromIterable(namesList);
    fluxFromIterable.subscribe(System.out::println);

    String[] namesArray = new String[] {"John", "Raj", "Peter"};
    Flux<String> fluxFromArray = Flux.fromArray(namesArray);
    fluxFromArray.subscribe(System.out::println);

    Stream<String> namesStream = Stream.of("Brad", "Colin");
    Flux<String> fluxFromStream = Flux.fromStream(namesStream);
    fluxFromStream.subscribe(System.out::println);

    Flux<Integer> fluxRange = Flux.range(1, 10);
    fluxRange.subscribe(System.out::println);

    // Similar to Optional
    Mono.empty();
    Mono.error(RuntimeException::new);
}
```

### Data transformation

```java
@Override
public void run(String... args) throws Exception {
    Flux<String> lettersFlux = Flux.just("A", "B", "C")
            .filter(t -> t.equals("C"));
    lettersFlux.subscribe(System.out::println, 
            e -> System.out.println("This is a onError"), 
            () -> System.out.println("This is onComplete"));

    // Transform the items emitted by this Flux by applying a synchronous function to each item
    Flux<Integer> numbersFluxMap = Flux.just("1", "2", "3")
            .map(Integer::valueOf);
    numbersFluxMap.subscribe(System.out::println, 
            e -> System.out.println("This is a onError"), 
            () -> System.out.println("This is onComplete"));

    // ConcatMap works almost the same as flatMap, but preserves the order of items
    Flux<Integer> numbersFluxConcatMap = Flux.just("1", "2", "3")
            .concatMap(t -> Flux.just(Integer.valueOf(t.concat("1"))));
    numbersFluxConcatMap.subscribe(System.out::println, 
            e -> System.out.println("This is a onError"), 
            () -> System.out.println("This is onComplete"));

    // FlatMap does not care about the order of the items
    // Transform the elements emitted by this Flux asynchronously into Publishers
    List<String> keywordToSearch = List.of("b", "bo", "boo", "book", "books");
    Flux<String> keywordFlatMap = Flux.fromIterable(keywordToSearch)
        .flatMap(s -> {return Flux.just(s + " FirstResult " + s + " SecondResult");})
        .delayElements(Duration.ofSeconds(10));
    keywordFlatMap.subscribe(System.out::println, 
            e -> System.out.println("This is a onError"), 
            () -> System.out.println("This is onComplete"));

    Flux<String> keywordSwitchMap = Flux.fromIterable(keywordToSearch)
            .switchMap(s -> {return Flux.just(s + " FirstResult " + s + " SecondResult");})
            .delayElements(Duration.ofSeconds(10));
    keywordSwitchMap.subscribe(System.out::println, 
            e -> System.out.println("This is a onError"), 
            () -> System.out.println("This is onComplete"));

}
```

### Combining Mono and Flux

```java
@Override
public void run(String... args) throws Exception {
    Flux<String> abc = Flux.just("A", "B", "C")
            .delayElements(Duration.ofMillis(100));

    Flux<String> def = Flux.just("D", "E", "F")
            .delayElements(Duration.ofMillis(125));

    // A B C D E F
    Flux<String> concat = Flux.concat(abc, def);
    abc.concatWith(def);
    concat.subscribe(System.out::println);
    abc.subscribe(System.out::println);

    // A D B E C F
    Flux<String> merge = Flux.merge(abc, def);
    abc.concatWith(def);
    merge.subscribe(System.out::println);
    abc.subscribe(System.out::println);

    // AD, BE, CF
    Flux<String> zip = Flux.zip(abc, def, (first, second) -> first.concat(second));
    zip.subscribe(System.out::println);
}
```

### Transform from Mono to Flux

```java
@Override
public void run(String... args) throws Exception {
    Flux<Object> monoNameFlatMapMany = Mono.just("alex")
            .map(String::toUpperCase)
            .filter(s -> s.length() > 1)  						// Mono<String>
            .flatMapMany(t -> Mono.just(List.of(t.split(""))) 	// Mono<List<String>> -> Flux<String>
                    .delayElement(Duration.ofSeconds(1)));
    monoNameFlatMapMany.subscribe(System.out::println);
    
    Mono<String> monoNameTransform = Mono.just("alex")
        .transform(t -> t.map(String::toUpperCase)
                .filter(s -> s.length() > 1));
    
    monoNameTransform.subscribe(System.out::println);    
}
```

### Default values

```java
@Override
public void run(String... args) throws Exception {
    // Setting default values
    Mono<String> monoNameDefault = Mono.just("alex")
            .map(String::toUpperCase)
            .filter(s -> s.length() > 1)
            .defaultIfEmpty("ecneb");
    monoNameDefault.subscribe(System.out::println);

    Mono<String> monoNameDefaultSwitch = Mono.just("alex")
            .map(String::toUpperCase)
            .filter(s -> s.length() > 1)
            .switchIfEmpty(Mono.just("ecneb"));
    monoNameDefaultSwitch.subscribe(System.out::println);
}
```

### Using doOn callbacks

```java
@Override
public void run(String... args) throws Exception {
    Flux<String> doOnCallbacks = Flux.just("alex", "ben", "chloe")
    .map(s -> s.toUpperCase())
    .map(String::toUpperCase)
    .delayElements(Duration.ofMillis(500))
    .filter(s -> s.length() > 3)
    .map(s -> s.length() + "-" + s)
    .doOnNext(name -> {
        System.out.println("name is : " + name);
        name = name.toLowerCase();
    })
    .doOnSubscribe(s -> {
        System.out.println("Subscription  is : " + s);
    })
    .doOnComplete(() -> {
        System.out.println("Completed sending all the items.");
    })
    .doFinally((signalType) -> {
        System.out.println("value is : " + signalType);
    })
    .defaultIfEmpty("default");

    doOnCallbacks.subscribe(System.out::println);
}
```

### Handling errors

```java
@Override
public void run(String... args) throws Exception {
    // Category 1: Recover from an Exception
        // onErrorReturn
        // onErrorResume
        // onErrorContinue

    Flux<String> onErrorReturn = Flux.just("A", "B", "C")
            .concatWith(Flux.error(new IllegalStateException("Exception Occurred"))).onErrorReturn("D");
    onErrorReturn.subscribe(System.out::println);

    Flux<String> onErrorResume = Flux.just("A", "B", "C")
            .concatWith(Flux.error(new IllegalStateException("Exception Occurred")))
            .onErrorResume((e) -> {
                System.out.println("Exception is " + e);
                if (e instanceof IllegalStateException) {
                    return Flux.just("D", "E", "F");
                } else {
                    return Flux.error(e);
                }
            });
    onErrorResume.subscribe(System.out::println);

    Flux<String> onErrorContinue = Flux.just("A", "B", "C")
            .map(name -> {
                if (name.equals("B")) {
                    throw new IllegalStateException("Exception Occurred");
                }
                return name;
            })
            .concatWith(Flux.just("D"))
            .onErrorContinue((exception, value) -> {
                System.out.println("Value is : " + value);
                System.out.println("Exception is : " + exception.getMessage());
            });
    onErrorContinue.subscribe(System.out::println);

    // Category 2: Take an action on the exception and re throw the exception
        // onErrorMap
        // doOnError

    Flux<String> onErrorMap = Flux.just("A", "B", "C")
            .map(name -> {
                if (name.equals("B")) {
                    throw new IllegalStateException("Exception Occurred");
                }
                return name;
            })
            .onErrorMap((exception) -> new NullPointerException(exception.getMessage()));
    onErrorMap.subscribe(System.out::println);
    
    Flux<String> doOnError = Flux.just("A", "B", "C")
            .concatWith(Flux.error(new IllegalStateException("Exception Occurred")))
            //Write any logic you would like to perform when an exception happens
            .doOnError((e) -> System.out.println("Exception is : " + e));
    doOnError.subscribe(System.out::println);
}
```

### repeat() retry() and retryWhen()

```java
@Override
public void run(String... args) throws Exception {
    Flux<String> onErrorReturn = Flux.just("A", "B", "C")
            .concatWith(Flux.error(new IllegalStateException("Exception Occurred")))
            // .retry()
            // .retry(3)
            // .repeat() USED TO REPEAT EXISTING SEQUENCE, THIS OPERATOR WORKS AS LONG AS NO EXCEPTION IS THROWED
            .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(10000)) // retry 3 times and wait 10s
                    .filter(IllegalStateException.class::isInstance)
                    .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) -> Exceptions.propagate(retrySignal.failure()))));
    onErrorReturn.subscribe(System.out::println);
}
```

### Schedulers & Delay

publishOn & subscribeOn are convenient methods in Project Reactor which accepts any of the above Schedulers to change the task execution context for the operations in a reactive pipeline. While subscribeOn forces the source emission to use specific Schedulers, publishOn changes Schedulers for all the downstream operations in the pipeline as shown below

![publishOn vs subscribeOn](https://raw.githubusercontent.com/matebence/spring/parallel/docs/publishOn_vs_subscribeOn.png)

```java
@Override
public void run(String... args) throws Exception {
    List<String> names = List.of("Ben", "Alex", "Cloe");
    
    // Immediate - single thread (main)
    Flux<String> immediateFlux = Flux.fromIterable(names)
        .delayElements(Duration.ofSeconds(1))
        .map(t -> {
            System.out.println(Thread.currentThread().getName());
            return new StringBuffer(t).reverse().toString();
        })
        .subscribeOn(Schedulers.boundedElastic())
        .publishOn(Schedulers.immediate())
        .map(t -> {
            System.out.println(Thread.currentThread().getName());
            return t.toUpperCase();
        });
    
    immediateFlux.subscribe(System.out::println);
    System.out.println("----------------------");
    
    // Parallel - pool of threads
    Flux<String> parallelFlux = Flux.fromIterable(names)
        .delayElements(Duration.ofSeconds(1))
        .map(t -> {
            System.out.println(Thread.currentThread().getName());
            return new StringBuffer(t).reverse().toString();
        })
        .subscribeOn(Schedulers.single())
        .publishOn(Schedulers.parallel())
        .map(t -> {
            System.out.println(Thread.currentThread().getName());
            return t.toUpperCase();
        });

    parallelFlux.subscribe(System.out::println);
    System.out.println("---------------------");
    
    // Single - one thread beside main
    // For heavy computations where we need high COU power we should use parallel
    Flux<String> singleFlux = Flux.fromIterable(names)
        .delayElements(Duration.ofSeconds(1))
        .map(t -> {
            System.out.println(Thread.currentThread().getName());
            return new StringBuffer(t).reverse().toString();
        })
        .subscribeOn(Schedulers.parallel())
        .publishOn(Schedulers.single())
        .map(t -> {
            System.out.println(Thread.currentThread().getName());
            return t.toUpperCase();
        });

    singleFlux.subscribe(System.out::println);
    System.out.println("---------------------");
    
    // Bounded elastic - the number of threads can grow based on the needs
    // For rest db calls we use boudedElastic
    Flux<String> boundedElastic = Flux.fromIterable(names)
        .delayElements(Duration.ofSeconds(1))
        .map(t -> {
            System.out.println(Thread.currentThread().getName());
            return new StringBuffer(t).reverse().toString();
        })
        .subscribeOn(Schedulers.immediate())
        .publishOn(Schedulers.boundedElastic())
        .map(t -> {
            System.out.println(Thread.currentThread().getName());
            return t.toUpperCase();
        });

    boundedElastic.subscribe(System.out::println);
    System.out.println("---------------------");
}
```

### Backpressure

```java
@Override
public void run(String... args) throws Exception {
    Flux<Integer> numbers = Flux.range(1, 100);
    numbers
        // .onBackpressureDrop(x -> System.out.println("Droppped " + x))
    
        // In case that our strategies of sampling or batching elements do not 
        // help with filling up a buffer, we need to implement a strategy of handling
        // cases when a buffer is filling up.
        .onBackpressureBuffer(10, BufferOverflowStrategy.ERROR)
        .subscribe(new BaseSubscriber() {

        @Override
        protected void hookOnSubscribe(Subscription subscription) {
            // request(2);
            for (int i = 0; i < 40; i++) {
                request(2);
            }
        }

        @Override
        protected void hookOnNext(Object value) {
            System.out.println(value);
            // if (value.equals(2)) cancel();
        }

        @Override
        protected void hookOnComplete() {
            System.out.println("hookOnComplete");
        }

        @Override
        protected void hookOnError(Throwable throwable) {
            System.out.println("hookOnError");
        }

        @Override
        protected void hookOnCancel() {
            System.out.println("hookOnCancel");
        }

        @Override
        protected void hookFinally(SignalType type) {
            System.out.println("hookFinally");
        }
    });
}
```

### Parallelism

The idea behind parallelFlux is to leverage the multi-core processor that we have in todays hardware

```java
@Override
public void run(String... args) throws Exception {
    List<String> names = List.of("Ben", "Alex", "Cloe");

    ParallelFlux<String> fluxRunOn = Flux.fromIterable(names)
        //.publishOn(Schedulers.parallel()) it would take 30s
        .parallel() // now it takes only 1s
        .runOn(Schedulers.boundedElastic())
        .map(this::upperCase)
        .log();
    
    fluxRunOn.subscribe(System.out::println);
    
    // The same can be achieved via flatMap, but the order is unpredictable
    Flux<String> flatMap = Flux.fromIterable(names)
            .flatMap(name -> Mono.just(name)
                    .map(this::upperCase)
                    .subscribeOn(Schedulers.parallel()))
                    .log();
    
    flatMap.subscribe(System.out::println);
    
    // Solution is to used flatMapSequential
    Flux<String> flatMapSequential = Flux.fromIterable(names)
            .flatMapSequential(name -> {
                return Mono.just(name)
                        .map(this::upperCase)
                        .subscribeOn(Schedulers.parallel());

            }).log();
    
    flatMapSequential.subscribe(System.out::println);
}

private String upperCase(String name) {
    try {
        Thread.sleep(10000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    return name.toUpperCase();
}
```

### Hot vs Cold streams
- **Cold** - streams is a type of stream which emits the elements from beginning to end for every new subscription
    - HTTP call
    - DB call
- **Hot** - streams data is emitted continiously. Any new subscriber will only get the currenct state of the Reactive stream
    - Uber driver tracking

```java
@Override
public void run(String... args) throws Exception {
    System.out.println("---------------COLD--------------");
    Flux<Integer> numbers = Flux.range(1, 10);

    numbers.subscribe(s -> System.out.println("Subscriber 1 : " + s)); //emits the value from beginning
    numbers.subscribe(s -> System.out.println("Subscriber 2 : " + s)); //emits the value from beginning
    
    System.out.println("---------------HOT--------------");
    
    Flux<Integer> numbersFlux = Flux.range(1, 10)
            .delayElements(Duration.ofSeconds(1));

    // ConnectableFlux<Integer> connectableFlux = numbersFlux.publish();
    // connectableFlux.connect();
    
    // ConnectableFlux<Integer> connectableFlux = stringFlux.publish().autoConnect(2); // we are waitfor atleas two subscribers
    
    ConnectableFlux<Integer> connectableFlux = stringFlux.publish().refCount(2); // we have least then two subscribers it will stop sending events

    Thread.sleep(3000);
    connectableFlux.subscribe(s -> System.out.println("Subscriber 1 : " + s));
    Thread.sleep(1000);
    connectableFlux.subscribe(s -> System.out.println("Subscriber 2 : " + s)); // does not get the values from beginning
    Thread.sleep(10000);
}
```

### Using create and generate

```java
@Override
public void run(String... args) throws Exception {
    List<String> letters = List.of("a", "b", "c");
    
    // Used to bridge and existig APi in to the Reactive World
	// This is Asynchronous and multihtreaded
    Flux<String> createFlux = Flux.create(sink -> {
        letters.forEach(sink::next);
        sink.complete();
    }, FluxSink.OverflowStrategy.BUFFER);
    createFlux.subscribe(System.out::println);
    
     // this operator takes a initial value and a generator function as an input and continiously emit values
     // Its also called Synchronous generator
    Flux<Integer> generateFlux = Flux.generate(
            () -> 1,
            (state, sink) -> {
                sink.next(state * 2);
                if (state == 10) {
                    sink.complete();
                }
                return state + 1;
            });
    generateFlux.subscribe(System.out::println);
}
```