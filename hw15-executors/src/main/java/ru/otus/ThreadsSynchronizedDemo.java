package ru.otus;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

public class ThreadsSynchronizedDemo {

    private final static Logger log = LoggerFactory.getLogger(ThreadsSynchronizedDemo.class);
    private final static String THREAD_NAME_1 = "Thread_1";
    private final static String THREAD_NAME_2 = "Thread_2";
    private final static int MAX_VALUE = 10;
    private int value = 1;
    private boolean increment = true;

    public static void main(String... args) {
        ThreadsSynchronizedDemo td = new ThreadsSynchronizedDemo();

        Thread t1 = new Thread(() -> td.justDoIt(integer -> integer % 2 != 0));
        t1.setName(THREAD_NAME_1);

        Thread t2 = new Thread(() -> td.justDoIt(integer -> integer != 0 && integer % 2 == 0));
        t2.setName(THREAD_NAME_2);

        t1.start();
        t2.start();
    }

    private synchronized void justDoIt(Predicate<Integer> predicate) {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            try {
                while (!predicate.test(value)) {
                    this.wait();
                }
                Thread.sleep(1000);
                log.info("Thread {} with value: {}", Thread.currentThread().getName(), value);
                if (increment) {
                    value++;
                    if (value == MAX_VALUE) increment = false;
                } else {
                    value--;
                    if (value == 1) increment = true;
                }
                this.notifyAll();
            } catch (Exception e) {
                log.error("WTF", e);
            }
        }
    }
}
