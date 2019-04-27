package cat.wars.concurrency.a_atomic;

/**
 * Created by IntelliJ IDEA.
 * User: wars
 * Date: 4/27/19
 * Time: 12:03 PM
 * Volatile test
 */

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
