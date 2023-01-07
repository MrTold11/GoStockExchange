package com.zeromb.gotostock;

import com.zeromb.gotostock.network.DispatcherServer;

import java.io.IOException;

/**
 * @author Mr_Told
 */
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        DispatcherServer server = new DispatcherServer(50150);
        server.start();
        server.blockUntilShutdown();
    }
}