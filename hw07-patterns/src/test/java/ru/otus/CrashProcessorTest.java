package ru.otus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import ru.otus.model.Message;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.CrashProcessor;
import ru.otus.processor.homework.EvenSecondException;

import java.time.LocalDateTime;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.mock;

public class CrashProcessorTest {

    @RepeatedTest(100)
    @DisplayName("Тестирование краша на чётной секунде")
    public void evenSecondCrashTest() {
        Predicate<LocalDateTime> predicate = dt -> dt.getSecond() % 2 == 0;

        Message message = mock(Message.class);
        await().until(() -> predicate.test(LocalDateTime.now()));
        Processor processor = new CrashProcessor(LocalDateTime.now());
        assertThatExceptionOfType(EvenSecondException.class).isThrownBy(() -> processor.process(message));
    }
}
