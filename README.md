# Concurrency

## CountDownLatch
<details>
<summary>Click me</summary>
<pre><code>

``` java
	CountDownLatch countdownlatch = new CountDownLatch(1); // The difinition count is 1
	System.out.println("Count: " + countdownlatch.getCount()); // 1

	new Thread(() -> {
		try {
			countdownlatch.await(); // Current thread enter await, until count reduced to 0
			System.out.println("Count: " + countdownlatch.getCount()); // 0
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}).start();

	Thread.sleep(1000);
	System.out.println("Count down");
	countdownlatch.countDown(); // down 1
```
</code></pre>
</details>

## Semaphore
<details>
<summary>Click me</summary>
<pre><code>

``` java
    Semaphore semaphore = new Semaphore(5);
    for (int i = 0; i < 5; i++) {
        new Thread(() -> {
            try {
                semaphore.acquire(); // Get a semaphore
                semaphore.release(); // Release a semaphore
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
   	Thread.sleep(1000);
    System.out.println("Available semaphore: " + semaphore.availablePermits()); // 5
```
``` java
    Semaphore semaphore = new Semaphore(5);
    for (int i = 0; i < 5; i++) {
        new Thread(() -> {
            try {
                semaphore.acquire();
                //semaphore.release(); // Not release
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    Thread.sleep(1000);
    System.out.println("Available semaphore: " + semaphore.availablePermits()); // 0
```
``` java
	Semaphore semaphore = new Semaphore(5);
	for (int i = 0; i < 5; i++) {
		new Thread(() -> {
			try {
				semaphore.acquire();
				//semaphore.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}
	Thread.sleep(1000);
	System.out.println("Available semaphore: " + semaphore.availablePermits()); // 0
	semaphore.acquire(); // Because available semaphore is 0, threa enters await status, until available gt 0
	System.out.println("END"); // Always not output END
```
</code></pre>
</details>

## Atomic
<details>
<summary>Click me</summary>
<pre><code>

### AtomicLong and LongAdder
Jdk8 update LongAdder and DoubleAdder  
> Under low update contention, the two classes have similar characteristics. But under 
**high contention, expected throughput of this class is significantly higher**, at the 
expense of **higher space consumption**.

Recommend use 'LongAdder' and 'DoubleAdder',
But may not be safe of reading(**update value when reading**), AtomicLong is safer, its principle is CAS(compareAndSwap)

### AtomicXX
``` java
@Slf4j
@ThreadSafe
    public class AtomicTest {
        
        private final static int threadTotal = 200;
        private final static int clientTotal = 5000;
        // private static AtomicLong count = new AtomicLong(0);
        private static LongAdder count = new LongAdder(); //Initial value is 0

        public static void main(String[] args) throws Exception {
            ExecutorService executor = Executors.newCachedThreadPool();
            Semaphore semaphore = new Semaphore(threadTotal);
            CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
            log.info("CountdownLatch count: {}", countDownLatch.getCount());

            new Thread(() -> {
                try {
                    countDownLatch.await();
                    executor.shutdown();
                    log.info("CountdownLatch count: {}", countDownLatch.getCount());
                    log.info("count: {}", count.longValue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }).start();

            Thread.sleep(1000);
            for (int i = 0; i < clientTotal; i++) {
                executor.execute(() -> {
                    try {
                        semaphore.acquire();
                        add();
                        semaphore.release();
                        countDownLatch.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        }

        private static void add() {
            count.increment();
        }
    }
```

### AtomicReference
> An object reference that may be updated atomically. 
``` java
    public class AtomicReferenceTest {

        @Data
        private static class User {
            private String name;
        }

        public static void main(String[] args) {
            AtomicReference<User> longAtomicReference = new AtomicReference<>();
            User oldUser = new User();
            oldUser.setName("old");
            longAtomicReference.set(oldUser);

            User userBak = oldUser; // Copy user addr
            oldUser = new User();

            boolean b = longAtomicReference.compareAndSet(oldUser, userBak); // Check addr value, if equals, update it
            System.out.println(b);
        }
    }
```

