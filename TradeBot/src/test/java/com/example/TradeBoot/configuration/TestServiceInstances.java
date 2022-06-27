package com.example.TradeBoot.configuration;

import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.api.http.HttpRequestFactory;
import com.example.TradeBoot.api.http.HttpResponseHandler;
import com.example.TradeBoot.api.services.*;

public class TestServiceInstances {

    private static HttpClientWorker httpClient;

    private static PositionsService positionsService;
    private static OrdersService ordersService;
    private static MarketService marketService;

    private static FutureService futureService;

    private static FinancialInstrumentService financialInstrumentService;

    public static FutureService getFutureService() {
        return futureService;
    }

    public static AccountService getAccountService() {
        return accountService;
    }

    private static AccountService accountService;

    public static HttpClientWorker getHttpClient() {
        return httpClient;
    }

    public static PositionsService getPositionsService() {
        return positionsService;
    }

    public static OrdersService getOrdersService() {
        return ordersService;
    }

    public static MarketService getMarketService() {
        return marketService;
    }

    public static WalletService getWalletService() {
        return walletService;
    }

    public static FinancialInstrumentService getFinancialInstrumentService() { return financialInstrumentService;}
    private static WalletService walletService;

    static {

        HttpRequestFactory httpRequestFactory = new HttpRequestFactory(TestConfig.getAuntification());
        HttpResponseHandler httpResponseHandler = new HttpResponseHandler();

        httpClient = new HttpClientWorker(httpRequestFactory, httpResponseHandler);

        httpClient = new HttpClientWorker(httpRequestFactory, httpResponseHandler);

        positionsService = new PositionsService(httpClient);
        ordersService = new OrdersService(httpClient);
        marketService = new MarketService(httpClient);
        walletService = new WalletService(httpClient);
        accountService = new AccountService(httpClient);
        futureService = new FutureService(httpClient);

        financialInstrumentService = new FinancialInstrumentService(marketService, futureService);
    }



}
