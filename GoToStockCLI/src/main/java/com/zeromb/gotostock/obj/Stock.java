package com.zeromb.gotostock.obj;

/**
 * @author Mr_Told
 */
public class Stock implements Comparable<Stock> {

    final String name, ticker, ISIN, description;
    double currentMarketPrice, openMarketPrice;

    int amount;
    double balancePrice;

    public Stock(String name, String ticker, String isin, String description) {
        this.name = name;
        this.ticker = ticker;
        ISIN = isin;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getTicker() {
        return ticker;
    }

    public String getISIN() {
        return ISIN;
    }

    public double getCurrentMarketPrice() {
        return currentMarketPrice;
    }

    public Stock setCurrentMarketPrice(double currentMarketPrice) {
        this.currentMarketPrice = currentMarketPrice;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public Stock setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public double getBalancePrice() {
        return balancePrice;
    }

    public Stock setBalancePrice(double balancePrice) {
        this.balancePrice = balancePrice;
        return this;
    }

    public double getOpenMarketPrice() {
        return openMarketPrice;
    }

    public Stock setOpenMarketPrice(double openMarketPrice) {
        this.openMarketPrice = openMarketPrice;
        return this;
    }

    public double getDeltaTotal() {
        return (currentMarketPrice - balancePrice) * amount;
    }

    public double getDeltaTotalPercents() {
        if (balancePrice == 0) return 0;

        return (currentMarketPrice - balancePrice) / balancePrice * 100;
    }

    public double getDeltaTodayOwned() {
        return (currentMarketPrice - openMarketPrice) * amount;
    }

    public double getDeltaTodayOne() {
        return currentMarketPrice - openMarketPrice;
    }

    public double getDeltaTodayPercents() {
        if (openMarketPrice == 0) return 0;

        return (currentMarketPrice - openMarketPrice) / openMarketPrice * 100;
    }

    @Override
    public int compareTo(Stock o) {
        return getTicker().compareToIgnoreCase(o.getTicker());
    }
}
