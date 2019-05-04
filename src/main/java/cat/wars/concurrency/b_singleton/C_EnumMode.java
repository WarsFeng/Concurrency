package cat.wars.concurrency.b_singleton;

/**
 * Created by IntelliJ IDEA.
 * User: wars
 * Date: 4/30/19
 * Time: 5:09 PM
 */

public class C_EnumMode {

    enum E {
        INSTANCE;

        private C_EnumMode instance;

        E() {
            instance = new C_EnumMode();
        }

        public C_EnumMode getInstance() {
            return instance;
        }
    }

    public static void main(String[] args) {
        System.out.println(E.INSTANCE.instance);
        System.out.println(E.INSTANCE.instance);
    }
}
