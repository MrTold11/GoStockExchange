package com.zeromb.gotostock.trade;

import com.zeromb.gotostock.Gateway;
import com.zeromb.gotostock.trade.obj.DenyReason;
import com.zeromb.gotostock.trade.obj.Order;
import com.zeromb.gotostock.trade.obj.Stock;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Mr_Told
 */
public class Dispatcher {

    TradeEngine tradeEngine;

    Queue<Order> resultOrders = new ConcurrentLinkedQueue<>();

    private final Map<String, Stock> stockMap = new HashMap<>();

    private final Map<String, Gateway.StockPrice> openingPrices = new HashMap<>();
    private final Gateway.StockPrice ZERO = Gateway.StockPrice.newBuilder().setPrice(0.0D).build();

    public Dispatcher() {
        tradeEngine = new TradeEngine(this);

        //prepare stocks
        addStock(new Stock("RU0000000001", 99.9D, 145.4D));
        addStock(new Stock("RU0000000002", 478.4D, 481.76D));
        addStock(new Stock("RU0000000003", 84.7D, 57D));
        addStock(new Stock("US0378331005", 125.02D, 129.62D));
        addStock(new Stock("US88160R1014", 110.34, 113.06D));
        addStock(new Stock("US0231351067", 83.12D, 86.08D));
        addStock(new Stock("US5949181045", 222.31D, 224.93D));
        addStock(new Stock("US67066G1040", 148.59D, 142.65D));

        //prepare users

    }

    public void placeOrder(Gateway.Order order) {
        if (order.getAsset().getAmount() <= 0) {
            denyOrder(order, DenyReason.INCORRECT_AMOUNT);
            return;
        }

        if (order.getType() == Gateway.OrderType.LIMIT
                && order.getAsset().getPrice() <= 0) {
            denyOrder(order, DenyReason.INCORRECT_PRICE);
            return;
        }

        if (order.getType() != Gateway.OrderType.MARKET
            && order.getType() != Gateway.OrderType.LIMIT) {
            denyOrder(order, DenyReason.INCORRECT_TYPE);
            return;
        }

        Stock stock = stockMap.get(order.getAsset().getISIN());
        if (stock == null) {
            denyOrder(order, DenyReason.STOCK_NOT_FOUND);
            return;
        }

        Order order1 = new Order(order, stock);

        tradeEngine.inputOrders.add(order1);
    }

    public void cancelOrder(Gateway.CancelOrderRequest order) {

    }

    private void denyOrder(Gateway.Order order, DenyReason reason) {

    }

    public void addStock(Stock stock) {
        stockMap.put(stock.getISIN(), stock);
        setOpeningPrice(stock.getISIN(), stock.getOpeningPrice());
    }

    public void setOpeningPrice(String ISIN, double price) {
        Stock stock = stockMap.get(ISIN);
        if (stock == null) return;

        stock.setOpeningPrice(price);

        openingPrices.put(ISIN,
                Gateway.StockPrice.newBuilder().setPrice(price).build());
    }

    public Gateway.StockPrice getOpeningPrice(String ISIN) {
        return openingPrices.getOrDefault(ISIN, ZERO);
    }

    public Gateway.StockPrice getCurrentPrice(String ISIN) {
        Stock stock = stockMap.get(ISIN);
        if (stock == null) return ZERO;

        double price = stock.getPrice();
        return Gateway.StockPrice.newBuilder().setPrice(price).build();
    }

    private static final AtomicLong prevTimestamp = new AtomicLong();

    public static long getUniqueTimestamp() {
        long value = System.nanoTime();
        synchronized (prevTimestamp) {
            long prev = prevTimestamp.get();
            if (value < prev)
                value = prev + 1;
            prevTimestamp.set(value);
        }
        return value;
    }

}