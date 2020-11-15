package ru.otus;

public class AOPDemo {

    public static void main(String... args) {
        SomeClass proxy = Ioc.get();
        proxy.justDoIt();
        proxy.justDoIt("test");
        proxy.justDoIt("test", 3);
        proxy.justDoIt("test", 3, 4);
    }
}
