package com.zeromb.gotostock;

import com.zeromb.gotostock.display.activity.LoginActivity;
import org.apache.logging.log4j.LogManager;
import ru.congas.core.CongasCore;

/**
 * @author Mr_Told
 */
public class GoStockCLI extends CongasCore {

    public static void main(String[] args) throws Exception {
        logger = LogManager.getLogger(GoStockCLI.class);
        CongasCore.debug = false;
        new GoStockCLI();
    }

    public GoStockCLI() throws Exception {
        super();
        setTitle("GoTo Stock Exchange CLI");
        openActivity(LoginActivity.class, null, null);
    }

    @Override
    protected void terminalNotSupported() {
        Runtime.getRuntime().removeShutdownHook(shutdownThread);
        //todo dumb terminal
    }
}
