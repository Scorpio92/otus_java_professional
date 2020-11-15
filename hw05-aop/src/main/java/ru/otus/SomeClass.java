package ru.otus;

public class SomeClass {

    @Log
    public void justDoIt() {
        System.out.println("simple justDoIt");
    }

    @Log
    public void justDoIt(String message) {
        System.out.printf("message [%s]%n", message);
    }

    @Log
    public void justDoIt(String message, int count) {
        System.out.printf("message [%s], count [%d]%n", message, count);
    }

    @Log
    public void justDoIt(String message, int count, int count2) {
        System.out.printf("message [%s], count [%d], count2 [%d]%n", message, count, count2);
    }
}
