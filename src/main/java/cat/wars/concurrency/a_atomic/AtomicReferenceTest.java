package cat.wars.concurrency.a_atomic;

import lombok.Data;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * Created by IntelliJ IDEA.
 * User: wars
 * Date: 4/24/19
 * Time: 10:18 PM
 * AtomicReference test
 */

public class AtomicReferenceTest {

    @Data
    private static class User {
        private String name;
    }

    public static void main(String[] args) {
        AtomicReference<User> longAtomicReference = new AtomicReference<>();
        User oldUser = new User();
        oldUser.setName("old");
        longAtomicReference.set(oldUser);

        User userBak = oldUser; // Copy user addr
        oldUser = new User();

        boolean b = longAtomicReference.compareAndSet(oldUser, userBak); // Check addr value, if equals, update it
        System.out.println(b);
    }
}
