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
