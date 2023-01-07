package com.zeromb.gotostock.display.view.group;

import com.zeromb.gotostock.display.view.line.CoinsLineView;
import com.zeromb.gotostock.display.view.line.stock.OwnedStockLineView;
import com.zeromb.gotostock.obj.Portfolio;
import ru.congas.core.output.canvas.Canvas;

/**
 * @author Mr_Told
 */
public class PortfolioView extends StockView<OwnedStockLineView> {

    CoinsLineView coinsView;

    public PortfolioView(Portfolio portfolio) {
        super(portfolio.getStocks(), OwnedStockLineView::new);

        this.coinsView = new CoinsLineView(portfolio.getCoins());
        update();
    }

    @Override
    public void renderStocks(Canvas canvas, int offsetY) {
        coinsView.pos().setOffset(pos().getOffsetX(), pos().getOffsetY() + offsetY);
        coinsView.render(canvas);
        super.renderStocks(canvas, offsetY + 2);
    }

    public void setDayDelta(boolean dayDelta) {
        for (OwnedStockLineView view : stockViews)
            view.setDayDelta(dayDelta);
    }

    public void update() {
        coinsView.update();
        coinsView.markDirty();

        super.update();
    }

}
