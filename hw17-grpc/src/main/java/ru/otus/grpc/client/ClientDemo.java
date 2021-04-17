package ru.otus.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.grpc.generated.GeneratedNumResponse;
import ru.otus.grpc.generated.NumGenerateRequest;
import ru.otus.grpc.generated.NumGenerateServiceGrpc;

import java.util.OptionalInt;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientDemo {

    private final static Logger log = LoggerFactory.getLogger(ClientDemo.class);

    private final static long DELAY_INTERVAL = 1;
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8090;

    private final CountDownLatch latch = new CountDownLatch(1);
    private final AtomicInteger cycleCount = new AtomicInteger(0);
    private Integer lastNumFromServer = null;
    private final AtomicInteger currentValue = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        new ClientDemo().start();
    }

    private void start() throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        NumGenerateServiceGrpc.NumGenerateServiceStub stub = NumGenerateServiceGrpc.newStub(channel);

        ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);
        scheduler.scheduleWithFixedDelay(() -> {
            currentValue.set(currentValue.get() + getLastNumFromServer().orElse(0) + 1);
            log.debug("Cycle №{}, current value: {}", cycleCount.incrementAndGet(), currentValue.get());
            if (cycleCount.get() == 50) scheduler.shutdown();
        }, 0, DELAY_INTERVAL, TimeUnit.SECONDS);

        stub.generate(NumGenerateRequest.newBuilder().setFirstValue(0).setLastValue(30).build(), new StreamObserver<>() {
            @Override
            public void onNext(GeneratedNumResponse value) {
                log.debug("Generated value from server: {}", value.getValue());
                setLastNumFromServer(value.getValue());
            }

            @Override
            public void onError(Throwable t) {
                log.error("Generate num error", t);
            }

            @Override
            public void onCompleted() {
                log.debug("Completed");
                latch.countDown();
            }
        });

        latch.await();
        channel.shutdown();
    }

    public synchronized void setLastNumFromServer(Integer lastNumFromServer) {
        this.lastNumFromServer = lastNumFromServer;
    }

    public synchronized OptionalInt getLastNumFromServer() {
        if (lastNumFromServer == null) return OptionalInt.empty();
        int val = lastNumFromServer;
        lastNumFromServer = null;
        return OptionalInt.of(val);
    }
}
