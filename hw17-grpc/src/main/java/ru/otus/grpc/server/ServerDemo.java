package ru.otus.grpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import ru.otus.grpc.generated.NumGenerateServiceGrpc;

import java.io.IOException;

public class ServerDemo {

    public static final int SERVER_PORT = 8090;

    public static void main(String[] args) throws IOException, InterruptedException {
        NumGenerateServiceGrpc.NumGenerateServiceImplBase numGenerateServiceGrpc = new NumGenerateService();

        Server server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(numGenerateServiceGrpc)
                .build();

        server.start();
        System.out.println("server waiting for client connections...");
        server.awaitTermination();
    }
}
