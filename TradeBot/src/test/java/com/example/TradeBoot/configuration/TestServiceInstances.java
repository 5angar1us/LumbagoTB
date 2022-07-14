package com.example.TradeBoot.configuration;

import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.api.http.HttpRequestFactory;
import com.example.TradeBoot.api.http.HttpResponseHandler;
import com.example.TradeBoot.api.services.implemetations.IPositionsService;
import com.example.TradeBoot.api.services.implemetations.*;
import com.example.TradeBoot.trade.calculator.OrderPriceCalculator;
import com.example.TradeBoot.trade.services.ClosePositionInformationService;
import com.example.TradeBoot.trade.services.FinancialInstrumentService;

public class TestServiceInstances {

    public static IFutureService getFutureService() {
        return IFutureService;
    }

    public static AccountService getAccountService() {
        return accountService;
    }

    private static AccountService accountService;

    public static HttpClientWorker getHttpClient() {
        return httpClient;
    }

    public static IPositionsService getPositionsService() {
        return IPositionsService;
    }

    public static OrdersService getOrdersService() {
        return ordersService;
    }

    public static IMarketService getMarketService() {
        return IMarketService;
    }

    public static IWalletService getWalletService() {
        return walletService;
    }

    public static FinancialInstrumentService getFinancialInstrumentService() {
        return financialInstrumentService;
    }

    public static OrderPriceCalculator getOrderPriceCalculator() {
        return orderPriceCalculator;
    }

    public static ClosePositionInformationService getClosePositionInformationService() {
        return closePositionInformationService;
    }
    //Api
    private static HttpClientWorker httpClient;

    private static IPositionsService IPositionsService;
    private static OrdersService ordersService;
    private static IMarketService IMarketService;

    private static IFutureService IFutureService;

    private static IWalletService walletService;


    //Trade
    private static FinancialInstrumentService financialInstrumentService;

    private static OrderPriceCalculator orderPriceCalculator;

    private static ClosePositionInformationService closePositionInformationService;

    private static ClosePositionInformationService mockClosePositionInformationService;


    static {
        //Api
        HttpRequestFactory httpRequestFactory = new HttpRequestFactory(TestConfig.getAuntification());
        HttpResponseHandler httpResponseHandler = new HttpResponseHandler();

        httpClient = new HttpClientWorker(httpRequestFactory, httpResponseHandler);

        IPositionsService = new IPositionsService.Base(httpClient);
        ordersService = new OrdersService(httpClient);
        IMarketService = new IMarketService.Base(httpClient);
        walletService = new IWalletService.Base(httpClient);
        accountService = new AccountService(httpClient);
        IFutureService = new IFutureService.Base(httpClient);


        //Trade
        financialInstrumentService = new FinancialInstrumentService(IMarketService, IFutureService);
        orderPriceCalculator = new OrderPriceCalculator();
        closePositionInformationService = new ClosePositionInformationService(walletService, financialInstrumentService , IPositionsService);



    }


}
