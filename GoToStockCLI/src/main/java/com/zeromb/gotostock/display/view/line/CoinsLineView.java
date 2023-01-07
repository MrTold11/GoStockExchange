package com.zeromb.gotostock.display.view.line;

import com.zeromb.gotostock.format.TextViewFormatter;
import com.zeromb.gotostock.obj.GotoCoin;
import ru.congas.core.output.modifier.Attribute;
import ru.congas.core.output.modifier.Style;
import ru.congas.core.output.widgets.TextView;

/**
 * @author Mr_Told
 */
public class CoinsLineView extends LineView {

    TextViewFormatter formatter = new TextViewFormatter();

    GotoCoin coin;

    TextView nameView = new TextView("Balance:", null);
    TextView priceValue = new TextView("???,?? GTB", null);

    public CoinsLineView(GotoCoin coin) {
        this.coin = coin;

        addView(nameView).addView(priceValue);

        nameView.setStyle(new Style().addAttribute(Attribute.INTENSITY_BOLD));

        update();
    }

    public void update() {
        formatter.formatValue(priceValue, coin.getAmount());
    }

}
