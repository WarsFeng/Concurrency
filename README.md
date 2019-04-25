# Concurrency

## CountDownLatch
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

## Semaphore
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

## Atomic

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

### Synchronized
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