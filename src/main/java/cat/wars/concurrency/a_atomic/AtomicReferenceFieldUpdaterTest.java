package cat.wars.concurrency.a_atomic;

import lombok.Data;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * Created by IntelliJ IDEA.
 * User: wars
 * Date: 4/24/19
 * Time: 11:22 PM
 * AtomicReferenceFieldUpdater test
 */

public class AtomicReferenceFieldUpdaterTest {

    @Data
    private static class User {
        volatile String name;
    }

    public static void main(String[] args) {
        AtomicReferenceFieldUpdater<User, String> atomicReferenceFieldUpdater = AtomicReferenceFieldUpdater.newUpdater(User.class, String.class, "name");
        User user = new User();
        user.setName("Wars");

        atomicReferenceFieldUpdater.compareAndSet(user, user.getName(), "Cat");
        System.out.println(user.getName());
    }
}
