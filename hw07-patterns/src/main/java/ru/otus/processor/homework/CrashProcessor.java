package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class CrashProcessor implements Processor {

    @Override
    public Message process(Message message) {
        long currentSecond = System.currentTimeMillis() / 1000;
        if (currentSecond % 2 == 0) throw new EvenSecondException(currentSecond);
        return message;
    }
}
