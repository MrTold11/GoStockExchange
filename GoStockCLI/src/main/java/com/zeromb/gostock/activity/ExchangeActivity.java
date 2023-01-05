package com.zeromb.gostock.activity;

import ru.congas.core.application.Bundle;
import ru.congas.core.application.PageActivity;
import ru.congas.core.input.keys.KeyPressed;
import ru.congas.core.output.modifier.Color;
import ru.congas.core.output.modifier.Style;
import ru.congas.core.output.widgets.TextView;
import ru.congas.core.output.widgets.properties.Gravity;

/**
 * @author Mr_Told
 */
public class ExchangeActivity extends PageActivity {

    TextView balanceView = new TextView("Активы: ", null);
    TextView balanceValue = new TextView("300.000 GTB",
            new Style().setForeground(Color.GREEN));

    @Override
    protected void onCreate(Bundle args) {
        super.onCreate(args);

        addWidget(balanceView).pos().setGravity(Gravity.leftTop)
                .setOffsetY(1);
        addWidget(balanceValue).pos().setGravity(Gravity.leftTop)
                .setOffsetX(balanceView.getText().length() +
                        balanceView.pos().getXCoordinate(0, getCanvas().getWidth()))
                .setOffsetY(1);

        render();
    }

    @Override
    public boolean handle(KeyPressed event) {
        return false;
    }

    @Override
    protected void render() {
        super.render();
    }
}
