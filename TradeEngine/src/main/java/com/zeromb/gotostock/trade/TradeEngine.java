package com.zeromb.gotostock.trade;

import com.zeromb.gotostock.trade.obj.Order;
import com.zeromb.gotostock.trade.obj.Stock;

import java.util.Queue;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Mr_Told
 */
public class TradeEngine extends Thread {

    final Dispatcher dispatcher;
    Queue<Order> inputOrders = new ConcurrentLinkedQueue<>();

    volatile boolean run = true;

    public TradeEngine(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        try {
            Order order, edge;
            Stock stock;
            TreeSet<Order> matchSet;
            int limit;

            //this thread could use conveyor architecture: (risk -> [match) -> finalize]
            while (run) {
                order = inputOrders.poll();
                if (order == null) continue;

                //check risk here

                //match
                stock = order.getStock();
                matchSet = order.isBuy() ? stock.sellSet : stock.buySet;
                edge = matchSet.first();

                while (edge != null && edge.getPrice() <= order.getPrice() && order.getLeftAmount() > 0) {
                    limit = Math.min(
                            order.getLeftAmount(),
                            edge.getLeftAmount());
                    order.getTable().add(edge.getPrice(), limit);
                    edge.getTable().add(edge.getPrice(), limit);
                    order.setLeftAmount(order.getLeftAmount() - limit);
                    edge.setLeftAmount(edge.getLeftAmount() - limit);
                    if (edge.getLeftAmount() == 0) {
                        sendOrder(edge);
                        matchSet.remove(edge);
                        if (order.getLeftAmount() != 0)
                            edge = matchSet.first();
                    }
                }

                //finalize
                if (order.getLeftAmount() > 0) {
                    if (order.isBuy())
                        stock.buySet.add(order);
                    else
                        stock.sellSet.add(order);
                }

                sendOrder(order);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void sendOrder(Order order) {
        //change users actives
        dispatcher.resultOrders.add(order);
    }

}
