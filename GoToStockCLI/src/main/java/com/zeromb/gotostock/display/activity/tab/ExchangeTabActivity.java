package com.zeromb.gotostock.display.activity.tab;

import com.zeromb.gotostock.display.activity.PortfolioActivity;
import com.zeromb.gotostock.display.activity.StockInfoActivity;
import com.zeromb.gotostock.display.activity.StockMarketActivity;
import com.zeromb.gotostock.display.activity.Updatable;
import com.zeromb.gotostock.display.view.group.StockView;
import com.zeromb.gotostock.display.view.line.LineView;
import com.zeromb.gotostock.display.view.line.stock.StockLineView;
import com.zeromb.gotostock.network.NetworkProvider;
import com.zeromb.gotostock.obj.Stock;
import com.zeromb.gotostock.obj.StockExchange;
import ru.congas.core.application.Bundle;
import ru.congas.core.application.PageActivity;
import ru.congas.core.input.keys.KeyPressed;
import ru.congas.core.output.modifier.Color;
import ru.congas.core.output.modifier.Style;
import ru.congas.core.output.widgets.TextView;
import ru.congas.core.output.widgets.properties.Gravity;
import ru.congas.core.output.widgets.properties.WidgetSizeType;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Mr_Told
 */
public abstract class ExchangeTabActivity extends PageActivity implements Updatable {

    public static final StockExchange exchange = new StockExchange();

    private static final Map<Class<? extends ExchangeTabActivity>, ActivityTab> tabs = new HashMap<>();

    private static final LineView tabsView = new LineView();

    static {
        addTab(new ActivityTab(PortfolioActivity.class, null, StockMarketActivity.class, Color.BLUE, "Portfolio"));
        addTab(new ActivityTab(StockMarketActivity.class, PortfolioActivity.class, null, Color.MAROON, "Market"));

        tabsView.pos().setGravity(Gravity.centerBottom);
        tabsView.setSeparator("   ");

        for (ActivityTab tab = tabs.values().stream()
                .filter(t -> t.leftClazz == null)
                .findAny().orElseThrow();
             tab != null;
             tab = tabs.get(tab.rightClazz)
        ) {
            tabsView.addView(tab.getTextView());
        }

        exchange.getPortfolio().getCoins().setAmount(300000);
    }

    private static void addTab(ActivityTab tab) {
        tabs.put(tab.clazz, tab);
    }

    protected NetworkProvider network;

    protected TextView exchangeView, hintView;
    int pointer = 0;
    ActivityTab current;

    @Override
    protected void onCreate(@Nullable Bundle args) {
        super.onCreate(args);

        if (args != null)
            network = (NetworkProvider) args.getObject("network-provider", NetworkProvider.class, null);

        if (network == null)
            throw new RuntimeException("Exchange tab activity open without Network Provider argument");

        current = tabs.get(getClass());
        if (current == null)
            throw new RuntimeException("Exchange Tab wasn't found for: " + getClass().getName());

        exchangeView = new TextView("GOTO EXCHANGE", new Style());
        exchangeView.setBackgroundStyle(exchangeView.getStyle()
                .setBackground(current.color));

        hintView = new TextView("This is a hint, probably needs to be filled",
                new Style().setBackground(Color.GRAY));

        addWidget(exchangeView).pos()
                .setGravity(Gravity.centerTop)
                .setOffsetY(1)
                .setWidgetHeight(3)
                .setWidthType(WidgetSizeType.match_parent);

        addWidget(hintView).pos()
                .setGravity(Gravity.leftBottom)
                .setOffset(1, -2);

        addWidget(tabsView);

        updatePointer();
        render();
    }

    @Override
    protected void onResume(@Nullable Bundle extra) {
        super.onResume(extra);
        network.resubscribeStocks(
                getStockView().getStockViews()
                        .stream().map(StockLineView::getStock)
                        .collect(Collectors.toSet())
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        network.resubscribeStocks();
    }

    @Override
    public boolean handle(KeyPressed event) {
        switch (event.getDefinedKey()) {
            case LEFT -> {
                if (current.leftClazz != null)
                    openActivity(
                            current.leftClazz,
                            new Bundle()
                                    .addExtra("network-provider", network),
                            true);

                return true;
            }
            case RIGHT -> {
                if (current.rightClazz != null) {
                    openActivity(
                            current.rightClazz,
                            new Bundle()
                                    .addExtra("network-provider", network),
                            true);
                }
                return true;
            }
            case UP -> {
                if (pointer == 0)
                    return true;
                pointer--;
                updatePointer();
                return true;
            }
            case DOWN -> {
                if (pointer == getStockView().getStockViews().size() - 1)
                    return true;
                pointer++;
                updatePointer();
                return true;
            }
            case ENTER -> {
                Stock selected = getSelectedStock();
                if (selected == null)
                    return false;
                openActivity(
                        StockInfoActivity.class,
                        new Bundle()
                                .addExtra("stock", selected)
                                .addExtra("network-provider", network),
                        false);
                return true;
            }
            case ESCAPE -> {
                closeActivity();
                return true;
            }
        }
        return false;
    }

    private void updatePointer() {
        int i = -1;
        for (StockLineView stockView : getStockView().getStockViews()) {
            i++;
            stockView.getTickerStyle().setBackground(
                    i == pointer ? Color.MAROON : Color.GRAY);
        }
        render();
    }

    protected Stock getSelectedStock() {
        int i = -1;
        for (StockLineView stockView : getStockView().getStockViews()) {
            i++;
            if (i == pointer)
                return stockView.getStock();
        }
        return null;
    }

    @Override
    protected void render() {
        getCanvas().clear();

        if (getCanvas().getHeight() == 0 || getCanvas().getWidth() == 0)
            return;

        int top = 0, down = Math.max(getCanvas().getHeight() - 2, 0);
        int left = 0, right = Math.max(getCanvas().getWidth() - 1, 0);

        for (int i = 0; i <= right; i++) {
            getCanvas().getCell(top, i).setBackground(current.color);
            getCanvas().getCell(down, i).setBackground(current.color);
        }

        for (int i = 0; i <= down; i++) {
            getCanvas().getCell(i, left).setBackground(current.color);
            getCanvas().getCell(i, right).setBackground(current.color);
        }

        super.render();
    }

    protected abstract StockView<? extends StockLineView> getStockView();

    @Override
    public void updateActivity() {
        runOnUiThread(() -> {
            getStockView().update();
            render();
        });
    }
}
