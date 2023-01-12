package com.zeromb.gotostock.display.activity;

import com.zeromb.gotostock.display.activity.tab.ExchangeTabActivity;
import com.zeromb.gotostock.display.view.group.StockView;
import com.zeromb.gotostock.display.view.line.BalanceLineView;
import com.zeromb.gotostock.display.view.group.PortfolioView;
import com.zeromb.gotostock.display.view.line.stock.StockLineView;
import ru.congas.core.application.Bundle;
import ru.congas.core.input.keys.KeyPressed;
import ru.congas.core.output.widgets.properties.Gravity;

/**
 * @author Mr_Told
 */
public class PortfolioActivity extends ExchangeTabActivity {

    protected static boolean dayDelta = true;

    BalanceLineView totalView = new BalanceLineView(exchange.getPortfolio());
    PortfolioView portfolioView = new PortfolioView(exchange.getPortfolio());

    @Override
    protected void onCreate(Bundle args) {
        super.onCreate(args);

        hintView.setText("Press TAB to switch delta mode, use ARROWS to navigate, ENTER to open stock");

        addWidget(totalView).pos().setGravity(Gravity.leftTop)
                .setOffset(2, 3);
        addWidget(portfolioView).pos().setOffset(4, 5);

        setDayDelta(dayDelta);
        render();

        network.updatePortfolio();
    }

    @Override
    public boolean handle(KeyPressed event) {
        switch (event.getDefinedKey()) {
            case TAB:
                setDayDelta(!dayDelta);
                render();
                return true;
        }
        return super.handle(event);
    }

    @Override
    protected StockView<? extends StockLineView> getStockView() {
        return portfolioView;
    }

    public void setDayDelta(boolean dayDelta) {
        PortfolioActivity.dayDelta = dayDelta;
        totalView.setDayDelta(dayDelta);
        portfolioView.setDayDelta(dayDelta);
        updateActivity();
    }

    @Override
    public void updateActivity() {
        runOnUiThread(() -> {
            totalView.update();
            totalView.markDirty();
        });
        super.updateActivity();
    }
}
