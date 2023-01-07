package com.zeromb.gotostock.display.view.line;

import com.zeromb.gotostock.format.TextViewFormatter;
import com.zeromb.gotostock.obj.Stock;
import ru.congas.core.output.modifier.Attribute;
import ru.congas.core.output.modifier.Style;
import ru.congas.core.output.widgets.TextView;

/**
 * @author Mr_Told
 */
public class OwnedStockInfoView extends LineView {

    TextViewFormatter formatter = new TextViewFormatter();

    Stock stock;

    TextView nameView = new TextView("Balance:", null);
    TextView amountValue = new TextView("? pcs", null);
    TextView amountValueSep = new TextView("|", null);
    TextView priceBalance = new TextView("Balance price: ", null);
    TextView priceBalanceVal = new TextView("? GTB", null);
    TextView priceValueSep = new TextView("|", null);
    TextView priceDeltaVal = new TextView("+ ? GTB", null);
    TextView priceDeltaSep = new TextView("/", null);
    TextView priceDeltaPer = new TextView("?,?? %", null);
    TextView priceDeltaTime = new TextView("за все время", null);

    boolean displayAmountInfo = true;

    public OwnedStockInfoView(Stock stock) {
        this.stock = stock;

        addAll(nameView, amountValue, amountValueSep, priceBalance,
                priceBalanceVal, priceValueSep, priceDeltaVal,
                priceDeltaSep, priceDeltaPer, priceDeltaTime);

        nameView.setStyle(new Style().addAttribute(Attribute.INTENSITY_BOLD));

        update();
    }

    public void update() {
        formatter.formatAmount(amountValue, stock.getAmount())
                .formatValue(priceBalanceVal, stock.getBalancePrice(),
                        stock.getBalancePrice() <= stock.getCurrentMarketPrice())
                .formatDelta(priceDeltaVal, stock.getDeltaTotal())
                .formatPercents(priceDeltaPer, stock.getDeltaTotalPercents());

        if (stock.getAmount() > 0 && !displayAmountInfo) {
            displayAmountInfo = true;
            clearViews().addAll(nameView, amountValue, amountValueSep, priceBalance,
                    priceBalanceVal, priceValueSep, priceDeltaVal,
                    priceDeltaSep, priceDeltaPer, priceDeltaTime);
        } else if (stock.getAmount() <= 0 && displayAmountInfo) {
            displayAmountInfo = false;
            clearViews().addView(nameView).addView(amountValue);
        }
    }

}
