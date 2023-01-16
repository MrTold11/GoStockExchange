package com.zeromb.gotostock.network;

import com.zeromb.gotostock.CLIServiceGrpc;
import com.zeromb.gotostock.Gateway;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

/**
 * @author Mr_Told
 */
public class ClientReceiver {

    private final Server server;

    public ClientReceiver(int port, NetUpdateHandler handler) {
        this(Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create()),
                handler);
    }

    public ClientReceiver(ServerBuilder<?> serverBuilder, NetUpdateHandler handler) {
        server = serverBuilder.addService(new CLIService(handler))
                .build();
    }

    /** Start serving requests. */
    public void start() {
        try {
            server.start();
        } catch (Throwable t) {
            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            try {
                ClientReceiver.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("*** server shut down");
        }));
    }

    /** Stop serving requests and shutdown resources. */
    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private static class CLIService extends CLIServiceGrpc.CLIServiceImplBase {

        private final NetUpdateHandler updateHandler;
        private final Gateway.Empty EMPTY = Gateway.Empty.newBuilder().build();

        CLIService(NetUpdateHandler handler) {
            this.updateHandler = handler;
        }

        @Override
        public void receiveOrderStatus(Gateway.OrderStatus request, StreamObserver<Gateway.Empty> responseObserver) {
            //todo
            updateHandler.updateCurrentActivity();
            responseObserver.onNext(EMPTY);
            responseObserver.onCompleted();
        }

    }

}
