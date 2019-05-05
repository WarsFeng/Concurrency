package cat.wars.concurrency;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by IntelliJ IDEA.
 * User: wars
 * Date: 5/5/19
 * Time: 11:47 PM
 */

public class CyclicBarrierExample {

    private static LongAdder count = new LongAdder();
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);
    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> System.out.println(count.longValue()));

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            executorService.execute(CyclicBarrierExample::add);
        }
        executorService.shutdown();
    }

    private static void add() {
        try {
            count.increment();
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
