package com.zeromb.gotostock.display.activity;

import com.zeromb.gotostock.display.view.line.stock.StockLineView;
import com.zeromb.gotostock.obj.Stock;
import ru.congas.core.application.Bundle;
import ru.congas.core.application.PageActivity;

/**
 * @author Mr_Told
 */
public class StockActivity extends PageActivity {

    Stock stock;

    StockLineView lineView;

    @Override
    protected void onCreate(Bundle args) {
        super.onCreate(args);

        if (args != null)
            stock = (Stock) args.getObject("stock", Stock.class, null);

        if (stock == null)
            throw new RuntimeException("Stock activity open without correct arguments");

        lineView = new StockLineView(stock);
    }

    @Override
    protected void render() {
        super.render();
    }

}
