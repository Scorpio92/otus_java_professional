package main.java.ru.otus;

import main.java.ru.otus.annotations.After;
import main.java.ru.otus.annotations.Before;
import main.java.ru.otus.annotations.Test;
import main.java.ru.otus.runner.TestUtils;
import ru.otus.DIYarrayList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Tests {

    private final static List<Integer> DEFAULT_TEST_LIST = Arrays.asList(1, 2, 3, 4, 5, 6);
    private DIYarrayList<Integer> arrayList;

    @Before
    public void before() {
        arrayList = new DIYarrayList<>(DEFAULT_TEST_LIST.size());
    }

    @Test
    public void testInitialize() {
        TestUtils.assertThatNonNull(arrayList);
    }

    @Test
    public void testArrayAddAll() {
        arrayList.addAll(DEFAULT_TEST_LIST);
        TestUtils.assertListSize(arrayList, DEFAULT_TEST_LIST.size());
    }

    @Test
    public void testArrayAdd() {
        DEFAULT_TEST_LIST.forEach(integer -> arrayList.add(integer));
        TestUtils.assertListSize(arrayList, DEFAULT_TEST_LIST.size());
    }

    @Test
    public void testArrayCopy() {
        DEFAULT_TEST_LIST.forEach(integer -> arrayList.add(integer));
        DIYarrayList<Integer> integers = new DIYarrayList<>();
        DEFAULT_TEST_LIST.forEach(integer -> integers.add(0));
        Collections.copy(integers, arrayList);
        TestUtils.assertListsEqual(arrayList, integers);
    }

    @After
    public void after() {
        arrayList = null;
    }

    public void notAnnotatedMethod() {

    }
}
