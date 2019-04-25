package cat.wars.concurrency.a_atomic;

import java.util.concurrent.atomic.AtomicLongArray;

/**
 * Created by IntelliJ IDEA.
 * User: wars
 * Date: 4/25/19
 * Time: 7:20 AM
 * {@link java.util.concurrent.atomic.AtomicLongArray test}
 */

public class AtomicLongArrayTest {

    private static AtomicLongArray atomicLongArray = new AtomicLongArray(new long[]{1L, 2L, 3L});

    public static void main(String[] args) {
        atomicLongArray.incrementAndGet(0); // index
    }
}
