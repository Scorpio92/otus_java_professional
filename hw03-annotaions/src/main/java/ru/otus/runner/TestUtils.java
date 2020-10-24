package main.java.ru.otus.runner;

import java.util.List;

public class TestUtils {

    public static void assertThatNonNull(Object o) {
        if (o == null) throw new TestException("Object is not initialized");
    }

    public static void assertListSize(List<?> list, int size) {
        if (list.size() != size) {
            throw new TestException(String.format("Current list size [%s] not equal [%s]", list.size(), size));
        }
    }
}
