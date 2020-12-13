package ru.otus;

import java.util.List;

public class SampleObject {

    private final int number;
    private final boolean b;
    private final Long l;
    private final String string;
    private final int[] numbers;
    private final List<Double> doubles;

    public SampleObject(int number, boolean b, Long l, String string, int[] numbers, List<Double> doubles) {
        this.number = number;
        this.b = b;
        this.l = l;
        this.string = string;
        this.numbers = numbers;
        this.doubles = doubles;
    }
}
