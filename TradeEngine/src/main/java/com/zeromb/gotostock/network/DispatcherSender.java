package com.zeromb.gotostock.network;

import com.zeromb.gotostock.DispatcherGatewayGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * @author Mr_Told
 */
public class DispatcherSender {

    private final DispatcherGatewayGrpc.DispatcherGatewayBlockingStub blockingStub;
    private final DispatcherGatewayGrpc.DispatcherGatewayStub asyncStub;

    ManagedChannel channel;

    public DispatcherSender(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    /** Construct client for accessing RouteGuide server using the existing channel. */
    public DispatcherSender(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        blockingStub = DispatcherGatewayGrpc.newBlockingStub(channel);
        asyncStub = DispatcherGatewayGrpc.newStub(channel);

    }

}
