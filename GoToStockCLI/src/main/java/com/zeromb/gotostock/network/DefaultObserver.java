package com.zeromb.gotostock.network;

import io.grpc.stub.StreamObserver;

/**
 * @author Mr_Told
 */
public class DefaultObserver<V> implements StreamObserver<V> {

    @Override
    public void onNext(V value) {}

    @Override
    public void onError(Throwable t) {}

    @Override
    public void onCompleted() {}

}
