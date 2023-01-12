package com.zeromb.gotostock.display.activity;

import com.zeromb.gotostock.network.NetworkProvider;
import ru.congas.core.application.Bundle;
import ru.congas.core.application.GameActivity;
import ru.congas.core.input.keys.Key;
import ru.congas.core.input.keys.KeyPressed;
import ru.congas.core.output.modifier.Color;
import ru.congas.core.output.modifier.Style;
import ru.congas.core.output.widgets.TextView;
import ru.congas.core.output.widgets.properties.Gravity;

/**
 * @author Mr_Told
 */
public class LoginActivity extends GameActivity {

    NetworkProvider networkProvider;

    int step = -1;

    TextView loginView = new TextView("Press ENTER to log in!",
            new Style().setBackground(Color.BLUE));

    @Override
    public void onCreate(Bundle args) {
        super.onCreate(args);

        if (args != null)
            networkProvider = (NetworkProvider) args.getObject("network-provider", NetworkProvider.class, null);

        if (networkProvider == null)
            throw new RuntimeException("Login activity open without Network Provider argument");

        setTps(3);
        loginView.pos().setGravity(Gravity.center);

        networkProvider.updateStocks();
        networkProvider.refreshOpeningPrices();
    }

    @Override
    public void onMainLoop() {
        step++;
        if (step == 0) loginView.pos().setOffsetY(-1);
        if (step == 1) loginView.pos().setOffsetY(0);
        if (step == 2) step = -1;

        runOnUiThread(() -> {
            getCanvas().clear();
            loginView.render(getCanvas());
            screen.updateCanvas();
        });
    }

    @Override
    public boolean handle(KeyPressed event) {
        if (event.getDefinedKey() == Key.ENTER) {
            networkProvider.setToken("test_token");
            networkProvider.startStatusReceiver();

            Bundle args = new Bundle(getClass())
                    .addExtra("access-token", "test_token")
                    .addExtra("network-provider", networkProvider);
            openActivity(PortfolioActivity.class, args, false);
            return true;
        }
        return false;
    }

}
