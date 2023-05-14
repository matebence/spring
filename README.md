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

public class ComplexTask implements  Runnable {

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