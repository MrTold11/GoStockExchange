package com.zeromb.gotostock.network;

import com.zeromb.gotostock.Gateway;
import com.zeromb.gotostock.GatewayReceiverGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * @author Mr_Told
 */
public class DispatcherSender {

    private final GatewayReceiverGrpc.GatewayReceiverStub asyncStub;

    ManagedChannel channel;

    final StreamObserver<Gateway.Empty> emptyObserver = new StreamObserver<>() {
        @Override
        public void onNext(Gateway.Empty value) {}

        @Override
        public void onError(Throwable t) {}

        @Override
        public void onCompleted() {}
    };

    public DispatcherSender(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    /** Construct client for accessing RouteGuide server using the existing channel. */
    public DispatcherSender(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        asyncStub = GatewayReceiverGrpc.newStub(channel);
    }

    public void sendResult(Gateway.OrderStatus status) {
        asyncStub.sendOrderStatus(status, emptyObserver);
    }

}
