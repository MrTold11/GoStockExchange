package com.zeromb.gotostock.display.view.group;

import com.zeromb.gotostock.display.view.line.stock.StockLineView;
import com.zeromb.gotostock.obj.Stock;
import ru.congas.core.output.canvas.Canvas;
import ru.congas.core.output.widgets.Widget;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

/**
 * @author Mr_Told
 */
public class StockView<T extends StockLineView> extends Widget {

    Set<T> stockViews = new TreeSet<>();

    public StockView(Collection<Stock> stocks, Function<Stock, T> creator) {
        stocks.forEach(
                s -> stockViews.add(creator.apply(s)));
    }

    @Override
    public void render(Canvas canvas) {
        renderStocks(canvas, 0);
    }

    public void renderStocks(Canvas canvas, int offsetY) {
        for (T view : stockViews) {
            view.pos().setOffset(pos().getOffsetX(), pos().getOffsetY() + offsetY);
            view.render(canvas);
            offsetY++;
        }
    }

    public void update() {
        int maxPriceLen = 0;
        int maxDeltaLen = 0;
        for (T view : stockViews) {
            view.update();
            view.markDirty();

            maxPriceLen = Math.max(maxPriceLen, view.getPriceSize());
            maxDeltaLen = Math.max(maxDeltaLen, view.getDeltaSize());
        }

        for (T view : stockViews) {
            int deltaPrice = maxPriceLen - view.getPriceSize();
            int deltaDelta = maxDeltaLen - view.getDeltaSize();
            view.addPriceSpace(deltaPrice);
            view.addDeltaSpace(deltaDelta);
        }
    }

    public Set<T> getStockViews() {
        return stockViews;
    }

}
