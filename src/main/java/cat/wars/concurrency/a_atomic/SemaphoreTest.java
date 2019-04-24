package cat.wars.concurrency.a_atomic;

import java.util.concurrent.Semaphore;

/**
 * Created by IntelliJ IDEA.
 * User: wars
 * Date: 4/24/19
 * Time: 1:56 PM
 * Semaphore test
 */

public class SemaphoreTest {

    public static void main(String[] args) throws InterruptedException {
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
    }
}
