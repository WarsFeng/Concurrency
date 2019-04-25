package cat.wars.concurrency.a_atomic;

/**
 * Created by IntelliJ IDEA.
 * User: wars
 * Date: 4/25/19
 * Time: 11:33 PM
 * Synchronized test
 */

public class SynchronizedTest {

    private synchronized void test() throws InterruptedException {
            System.out.println("In");
            Thread.sleep(10000);
    }

    public static void main(String[] args) {
        SynchronizedTest t = new SynchronizedTest();
        SynchronizedTest t2 = new SynchronizedTest();
        new Thread(() -> {
            try {
                t.test();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                t2.test();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
