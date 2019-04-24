package cat.wars.concurrency.a_atomic;

import cat.wars.concurrency.annoations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by IntelliJ IDEA.
 * User: wars
 * Date: 4/24/19
 * Time: 1:00 PM
 */

@Slf4j
@ThreadSafe
public class AtomicTest {

    private static int threadTotal = 200;
    private static int clientTotal = 5000;
    private static AtomicInteger count = new AtomicInteger(0);

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
                log.info("count: {}", count.get());
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
        count.getAndIncrement();
    }
}
