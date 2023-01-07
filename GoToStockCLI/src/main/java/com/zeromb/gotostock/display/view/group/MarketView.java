package com.zeromb.gotostock.display.view.group;

import com.zeromb.gotostock.display.view.line.stock.StockLineView;
import com.zeromb.gotostock.obj.StockExchange;

/**
 * @author Mr_Told
 */
public class MarketView extends StockView<StockLineView> {

    public MarketView(StockExchange exchange) {
        super(exchange.getStocks(), StockLineView::new);
        update();
    }

}
