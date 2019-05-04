package cat.wars.concurrency.b_singleton;

import cat.wars.concurrency.annoations.ThreadSafe;

/**
 * Created by IntelliJ IDEA.
 * User: wars
 * Date: 4/30/19
 * Time: 11:17 AM
 */

@ThreadSafe
public class A_DualIfLazyMode {

    private volatile static A_DualIfLazyMode instance;

    private A_DualIfLazyMode() {
    }

    public static A_DualIfLazyMode getInstance() {
        if (null == instance)
            synchronized (A_DualIfLazyMode.class) {
                instance = new A_DualIfLazyMode(); // If no volatile, it may lead to Out-of-order execution initialization
            }
        return instance;
    }
}
