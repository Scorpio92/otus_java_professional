package ru.otus.runner;

import ru.otus.annotations.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class TestRunner {

    public static void runTestsInClass(Class<?> c) {
        Objects.requireNonNull(c, "Test class must not be null!");

        Method[] beforeMethods = findAnnotatedMethods(c, Before.class);
        if (beforeMethods.length > 1) {
            throw new RuntimeException("Only one method must be annotated with @Before annotation");
        }
        Method[] afterMethods = findAnnotatedMethods(c, After.class);
        if (afterMethods.length > 1) {
            throw new RuntimeException("Only one method must be annotated with @After annotation");
        }
        Method[] testMethods = findAnnotatedMethods(c, Test.class);
        if (testMethods.length == 0) throw new RuntimeException("Not found @Test annotation");

        AtomicInteger counter = new AtomicInteger();
        List<Result> results = new ArrayList<>();
        Arrays.stream(testMethods).forEach(testMethod -> {
            log("\nRun test [%s] number [%s]:\n", testMethod.getName(), counter.incrementAndGet());
            Object o = instanceByClass(c);
            Result result;
            if (beforeMethods.length == 1) {
                result = runMethod(o, beforeMethods[0]);
                results.add(result);
                result.error().ifPresentOrElse(TestRunner::log, () -> log("@Before is passed"));
            }
            result = runMethod(o, testMethod);
            results.add(result);
            result.error().ifPresentOrElse(TestRunner::log, () -> log("@Test is passed"));
            if (afterMethods.length == 1) {
                result = runMethod(o, afterMethods[0]);
                results.add(result);
                result.error().ifPresentOrElse(TestRunner::log, () -> log("@After is passed"));
            }
        });

        int generalCount = testMethods.length;
        int successCount = (int) results.stream()
                .filter(result -> result.annotationType() == Test.class)
                .filter(Result::isSuccess)
                .count();
        int errorCount = (int) results.stream()
                .filter(result -> result.annotationType() == Test.class)
                .filter(result -> !result.isSuccess())
                .count();
        log("\nAll tests is completed. General count: [%s], passed: [%s], failed: [%s]\n", generalCount, successCount, errorCount);
    }

    private static Method[] findAnnotatedMethods(Class<?> c, Class<?> annotation) {
        return Arrays.stream(c.getMethods())
                .filter(method -> Arrays.stream(method.getDeclaredAnnotations()).count() == 1)
                .filter(method -> Arrays.stream(method.getDeclaredAnnotations()).anyMatch(a -> a.annotationType() == annotation))
                .toArray(Method[]::new);
    }

    private static Object instanceByClass(Class<?> c) {
        try {
            return c.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("Can't get instance of [%s] with default constructor", c.getSimpleName()), e);
        }
    }

    private static Result runMethod(Object o, Method method) {
        Class<?> annotationType = method.getDeclaredAnnotations()[0].annotationType();
        Result result = new Result(annotationType, true, null);
        try {
            method.invoke(o);
        } catch (Throwable throwable) {
            result = new Result(annotationType, false, throwable.getCause());
        }
        return result;
    }

    private static void log(String msg) {
        System.out.println(msg);
    }

    private static void log(String msg, Object... args) {
        System.out.printf(msg.concat("\n"), args);
    }

    private static void log(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        System.out.println("\n".concat(sw.toString()));
        pw.close();
        try {
            sw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
