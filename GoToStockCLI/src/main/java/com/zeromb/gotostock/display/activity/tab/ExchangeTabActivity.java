package com.zeromb.gotostock.display.activity.tab;

import com.zeromb.gotostock.display.activity.PortfolioActivity;
import com.zeromb.gotostock.display.activity.StockActivity;
import com.zeromb.gotostock.display.activity.StockListActivity;
import com.zeromb.gotostock.display.view.group.StockView;
import com.zeromb.gotostock.display.view.line.LineView;
import com.zeromb.gotostock.display.view.line.stock.StockLineView;
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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr_Told
 */
public abstract class ExchangeTabActivity extends PageActivity {

    protected static final StockExchange exchange = new StockExchange();

    private static final Map<Class<? extends ExchangeTabActivity>, ActivityTab> tabs = new HashMap<>();

    private static final LineView tabsView = new LineView(null);

    static {
        addTab(new ActivityTab(PortfolioActivity.class, null, StockListActivity.class, Color.BLUE, "Portfolio"));
        addTab(new ActivityTab(StockListActivity.class, PortfolioActivity.class, null, Color.CORAL, "Market"));

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

    protected TextView exchangeView, hintView;
    int pointer = 0;
    ActivityTab current;

    @Override
    protected void onCreate(Bundle args) {
        super.onCreate(args);

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
    public boolean handle(KeyPressed event) {
        switch (event.getDefinedKey()) {
            case LEFT:
                if (current.leftClazz != null)
                    openActivity(current.leftClazz, null, true);
                return true;
            case RIGHT:
                if (current.rightClazz != null)
                    openActivity(current.rightClazz, null, true);
                return true;
            case UP:
                if (pointer == 0)
                    return true;
                pointer--;
                updatePointer();
                return true;
            case DOWN:
                if (pointer == getStockView().getStockViews().size() - 1)
                    return true;
                pointer++;
                updatePointer();
                return true;
            case ENTER:
                Stock selected = getSelectedStock();
                if (selected == null)
                    return false;

                openActivity(
                        StockActivity.class,
                        new Bundle().addExtra("stock", selected),
                        false);
                return true;
            case ESCAPE:
                closeActivity();
                return true;
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

}
