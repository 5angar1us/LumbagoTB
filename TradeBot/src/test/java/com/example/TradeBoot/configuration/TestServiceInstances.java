package com.example.TradeBoot.configuration;

import com.example.TradeBoot.api.http.*;
import com.example.TradeBoot.api.http.delay.GlobalDelay;
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

    public static IOrdersService.Base getOrdersService() {
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

    private static GlobalDelay globalDelay;

    private static IPositionsService iPositionsService;
    private static IOrdersService.Base ordersService;
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
        HttpResponseHandler httpResponseHandler = new HttpResponseHandler(new HttpFTXResponseParser(), new HttpResponseErrorHandler());

        httpClient = new HttpClientWorker(httpRequestFactory, new HttpSendErrorHandler(), httpResponseHandler);
        globalDelay = new GlobalDelay(httpClient, 1);

        iPositionsService = new IPositionsService.Base(globalDelay);
        ordersService = new IOrdersService.Base(globalDelay);
        iMarketService = new IMarketService.Base(globalDelay);
        walletService = new IWalletService.Base(globalDelay);
        accountService = new AccountService(globalDelay);
        iFutureService = new IFutureService.Base(globalDelay);




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
