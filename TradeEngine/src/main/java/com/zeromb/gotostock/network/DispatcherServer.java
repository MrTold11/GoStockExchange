package com.zeromb.gotostock.network;

import com.zeromb.gotostock.DispatcherGatewayGrpc;
import com.zeromb.gotostock.Gateway;
import com.zeromb.gotostock.trade.Dispatcher;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr_Told
 */
public class DispatcherServer {

    private final Server server;

    public DispatcherServer(String gatewayIp, int gatewayPort, int selfPort) {
        this(selfPort, new Dispatcher(gatewayIp, gatewayPort));
    }

    /** Create a RouteGuide server listening on {@code port} using {@code featureFile} database. */
    public DispatcherServer(int port, Dispatcher dispatcher) {
        this(Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create()),
                dispatcher);
    }

    /** Create a RouteGuide server using serverBuilder as a base and features as data. */
    public DispatcherServer(ServerBuilder<?> serverBuilder, Dispatcher dispatcher) {
        server = serverBuilder.addService(new DispatcherService(dispatcher))
                .build();
    }

    /** Start serving requests. */
    public void start() throws IOException {
        server.start();
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            try {
                DispatcherServer.this.stop();
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

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * Our implementation of RouteGuide service.
     *
     * <p>See route_guide.proto for details of the methods.
     */
    private static class DispatcherService extends DispatcherGatewayGrpc.DispatcherGatewayImplBase {

        private final Dispatcher dispatcher;
        private final Gateway.Empty EMPTY = Gateway.Empty.newBuilder().build();

        DispatcherService(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        @Override
        public void getCurrentPrice(Gateway.ISIN request, StreamObserver<Gateway.StockPrice> responseObserver) {
            responseObserver.onNext(dispatcher.getCurrentPrice(request.getISIN()));
            responseObserver.onCompleted();
        }

        @Override
        public void getOpeningPrice(Gateway.ISIN request, StreamObserver<Gateway.StockPrice> responseObserver) {
            responseObserver.onNext(dispatcher.getOpeningPrice(request.getISIN()));
            responseObserver.onCompleted();
        }

        @Override
        public void placeOrder(Gateway.Order request, StreamObserver<Gateway.Empty> responseObserver) {
            dispatcher.placeOrder(request);
            responseObserver.onNext(EMPTY);
            responseObserver.onCompleted();
        }

        @Override
        public void cancelOrder(Gateway.CancelOrderRequest request, StreamObserver<Gateway.Empty> responseObserver) {
            dispatcher.cancelOrder(request);
            responseObserver.onNext(EMPTY);
            responseObserver.onCompleted();
        }

    }

}
