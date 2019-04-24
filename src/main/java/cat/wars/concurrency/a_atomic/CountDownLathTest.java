package cat.wars.concurrency.a_atomic;

import java.util.concurrent.CountDownLatch;

/**
 * Created by IntelliJ IDEA.
 * User: wars
 * Date: 4/24/19
 * Time: 1:46 PM
 * Count downLath test
 */

public class CountDownLathTest {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countdownlatch = new CountDownLatch(1);
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
        countdownlatch.countDown();
    }
}
