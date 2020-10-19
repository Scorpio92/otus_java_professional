package ru.otus.hw3;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class DIYarrayListDemo {

    public static void main(String... args) {
        List<String> listAddAll = new DIYarrayList<>();

        System.out.println("Test Collections.addAll():\n");
        Collections.addAll(listAddAll, generateArray(32));
        System.out.println(listAddAll);

        System.out.println("\nTest Collections.copy():\n");
        List<String> listCopyDest = new DIYarrayList<>();
        Collections.addAll(listCopyDest, generateArray(32));
        Collections.copy(listCopyDest, listAddAll);
        System.out.println(listCopyDest);

        System.out.println("\nTest Collections.sort():\n");
        Collections.sort(listCopyDest, Comparator.comparing(Integer::valueOf));
        System.out.println(listCopyDest);
    }

    private static String[] generateArray(int size) {
        String[] strings = new String[size];
        for (int i = 0; i < size; i++) {
            strings[i] = String.valueOf(new Random().nextInt(size + 1));
        }
        return strings;
    }
}
