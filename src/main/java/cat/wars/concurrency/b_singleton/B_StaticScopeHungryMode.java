package cat.wars.concurrency.b_singleton;

import cat.wars.concurrency.annoations.ThreadSafe;

/**
 * Created by IntelliJ IDEA.
 * User: wars
 * Date: 4/30/19
 * Time: 5:03 PM
 */

@ThreadSafe
public class B_StaticScopeHungryMode {

    private static B_StaticScopeHungryMode instance;

    static {
        instance = new B_StaticScopeHungryMode();
    }

    private B_StaticScopeHungryMode() {
    }

    public static B_StaticScopeHungryMode getInstance() {
        return instance;
    }
}
