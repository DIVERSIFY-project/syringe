package fr.inria.diversify.syringe;

/**
 * Created by marodrig on 11/12/2014.
 */
public class TestLogger {

    public static int totalCount;

    public static void method() {
        totalCount++;
    }

    public static void test() {
        totalCount++;
    }

    public static void inc() {};

    public static void testWithId(int id) {
        totalCount++;
    }


}
