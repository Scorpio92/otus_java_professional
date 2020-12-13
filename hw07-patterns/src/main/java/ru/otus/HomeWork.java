package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinter;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.listener.homework.MessageStorage;
import ru.otus.listener.homework.MemoryMessageStorage;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.ProcessorConcatFields;
import ru.otus.processor.ProcessorUpperField10;
import ru.otus.processor.homework.CrashProcessor;
import ru.otus.processor.homework.ReplaceFieldsProcessor;

import java.time.LocalDateTime;
import java.util.List;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
       2. Сделать процессор, который поменяет местами значения field11 и field12
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
       4. Сделать Listener для ведения истории: старое сообщение - новое (подумайте, как сделать, чтобы сообщения не портились)
     */

    public static void main(String[] args) {
        /*
           по аналогии с Demo.class
           из элеменов "to do" создать new ComplexProcessor и обработать сообщение
         */
        MessageStorage messageStorage = new MemoryMessageStorage();

        var processors = List.of(
                new ProcessorConcatFields(),
                new ProcessorUpperField10(),
                new ReplaceFieldsProcessor(),
                new CrashProcessor(LocalDateTime::now)
        );

        var complexProcessor = new ComplexProcessor(processors, Throwable::printStackTrace);
        var listenerPrinter = new ListenerPrinter();
        var historyListener = new HistoryListener(messageStorage);

        complexProcessor.addListener(listenerPrinter);
        complexProcessor.addListener(historyListener);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .field13(new ObjectForMessage())
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);
        System.out.println("message storage: " + messageStorage.elements());

        complexProcessor.removeListener(listenerPrinter);
        complexProcessor.removeListener(historyListener);
    }
}
