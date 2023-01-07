package com.zeromb.gotostock.display.view.line.stock;

import com.zeromb.gotostock.obj.Stock;
import ru.congas.core.output.widgets.TextView;

/**
 * @author Mr_Told
 */
public class OwnedStockLineView extends StockLineView {

    TextView amountSep = new TextView("|", null);
    TextView amount = new TextView("?? pcs", null);
    boolean dayDelta;

    public OwnedStockLineView(Stock stock) {
        super(stock);
        addView(amountSep).addView(amount);
    }

    @Override
    public void update() {
        formatter.formatValue(priceValue, stock.getCurrentMarketPrice() * stock.getAmount(),
                dayDelta && stock.getDeltaTodayOwned() >= 0
                        || !dayDelta && stock.getDeltaTotal() >= 0)
                .formatDelta(priceDeltaVal,
                dayDelta ? stock.getDeltaTodayOwned() : stock.getDeltaTotal())
                .formatPercents(priceDeltaPer,
                dayDelta ? stock.getDeltaTodayPercents() : stock.getDeltaTotalPercents())
                .formatAmount(amount, stock.getAmount());
    }

    public void setDayDelta(boolean dayDelta) {
        this.dayDelta = dayDelta;
    }

}
