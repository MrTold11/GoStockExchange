package com.zeromb.gostock.activity;

import ru.congas.core.application.Bundle;
import ru.congas.core.application.GameActivity;
import ru.congas.core.application.PageActivity;
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

    int step = -1;

    TextView loginView = new TextView("Press ENTER to log in!",
            new Style().setBackground(Color.BLUE));

    @Override
    public void onCreate(Bundle args) {
        super.onCreate(args);

        setTps(3);

        loginView.pos().setGravity(Gravity.center);
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
            Bundle args = new Bundle(getClass());
            args.addExtra("access-token", "test_token");
            openActivity(ExchangeActivity.class, args, true);
            return true;
        }
        return false;
    }

}
