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

### Concurrent variables, objects and methods 