### AtomicReferenceFieldUpdater
Modify object field
``` java
    public class AtomicReferenceFieldUpdaterTest {
    
        @Data
        private static class User {
            volatile String name;
        }

        public static void main(String[] args) {
            AtomicReferenceFieldUpdater<User, String> atomicReferenceFieldUpdater = AtomicReferenceFieldUpdater.newUpdater(User.class, String.class, "name");
            User user = new User();
            user.setName("Wars");

            atomicReferenceFieldUpdater.compareAndSet(user, user.getName(), "Cat");
            System.out.println(user.getName());
        }
    }
```

### AtomicStampedReference
AtomicStampedReference vs. AtomicReference added one version number param, fix ABA bug
``` java
    public class AtomicStampedReferenceTest {

        private static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(0, 0);

        public static void main(String[] args) {
            atomicStampedReference.compareAndSet(0, 1, 0, 1);
        }
    }
```

### AtomicLongArray
AtomicLongArray through the index operation array
``` java
    public class AtomicLongArrayTest {

        private static AtomicLongArray atomicLongArray = new AtomicLongArray(new long[]{1L, 2L, 3L});
        
        public static void main(String[] args) {
            atomicLongArray.incrementAndGet(0); // index
        }
    }
```
</code></pre>
</details>

## Synchronized
<details>
<summary>Click me</summary>
<pre><code>

* Code scope
  * If synchronized(this), not unique: The current instance is valid
  * If synchronized(A.class), unique: The all instance is valid
* Function
  * equals synchronized(this){} this function content
  * The current instance is valid
* Static function
  * equals synchronized(This.class){} this function content
  * The all instance is valid
* Class
  * equals synchronized(This.class){} all function content of this class 
  * The all instance is valid
</code></pre>
</details>
  
## volatile
<details>
<summary>Click me</summary>

Guaranteed single thread, sequential execution of reads and writes
1) Wrong usage
<details>
<summary>Click me</summary>
<pre><code>

``` java
    @NotThreadSafe
    public class VolatileTest {

        private static final int MAX_THREAD = 50;
        private static final int EXECUTE_COUNT = 5000;
        private static volatile int count = 0;

        public static void main(String[] args) {
            CountDownLatch countDownLatch = new CountDownLatch(EXECUTE_COUNT);
            // Executed out
            new Thread(() -> {
                try {
                    countDownLatch.await();
                    System.out.println("count: " + count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            // Executor
            ExecutorService executor = Executors.newCachedThreadPool();
            Semaphore semaphore = new Semaphore(MAX_THREAD);
            for (int i = 0; i < EXECUTE_COUNT; i++) {
                executor.execute(() -> {
                    try {
                        semaphore.acquire();
                        count++;
                        semaphore.release();
                        countDownLatch.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
```
</code></pre>
</details>

2) Recommended usage
<details>
<summary>Click me</summary>
<pre><code>

``` java
    public class VolatileTest2 {

        private static volatile boolean semaphore = false;

        public static void main(String[] args) throws InterruptedException {
            new Thread(() -> {
                try {
                    while (!semaphore) Thread.sleep(1000);
                    System.out.println("Finish");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();


            Thread.sleep(10000);
            semaphore = true;
        }
    }
```
</code></pre>
</details>
</details>

## Singleton publish
<details>
<summary>Click me</summary>

1. Dual if lazy mode
<details>
<summary>Click me</summary>
<pre><code>

``` java
    @ThreadSafe
    public class A_Singleton {

        private volatile static A_Singleton instance;

        private A_Singleton() {
        }

        public static A_Singleton getInstance() {
            if (null == instance)
                synchronized (A_Singleton.class) {
                    instance = new A_Singleton(); // If no volatile, it may lead to Out-of-order execution initialization
                }

            return instance;
        }
    }
```
</code></pre>
</details>

2. Static scope Hungry mode
<details>
<summary>Click me</summary>
<pre><code>

``` java
    @ThreadSafe
    public class B {

        private static B instance;

        static {
            instance = new B();
        }

        private B() {
        }

        public static B getInstance() {
            return instance;
        }
    }
```
</code></pre>
</details>

3. Enum mode
<details>
<summary>Click me</summary>
<pre><code>

``` java
    enum E {
        INSTANCE;

        private C_EnumMode instance;

        E() {
            instance = new C_EnumMode();
        }

        public C_EnumMode getInstance() {
            return instance;
        }
    }
```
</code></pre>
</details>
</details>
