package ru.otus;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class ThreadsCasDemo {

    private final static Logger log = LoggerFactory.getLogger(ThreadsCasDemo.class);
    private final static String THREAD_NAME_1 = "Thread_1";
    private final static String THREAD_NAME_2 = "Thread_2";
    private final static int MAX_VALUE = 10;
    private final AtomicInteger value = new AtomicInteger(1);
    private final AtomicBoolean increment = new AtomicBoolean(true);

    public static void main(String... args) {
        ThreadsCasDemo td = new ThreadsCasDemo();

        Thread t1 = new Thread(() -> td.justDoIt(integer -> integer % 2 != 0));
        t1.setName(THREAD_NAME_1);

        Thread t2 = new Thread(() -> td.justDoIt(integer -> integer != 0 && integer % 2 == 0));
        t2.setName(THREAD_NAME_2);

        t1.start();
        t2.start();
    }

    private void justDoIt(Predicate<Integer> predicate) {
        while (true) {
            try {
                int val = value.get();
                if (predicate.test(val)) {
                    Thread.sleep(1000);
                    log.info("Thread {} with value: {}", Thread.currentThread().getName(), val);
                    if (increment.get()) {
                        if (value.incrementAndGet() >= MAX_VALUE) increment.set(false);
                    } else {
                        if (value.decrementAndGet() <= 1) increment.set(true);
                    }
                }
            } catch (Exception e) {
                log.error("WTF", e);
            }
        }
    }
}
