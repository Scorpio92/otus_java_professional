package ru.otus;

import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

public class HelloOtus {

    public static void main(String... args) {
        System.out.println(Hashing.sha512().hashString("test", Charset.defaultCharset()));
    }
}
