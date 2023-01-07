package com.zeromb.gotostock.display.view.line;

import com.zeromb.gotostock.format.TextViewFormatter;
import com.zeromb.gotostock.obj.Portfolio;
import ru.congas.core.output.modifier.Attribute;
import ru.congas.core.output.modifier.Style;
import ru.congas.core.output.widgets.TextView;

/**
 * @author Mr_Told
 */
public class BalanceLineView extends LineView {

    TextViewFormatter formatter = new TextViewFormatter();

    Portfolio portfolio;
    boolean dayDelta;

    TextView nameView = new TextView("Total:", null);
    TextView priceValue = new TextView("???,?? GTB", null);
    TextView priceValueSep = new TextView("|", null);
    TextView priceDeltaVal = new TextView("+ ? GTB", null);
    TextView priceDeltaSep = new TextView("/", null);
    TextView priceDeltaPer = new TextView("?,?? %", null);
    TextView priceDeltaTime = new TextView("за все время", null);

    public BalanceLineView(Portfolio portfolio) {
        this.portfolio = portfolio;

        addView(nameView).addView(priceValue).addView(priceValueSep)
                .addView(priceDeltaVal).addView(priceDeltaSep)
                .addView(priceDeltaPer).addView(priceDeltaTime);

        nameView.setStyle(new Style().addAttribute(Attribute.INTENSITY_BOLD));

        update();
    }

    public void update() {
        formatter.formatValue(priceValue, portfolio.getTotalBalance(),
                dayDelta && portfolio.getDeltaToday() >= 0
                || !dayDelta && portfolio.getDeltaTotal() >= 0);
        formatter.formatDelta(priceDeltaVal,
                dayDelta ? portfolio.getDeltaToday() : portfolio.getDeltaTotal());
        formatter.formatPercents(priceDeltaPer,
                dayDelta ? portfolio.getDeltaTodayPercents() : portfolio.getDeltaTotalPercents());
        priceDeltaTime.setText(dayDelta ? "за сегодня" : "за все время");
    }

    public void setDayDelta(boolean dayDelta) {
        this.dayDelta = dayDelta;
    }

    public boolean isDayDelta() {
        return dayDelta;
    }
}
