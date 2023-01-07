package com.zeromb.gotostock.display.view.line.stock;

import com.zeromb.gotostock.display.view.line.LineView;
import com.zeromb.gotostock.format.TextViewFormatter;
import com.zeromb.gotostock.obj.Stock;
import ru.congas.core.output.modifier.Attribute;
import ru.congas.core.output.modifier.Color;
import ru.congas.core.output.modifier.Style;
import ru.congas.core.output.widgets.TextView;

/**
 * @author Mr_Told
 */
public class StockLineView extends LineView implements Comparable<StockLineView> {

    TextViewFormatter formatter = new TextViewFormatter();

    Stock stock;

    TextView tickerView = new TextView("NAME", null);
    TextView priceValue = new TextView("???,?? GTB", null);
    TextView priceValueSep = new TextView("|", null);
    TextView priceDeltaVal = new TextView("+ ? GTB", null);
    TextView priceDeltaSep = new TextView("/", null);
    TextView priceDeltaPer = new TextView("?,?? %", null);

    public StockLineView(Stock stock) {
        super(null);
        this.stock = stock;

        addView(tickerView).addView(priceValue).addView(priceValueSep)
                .addView(priceDeltaVal).addView(priceDeltaSep).addView(priceDeltaPer);
        tickerView.setText(stock.getTicker());
        tickerView.setStyle(new Style()
                .addAttribute(Attribute.INTENSITY_BOLD)
                .setBackground(Color.GRAY));
    }

    public Style getTickerStyle() {
        return tickerView.getStyle();
    }

    public void update() {
        tickerView.getStyle().setForeground(stock.getAmount() > 0 ?
                Color.GOLD : Color.WHITE);
        formatter.formatValue(priceValue, stock.getCurrentMarketPrice())
                .formatDelta(priceDeltaVal, stock.getDeltaTodayOne())
                .formatPercents(priceDeltaPer, stock.getDeltaTodayPercents());
    }

    public int getPriceSize() {
        return priceValue.getText().length();
    }

    public void addPriceSpace(int spaces) {
        if (spaces < 1) return;

        priceValue.setText(" ".repeat(spaces) + priceValue.getText());
    }

    public int getDeltaSize() {
        return priceDeltaVal.getText().length();
    }

    public void addDeltaSpace(int spaces) {
        if (spaces < 1) return;

        priceDeltaVal.setText(" ".repeat(spaces) + priceDeltaVal.getText());
    }

    public Stock getStock() {
        return stock;
    }

    @Override
    public int compareTo(StockLineView o) {
        return stock.compareTo(o.stock);
    }
}
