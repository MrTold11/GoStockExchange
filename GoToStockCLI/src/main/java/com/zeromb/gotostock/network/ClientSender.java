package com.zeromb.gotostock.network;

import com.zeromb.gotostock.Gateway;
import com.zeromb.gotostock.FEServiceGrpc;
import com.zeromb.gotostock.obj.Stock;
import com.zeromb.gotostock.obj.StockExchange;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * @author Mr_Told
 */
public class ClientSender {

    private final FEServiceGrpc.FEServiceStub asyncStub;
    private final Gateway.Empty EMPTY = Gateway.Empty.newBuilder().build();

    private final NetUpdateHandler updateHandler;
    private final StockExchange exchange;

    private final StreamObserver<Gateway.Empty> emptyObserver = new DefaultObserver<>();

    private final StreamObserver<Gateway.StocksList> stocksListObserver = new DefaultObserver<>() {

        @Override
        public void onNext(Gateway.StocksList value) {
            exchange.clearStocks();
            for (Gateway.Stock gs : value.getStocksList())
                exchange.addStock(
                        new Stock(
                                gs.getFullName(),
                                gs.getTicker(),
                                gs.getIsin(),
                                gs.getAbout()));
            updateHandler.updateCurrentActivity();
        }

    };

    private final StreamObserver<Gateway.StockPrice> openPriceObserver = new DefaultObserver<>() {

        @Override
        public void onNext(Gateway.StockPrice value) {
            exchange.getStockByISIN(value.getIsin())
                    .setOpenMarketPrice(value.getPrice());
            updateHandler.updateCurrentActivity();
        }

    };

    private final StreamObserver<Gateway.StockPrice> currentPriceObserver = new DefaultObserver<>() {

        @Override
        public void onNext(Gateway.StockPrice value) {
            exchange.getStockByISIN(value.getIsin())
                    .setCurrentMarketPrice(value.getPrice());
            updateHandler.updateCurrentActivity();
        }

    };

    private final StreamObserver<Gateway.Portfolio> portfolioObserver = new DefaultObserver<>() {

        @Override
        public void onNext(Gateway.Portfolio value) {
            for (Gateway.Asset asset : value.getAssetsList()) {
                Stock stock = exchange.getStockByISIN(asset.getIsin());
                if (stock == null) continue;

                stock.setAmount(asset.getAmount())
                        .setBalancePrice(asset.getPrice());
            }
            exchange.getPortfolio().updateStocks(exchange.getStocks());
            updateHandler.updateCurrentActivity();
        }

    };

    public ClientSender(String host, int port, NetUpdateHandler handler, StockExchange exchange) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext(), handler, exchange);
    }

    public ClientSender(ManagedChannelBuilder<?> channelBuilder, NetUpdateHandler handler, StockExchange exchange) {
        ManagedChannel channel = channelBuilder.build();
        asyncStub = FEServiceGrpc.newStub(channel);
        this.updateHandler = handler;
        this.exchange = exchange;
    }

    public void updateStocks() {
        asyncStub.getStocksList(EMPTY, stocksListObserver);
    }

    public void getOpeningPrice(Stock stock) {
        asyncStub.getOpeningPrice(
                Gateway.ISIN.newBuilder().setIsin(stock.getISIN()).build(),
                openPriceObserver);
    }

    public void updatePrice(Stock stock) {
        asyncStub.getCurrentPrice(
                Gateway.ISIN.newBuilder().setIsin(stock.getISIN()).build(),
                currentPriceObserver);
    }

    public void updatePortfolio(String token) {
        if (token == null) return;
        asyncStub.getPortfolio(
                Gateway.Token.newBuilder().setToken(token).build(),
                portfolioObserver
        );
    }

    public void placeOrder(String token, boolean isBuy, boolean isLimit,
                           Stock stock, int amount, double price) {
        if (token == null) return;
        asyncStub.placeOrder(
                Gateway.Order.newBuilder()
                        .setToken(token)
                        .setIsBuy(isBuy)
                        .setType(isLimit ?
                                Gateway.OrderType.LIMIT :
                                Gateway.OrderType.MARKET)
                        .setAsset(Gateway.Asset.newBuilder()
                                .setIsin(stock.getISIN())
                                .setAmount(amount)
                                .setPrice(price)
                                .build())
                        .build(),
                emptyObserver);
    }

    public void cancelOrder(String token, long id) {
        if (token == null) return;
        asyncStub.cancelOrder(
                Gateway.CancelOrderRequest.newBuilder()
                        .setToken(token)
                        .setOrderId(id)
                        .build(),
                emptyObserver);
    }

}
