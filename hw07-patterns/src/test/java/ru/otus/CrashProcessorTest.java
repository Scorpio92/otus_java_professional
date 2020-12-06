package ru.otus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import ru.otus.model.Message;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.CrashProcessor;
import ru.otus.processor.homework.EvenSecondException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.mock;

public class CrashProcessorTest {

    @RepeatedTest(100)
    @DisplayName("Тестирование краша на чётной секунде")
    public void evenSecondCrashTest() {
        Message message = mock(Message.class);
        Processor processor = new CrashProcessor();
        await().until(() -> System.currentTimeMillis() / 1000 % 2 == 0);
        assertThatExceptionOfType(EvenSecondException.class).isThrownBy(() -> processor.process(message));
    }
}
