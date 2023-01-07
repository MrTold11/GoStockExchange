package com.zeromb.gotostock.display.activity;

import com.zeromb.gotostock.display.activity.tab.ExchangeTabActivity;
import com.zeromb.gotostock.display.view.group.MarketView;
import com.zeromb.gotostock.display.view.group.StockView;
import com.zeromb.gotostock.display.view.line.stock.StockLineView;
import ru.congas.core.application.Bundle;

/**
 * @author Mr_Told
 */
public class StockListActivity extends ExchangeTabActivity {

    MarketView marketView = new MarketView(exchange);

    @Override
    protected void onCreate(Bundle args) {
        super.onCreate(args);

        hintView.setText("Use ARROWS to navigate, ENTER to open stock");

        addWidget(marketView).pos().setOffset(4, 3);

        render();
    }

    @Override
    protected StockView<? extends StockLineView> getStockView() {
        return marketView;
    }

}
