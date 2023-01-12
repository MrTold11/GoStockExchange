package com.zeromb.gotostock.network;

import com.zeromb.gotostock.obj.Stock;
import com.zeromb.gotostock.obj.StockExchange;

import java.util.*;

/**
 * @author Mr_Told
 */
public class NetworkProvider {

    final StockExchange exchange;

    final ClientReceiver clientReceiver;
    final ClientSender clientSender;

    final Timer priceUpdateTimer = new Timer();
    final TimerTask priceUpdateTask = new TimerTask() {
        @Override
        public void run() {
            updateStocksPrices();
        }
    };

    final Set<Stock> subscribedStock = new HashSet<>();

    String token;

    public NetworkProvider(String gatewayIp, NetUpdateHandler handler, StockExchange exchange) {
        this.clientReceiver = new ClientReceiver(50051, handler);
        this.clientSender = new ClientSender(gatewayIp, 50050, handler, exchange);
        this.exchange = exchange;

        priceUpdateTimer.scheduleAtFixedRate(priceUpdateTask, 1L, 500L);
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void subscribeStock(Stock stock) {
        subscribedStock.add(stock);
    }

    public void unsubscribeStock(Stock stock) {
        subscribedStock.remove(stock);
    }

    public void resubscribeStocks(Collection<Stock> stocks) {
        subscribedStock.clear();
        subscribedStock.addAll(stocks);
        updateStocksPrices();
    }

    public void resubscribeStocks(Stock... stocks) {
        resubscribeStocks(Set.of(stocks));
    }

    public void updateStocksPrices() {
        synchronized (subscribedStock) {
            for (Stock stock : subscribedStock)
                clientSender.updatePrice(stock);
        }
    }

    public void refreshOpeningPrices() {
        for (Stock stock : exchange.getStocks())
            clientSender.getOpeningPrice(stock);
    }

    public void updateStocks() {
        clientSender.updateStocks();
    }

    public void updatePortfolio() {
        clientSender.updatePortfolio(token);
    }

    public void startStatusReceiver() {
        clientReceiver.start();
    }

    public void placeOrder(boolean isBuy, boolean isLimit,
                           Stock stock, int amount, double price) {
        clientSender.placeOrder(token, isBuy, isLimit, stock, amount, price);
    }

    public void cancelOrder(long id) {
        clientSender.cancelOrder(token, id);
    }

    public void close() throws InterruptedException {
        priceUpdateTimer.cancel();
        clientReceiver.stop();
    }

}
