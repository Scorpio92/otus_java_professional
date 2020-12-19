package ru.otus;

import com.google.gson.Gson;
import ru.otus.base.MyGson;

import java.util.Arrays;

public class Demo {

    public static void main(String... args) {
        SampleObject sampleObject = new SampleObject(
                1,
                true,
                100500L,
                "this is test",
                new int[]{1, 2, 3},
                Arrays.asList(1d, 2d, 3d)
        );

        Gson gson = new Gson();
        String gsonString = gson.toJson(sampleObject);
        System.out.println("Gson: " + gsonString);

        MyGson myGson = new MyGson();
        String myGsonString = myGson.toJson(sampleObject);
        System.out.println("MyGson: " + myGsonString);

        System.out.println("Is equals: " + gsonString.equals(myGsonString));
    }
}
