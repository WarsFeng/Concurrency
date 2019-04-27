package cat.wars.concurrency.a_atomic;

import cat.wars.concurrency.annoations.NotThreadSafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by IntelliJ IDEA.
 * User: wars
 * Date: 4/27/19
 * Time: 12:03 PM
 * Volatile test
 */

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
