package com.zeromb.gotostock.display.activity;

import com.zeromb.gotostock.display.view.line.OwnedStockInfoView;
import com.zeromb.gotostock.display.view.line.stock.StockLineView;
import com.zeromb.gotostock.network.NetworkProvider;
import com.zeromb.gotostock.obj.Stock;
import ru.congas.core.application.Bundle;
import ru.congas.core.application.PageActivity;
import ru.congas.core.input.keys.KeyPressed;
import ru.congas.core.output.modifier.Color;
import ru.congas.core.output.modifier.Style;
import ru.congas.core.output.widgets.TextView;
import ru.congas.core.output.widgets.properties.Gravity;

import javax.annotation.Nullable;

/**
 * @author Mr_Told
 */
public class StockInfoActivity extends PageActivity implements Updatable {

    NetworkProvider network;
    Stock stock;

    TextView labelView, hintView;
    StockLineView lineView;
    OwnedStockInfoView stockOwnInfo;

    @Override
    protected void onCreate(@Nullable Bundle args) {
        super.onCreate(args);

        if (args != null) {
            network = (NetworkProvider) args.getObject("network-provider", NetworkProvider.class, null);
            stock = (Stock) args.getObject("stock", Stock.class, null);
        }

        if (stock == null)
            throw new RuntimeException("Stock activity open without Stock argument");
        if (network == null)
            throw new RuntimeException("Stock activity open without Network Provider argument");

        labelView = new TextView("Stock Page: " + stock.getName()
                + " (" + stock.getTicker() + "). ISIN: " + stock.getISIN(),
                new Style().setBackground(1528060).setForeground(Color.WHITE));
        lineView = new StockLineView(stock);
        lineView.addView(new TextView("|", null))
                .addView(new TextView("Opening price: ", null))
                .addView(new TextView(stock.getOpenMarketPrice() + " GTB", new Style().setForeground(Color.WHITE)));
        stockOwnInfo = new OwnedStockInfoView(stock);

        hintView = new TextView("Use ARROWS to navigate/select, press ENTER to process, ESC to go back",
                new Style().setBackground(Color.GRAY));

        addWidget(labelView).pos().setOffset(2, 1);
        addWidget(lineView).pos().setOffset(4, 3);
        addWidget(stockOwnInfo).pos().setOffset(4, 5);
        addWidget(hintView).pos().setGravity(Gravity.leftBottom);

        updateActivity();
    }

    @Override
    protected void onResume(@Nullable Bundle extra) {
        super.onResume(extra);
        network.subscribeStock(stock);
    }

    @Override
    protected void onPause() {
        super.onPause();
        network.unsubscribeStock(stock);
    }

    @Override
    protected void render() {
        getCanvas().clear();
        super.render();
    }

    @Override
    public boolean handle(KeyPressed event) {
        switch (event.getDefinedKey()) {
            case ESCAPE -> {
                closeActivity();
                return true;
            }
        }
        return super.handle(event);
    }

    @Override
    public void updateActivity() {
        runOnUiThread(() -> {
            stockOwnInfo.update();
            lineView.update();
            render();
        });
    }

}
