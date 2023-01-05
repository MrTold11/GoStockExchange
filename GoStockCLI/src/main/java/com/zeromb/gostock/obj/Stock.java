package com.zeromb.gostock.obj;

/**
 * @author Mr_Told
 */
public class Stock implements Comparable<Stock> {

    final String name;
    double currentMarketPrice, openMarketPrice;

    int amount;
    double balancePrice;

    public Stock(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getCurrentMarketPrice() {
        return currentMarketPrice;
    }

    public void setCurrentMarketPrice(double currentMarketPrice) {
        this.currentMarketPrice = currentMarketPrice;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getBalancePrice() {
        return balancePrice;
    }

    public void setBalancePrice(double balancePrice) {
        this.balancePrice = balancePrice;
    }

    public double getOpenMarketPrice() {
        return openMarketPrice;
    }

    public void setOpenMarketPrice(double openMarketPrice) {
        this.openMarketPrice = openMarketPrice;
    }

    public double getDeltaTotal() {
        return (currentMarketPrice - balancePrice) * amount;
    }

    public double getDeltaTotalPercents() {
        if (balancePrice == 0) return 0;

        return (currentMarketPrice - balancePrice) / balancePrice * 100;
    }

    public double getDeltaToday() {
        return (currentMarketPrice - openMarketPrice) * amount;
    }

    public double getDeltaTodayPercents() {
        if (openMarketPrice == 0) return 0;

        return (currentMarketPrice - openMarketPrice) / openMarketPrice * 100;
    }

    @Override
    public int compareTo(Stock o) {
        return getName().compareToIgnoreCase(o.getName());
    }
}
