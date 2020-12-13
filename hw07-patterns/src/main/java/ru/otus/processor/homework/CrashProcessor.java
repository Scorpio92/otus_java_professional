package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.LocalDateTime;
import java.util.function.Predicate;

public class CrashProcessor implements Processor {

    private final DateTimeProvider dateTimeProvider;
    private final Predicate<LocalDateTime> condition = dt -> dt.getSecond() % 2 == 0;

    public CrashProcessor(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        if (condition.test(dateTimeProvider.getDate())) {
            throw new EvenSecondException(dateTimeProvider.getDate().getSecond());
        }
        return message;
    }
}
