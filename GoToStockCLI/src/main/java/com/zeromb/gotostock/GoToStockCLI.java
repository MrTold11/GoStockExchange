package com.zeromb.gotostock;

import com.zeromb.gotostock.display.activity.LoginActivity;
import com.zeromb.gotostock.display.activity.Updatable;
import com.zeromb.gotostock.display.activity.tab.ExchangeTabActivity;
import com.zeromb.gotostock.network.NetUpdateHandler;
import com.zeromb.gotostock.network.NetworkProvider;
import org.apache.logging.log4j.LogManager;
import ru.congas.core.CongasCore;
import ru.congas.core.application.Bundle;

/**
 * @author Mr_Told
 */
public class GoToStockCLI extends CongasCore implements NetUpdateHandler {

    NetworkProvider networkProvider;

    public static void main(String[] args) throws Exception {
        logger = LogManager.getLogger(GoToStockCLI.class);
        CongasCore.debug = false;
        new GoToStockCLI();
    }

    public GoToStockCLI() throws Exception {
        super();
        setTitle("GoTo Stock Exchange CLI");
        this.networkProvider = new NetworkProvider("localhost", this, ExchangeTabActivity.exchange);
        openActivity(
                LoginActivity.class,
                new Bundle()
                        .addExtra("network-provider", networkProvider),
                null);
    }

    @Override
    public void updateCurrentActivity() {
        if (current instanceof Updatable)
            ((Updatable) current).updateActivity();
    }

    @Override
    protected void terminalNotSupported() {
        Runtime.getRuntime().removeShutdownHook(shutdownThread);
        //todo dumb terminal
    }

    protected void close() {
        if (run) {
            try {
                networkProvider.close();
            } catch (Throwable t) {
                logger.error("Error during Network Provider close", t);
            }
        }
        //super.close();
    }

}
