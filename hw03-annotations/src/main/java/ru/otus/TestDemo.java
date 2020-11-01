package ru.otus;

import ru.otus.runner.TestRunner;

public class TestDemo {

    public static void main(String... args) {
        TestRunner.runTestsInClass(Tests.class);
    }
}
