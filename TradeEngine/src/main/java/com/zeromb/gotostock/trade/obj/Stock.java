package com.zeromb.gotostock.trade.obj;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author Mr_Told
 */
public class Stock {

    final String ISIN;

    double openingPrice;

    volatile double price;

    public TreeSet<Order> buySet = new TreeSet<>();
    public TreeSet<Order> sellSet = new TreeSet<>();

    public Stock(String ISIN, double openingPrice, double price) {
        this.ISIN = ISIN;
        this.openingPrice = openingPrice;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getISIN() {
        return ISIN;
    }

    public double getOpeningPrice() {
        return openingPrice;
    }

    public void setOpeningPrice(double openingPrice) {
        this.openingPrice = openingPrice;
    }
}
