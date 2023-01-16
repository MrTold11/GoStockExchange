package com.zeromb.gotostock.trade;

import com.zeromb.gotostock.Gateway;
import com.zeromb.gotostock.network.DispatcherSender;
import com.zeromb.gotostock.trade.obj.DenyReason;
import com.zeromb.gotostock.trade.obj.Order;
import com.zeromb.gotostock.trade.obj.Stock;
import com.zeromb.gotostock.util.TransactionTable;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Mr_Told
 */
public class Dispatcher {

    DispatcherSender sender;

    TradeEngine tradeEngine;

    ExecutorService executors = Executors.newFixedThreadPool(10);

    private final Map<String, Stock> stockMap = new HashMap<>();
    private final Map<String, Gateway.StockPrice> openingPrices = new HashMap<>();

    protected final AtomicInteger QPS = new AtomicInteger();

    public Dispatcher(String gatewayIp, int gatewayPort) {
        tradeEngine = new TradeEngine(this);
        sender = new DispatcherSender(gatewayIp, gatewayPort);

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

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int a = QPS.getAndSet(0);
                System.out.printf("Queries per second: %d\n", a);
            }
        }, 1000L, 1000L);
        //launch
        tradeEngine.start();
    }

    public void placeOrder(Gateway.Order order) {
        QPS.getAndIncrement();

        Order order1 = new Order(order,
                stockMap.get(order.getAsset().getIsin()));

        if (order1.getStock() == null) {
            denyOrder(order1, DenyReason.STOCK_NOT_FOUND);
            return;
        }

        if (order1.getOrderAmount() <= 0) {
            denyOrder(order1, DenyReason.INCORRECT_AMOUNT);
            return;
        }

        if (order1.isType() && order1.getPrice() <= 0) {
            denyOrder(order1, DenyReason.INCORRECT_PRICE);
            return;
        }

        tradeEngine.inputOrders.add(order1);
    }

    public void cancelOrder(Gateway.CancelOrderRequest order) {
        QPS.getAndIncrement();
    }

    private void denyOrder(Order order, DenyReason reason) {
        executors.submit(() -> {
            Gateway.OrderStatus status =
                    Gateway.OrderStatus.newBuilder()
                            .setStatus(Gateway.OStatus.DENIED)
                            .setToken(order.getUid())
                            .setOrderId(order.getTimestamp())
                            .setIsBuy(order.isBuy())
                            .setAsset(Gateway.Asset.newBuilder()
                                    .setIsin(order.getISIN())
                                    .setAmount(order.getLeftAmount())
                                    .setPrice(order.getPrice())
                                    .build())
                            .build();
            sender.sendResult(status);
        });
    }

    public void sendOrderStatus(Order order) {
        TransactionTable table = order.getTable();
        table.commit();

        executors.submit(() -> {
            Set<Gateway.Transaction> transactionSet = new HashSet<>();

            for (int i = table.getA_pos(); i < table.getB_pos(); i++) {
                transactionSet.add(Gateway.Transaction.newBuilder()
                        .setPrice(table.getKeys()[i])
                        .setAmount(table.getValues()[i])
                        .build());
            }

            Gateway.OrderStatus status =
                    Gateway.OrderStatus.newBuilder()
                            .setStatus(Gateway.OStatus.OK)
                            .setToken(order.getUid())
                            .setOrderId(order.getTimestamp())
                            .setIsBuy(order.isBuy())
                            .setAsset(Gateway.Asset.newBuilder()
                                    .setIsin(order.getISIN())
                                    .setAmount(order.getLeftAmount())
                                    .setPrice(order.getPrice())
                                    .build())
                            .addAllTransactions(transactionSet)
                            .build();
            sender.sendResult(status);
        });
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
                Gateway.StockPrice.newBuilder().setIsin(ISIN).setPrice(price).build());
    }

    public Gateway.StockPrice getOpeningPrice(String ISIN) {
        QPS.getAndIncrement();

        return openingPrices.getOrDefault(ISIN,
                Gateway.StockPrice.newBuilder().setIsin(ISIN).setPrice(0).build());
    }

    public Gateway.StockPrice getCurrentPrice(String ISIN) {
        QPS.getAndIncrement();

        Stock stock = stockMap.get(ISIN);
        double price = stock == null ? 0 : stock.getPrice();

        return Gateway.StockPrice.newBuilder().setIsin(ISIN).setPrice(price).build();
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
