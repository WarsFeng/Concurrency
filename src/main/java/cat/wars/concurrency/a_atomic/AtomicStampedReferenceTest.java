package cat.wars.concurrency.a_atomic;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * Created by IntelliJ IDEA.
 * User: wars
 * Date: 4/25/19
 * Time: 6:56 AM
 * AtomicStampedReference test
 */

public class AtomicStampedReferenceTest {

    private static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(0, 0);

    public static void main(String[] args) {
        atomicStampedReference.compareAndSet(0, 1, 0, 1);
    }
}
