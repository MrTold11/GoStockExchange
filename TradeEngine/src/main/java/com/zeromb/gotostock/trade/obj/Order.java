package com.zeromb.gotostock.trade.obj;

import com.zeromb.gotostock.Gateway;
import com.zeromb.gotostock.trade.Dispatcher;
import com.zeromb.gotostock.util.TransactionTable;

import javax.annotation.Nonnull;

/**
 * @author Mr_Told
 */
public class Order implements Comparable<Order> {

    final String uid;
    final String ISIN;
    final Stock stock;
    final int orderAmount;
    final double price;
    final boolean type; // limit = true
    final boolean isBuy;
    final long timestamp;

    final TransactionTable table;

    int leftAmount;

    public Order(Gateway.Order order, Stock stock) {
        this.uid = order.getToken();
        this.ISIN = order.getAsset().getISIN();
        this.stock = stock;
        this.orderAmount = order.getAsset().getAmount();
        this.leftAmount = orderAmount;
        this.type = order.getType() == Gateway.OrderType.LIMIT;
        this.price = type ? order.getAsset().getPrice() :
                stock != null ? stock.getPrice() : 0D;
        this.isBuy = order.getIsBuy();
        this.timestamp = Dispatcher.getUniqueTimestamp();

        table = new TransactionTable(orderAmount);
    }

    public String getUid() {
        return uid;
    }

    public String getISIN() {
        return ISIN;
    }

    public Stock getStock() {
        return stock;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public double getPrice() {
        return price;
    }

    public boolean isType() {
        return type;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public TransactionTable getTable() {
        return table;
    }

    public int getLeftAmount() {
        return leftAmount;
    }

    public void setLeftAmount(int leftAmount) {
        this.leftAmount = leftAmount;
    }

    @Override
    public int compareTo(@Nonnull Order o) {
        return (int) (price == o.price ?
                timestamp - o.timestamp :
                isBuy ?
                        (price > o.price ? 1 : -1) :
                        (price > o.price ? -1 : 1));
    }
}
