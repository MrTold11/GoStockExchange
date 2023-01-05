package com.zeromb.gostock.obj;

import java.util.*;

/**
 * @author Mr_Told
 */
public class Portfolio {

    Set<Stock> stocks = new TreeSet<>();
    GotoCoin coins = new GotoCoin();

    public Set<Stock> getStocks() {
        stocks.removeIf(s -> s.getAmount() < 1);

        return stocks;
    }

    public void updateStocks(Set<Stock> all) {
        for (Stock s : all) {
            if (s.getAmount() > 0)
                stocks.add(s);
        }
    }

    public void setStocks(Set<Stock> stocks) {
        this.stocks = stocks;
    }

    public GotoCoin getCoins() {
        return coins;
    }

    public void setCoins(GotoCoin coins) {
        this.coins = coins;
    }

    public double getDeltaTotal() {
        double delta = 0;
        for (Stock stock : stocks)
            delta += stock.getDeltaTotal();
        return delta;
    }

    public double getDeltaTotalPercents() {
        double deltaTotal = Math.abs(getDeltaTotal());
        if (deltaTotal == 0) return 0;

        return (deltaTotal < 0 ? -1 : 1) *
                deltaTotal * 100 / (deltaTotal + coins.amount);
    }

    public double getDeltaToday() {
        double delta = 0;
        for (Stock stock : stocks)
            delta += stock.getDeltaToday();
        return delta;
    }

    public double getDeltaTodayPercents() {
        double deltaToday = Math.abs(getDeltaToday());
        if (deltaToday == 0) return 0;

        return deltaToday < 0 ? -1 : 1 *
                deltaToday * 100 / (deltaToday + coins.amount);
    }

}
