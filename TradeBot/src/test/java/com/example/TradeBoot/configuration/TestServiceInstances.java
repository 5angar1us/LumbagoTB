package com.example.TradeBoot.configuration;

import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.api.http.HttpClientWorkerWithDelay;
import com.example.TradeBoot.api.http.HttpRequestFactory;
import com.example.TradeBoot.api.http.HttpResponseHandler;
import com.example.TradeBoot.api.services.*;
import com.example.TradeBoot.trade.services.OrderPriceService;
import com.example.TradeBoot.trade.services.*;

public class TestServiceInstances {

    public static com.example.TradeBoot.api.services.IFutureService getFutureService() {
        return iFutureService;
    }

    public static AccountService getAccountService() {
        return accountService;
    }

    private static AccountService accountService;

    public static HttpClientWorker getHttpClient() {
        return httpClient;
    }

    public static IPositionsService getPositionsService() {
        return iPositionsService;
    }

    public static OrdersService getOrdersService() {
        return ordersService;
    }

    public static IMarketService getMarketService() {
        return iMarketService;
    }

    public static IWalletService getWalletService() {
        return walletService;
    }

    public static FinancialInstrumentService getFinancialInstrumentService() {
        return financialInstrumentService;
    }

    public static OrderPriceService getOrderPriceCalculator() {
        return orderPriceService;
    }

    public static ClosePositionInformationService getClosePositionInformationService() {
        return closePositionInformationService;
    }

    public static FinancialInstrumentPositionsService getFinancialInstrumentPositionsService() {
        return financialInstrumentPositionsService;
    }

    //Api
    private static HttpClientWorker httpClient;

    private static HttpClientWorkerWithDelay httpClientWorkerWithDelay;

    private static IPositionsService iPositionsService;
    private static OrdersService ordersService;
    private static IMarketService iMarketService;

    private static IFutureService iFutureService;

    private static IWalletService walletService;


    //Trade
    private static FinancialInstrumentService financialInstrumentService;

    private static OrderPriceService orderPriceService;

    private static ClosePositionInformationService closePositionInformationService;

    private static ClosePositionInformationService mockClosePositionInformationService;

    private static FinancialInstrumentPositionsService financialInstrumentPositionsService;



    static {
        //Api
        HttpRequestFactory httpRequestFactory = new HttpRequestFactory(TestConfig.getAuntification());
        HttpResponseHandler httpResponseHandler = new HttpResponseHandler();

        httpClient = new HttpClientWorker(httpRequestFactory, httpResponseHandler);
        httpClientWorkerWithDelay = new HttpClientWorkerWithDelay(httpClient);

        iPositionsService = new IPositionsService.Base(httpClientWorkerWithDelay);
        ordersService = new OrdersService(httpClientWorkerWithDelay);
        iMarketService = new IMarketService.Base(httpClientWorkerWithDelay);
        walletService = new IWalletService.Base(httpClientWorkerWithDelay);
        accountService = new AccountService(httpClientWorkerWithDelay);
        iFutureService = new IFutureService.Base(httpClientWorkerWithDelay);




        //Trade
        var coinHandler = new VolumeVisitor.CoinVolumeVisitor(walletService);
        var futureHandler = new VolumeVisitor.FutureVolumeVisitor(iPositionsService);

        financialInstrumentService = new FinancialInstrumentService(iMarketService, iFutureService);
        orderPriceService = new OrderPriceService();
        closePositionInformationService = new ClosePositionInformationService(
                walletService,
                financialInstrumentService ,
                iPositionsService,
                coinHandler,
                futureHandler);
    }


}
