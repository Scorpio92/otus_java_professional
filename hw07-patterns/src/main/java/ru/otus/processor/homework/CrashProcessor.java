package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.LocalDateTime;
import java.util.function.Predicate;

public class CrashProcessor implements Processor {

    private final LocalDateTime dateTime;
    private final Predicate<LocalDateTime> condition = dt -> dt.getSecond() % 2 == 0;

    public CrashProcessor(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public Message process(Message message) {
        if (condition.test(dateTime)) throw new EvenSecondException(dateTime.getSecond());
        return message;
    }
}
