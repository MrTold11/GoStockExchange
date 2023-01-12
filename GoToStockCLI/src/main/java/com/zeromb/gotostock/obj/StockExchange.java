package com.zeromb.gotostock.obj;

import java.util.*;

/**
 * @author Mr_Told
 */
public class StockExchange {

    Map<String, Stock> stockMap = Collections.synchronizedMap(new TreeMap<>());

    Portfolio portfolio = new Portfolio();

    public StockExchange() {
        addStock(new Stock("Аэрофлот", "AFLT", "RU0009062285", "")
                .setCurrentMarketPrice(542.71)
                .setOpenMarketPrice(528.58)
                .setBalancePrice(457.33)
                .setAmount(80));
        addStock(new Stock("Tesla", "TSLA", "US88160R1014", "")
                .setCurrentMarketPrice(420.69)
                .setOpenMarketPrice(451.87)
                .setBalancePrice(846.87)
                .setAmount(5));
        addStock(new Stock("Virgin Galactic Holdings", "SPCE", "US92766K1060", "")
                .setCurrentMarketPrice(3.4)
                .setOpenMarketPrice(3.48)
                .setBalancePrice(29.87)
                .setAmount(19));
        addStock(new Stock("Яндекс", "YNDX", "NL0009805522", "")
                .setCurrentMarketPrice(1846.8)
                .setOpenMarketPrice(1874.2));

        portfolio.updateStocks(getStocks());
    }

    public void clearStocks() {
        stockMap.clear();
    }

    public void addStock(Stock stock) {
        stockMap.put(stock.getISIN(), stock);
    }

    public Set<Stock> findStocks(String query) {
        Set<Stock> result = new TreeSet<>();
        for (Stock s : stockMap.values()) {
            if (s.getName().contains(query))
                result.add(s);
        }
        return result;
    }

    public Stock getStockByISIN(String ISIN) {
        return stockMap.get(ISIN);
    }

    public Collection<Stock> getStocks() {
        return stockMap.values();
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }
}
