package ru.otus.runner;

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

    public static void assertListsEqual(List<?> l1, List<?> l2) {
        if (l1.size() != l2.size()) throw new TestException("Lists is not equals");
        int eqCount = 0;
        for (int i = 0; i < l1.size(); i++) {
            if (l1.get(i).equals(l2.get(i))) eqCount++;
        }
        if (eqCount != l1.size()) throw new TestException("Lists is not equals");
    }
}
