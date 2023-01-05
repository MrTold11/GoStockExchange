package com.zeromb.gostock.obj;

import java.util.*;

/**
 * @author Mr_Told
 */
public class StockExchange {


    Map<String, Stock> stockMap = new TreeMap<>();

    Portfolio portfolio = new Portfolio();

    public Set<Stock> findStocks(String query) {
        Set<Stock> result = new TreeSet<>();
        for (Stock s : stockMap.values()) {
            if (s.getName().contains(query))
                result.add(s);
        }
        return result;
    }

}
