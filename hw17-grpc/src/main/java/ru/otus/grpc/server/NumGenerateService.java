package ru.otus.grpc.server;

import io.grpc.stub.StreamObserver;
import ru.otus.grpc.generated.GeneratedNumResponse;
import ru.otus.grpc.generated.NumGenerateRequest;
import ru.otus.grpc.generated.NumGenerateServiceGrpc;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class NumGenerateService extends NumGenerateServiceGrpc.NumGenerateServiceImplBase {

    private final static long EMIT_INTERVAL_IN_SEC = 2;
    private final AtomicInteger integer = new AtomicInteger();

    @Override
    public void generate(NumGenerateRequest request, StreamObserver<GeneratedNumResponse> responseObserver) {
        integer.set(request.getFirstValue());
        ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);
        scheduler.scheduleWithFixedDelay(() -> {
            responseObserver.onNext(GeneratedNumResponse.newBuilder().setValue(integer.incrementAndGet()).build());
            if (integer.get() == request.getLastValue()) {
                responseObserver.onCompleted();
                scheduler.shutdown();
            }
        }, EMIT_INTERVAL_IN_SEC, EMIT_INTERVAL_IN_SEC, TimeUnit.SECONDS);
    }
}
