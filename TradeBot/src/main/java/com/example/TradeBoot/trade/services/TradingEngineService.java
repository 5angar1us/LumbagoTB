package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.services.MarketService;
import com.example.TradeBoot.api.services.OrdersService;
import com.example.TradeBoot.api.services.WalletService;
import com.example.TradeBoot.trade.calculator.OrderPriceCalculator;
import com.example.TradeBoot.trade.model.*;
import com.example.TradeBoot.ui.models.TradeSettings;
import com.example.TradeBoot.ui.models.TradeSettingsDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class TradingEngineService {

    static final Logger log =
            LoggerFactory.getLogger(TradingEngineService.class);
    protected final OrdersService ordersService;
    protected final MarketService marketService;

    protected final WalletService walletService;

    protected final OrderPriceCalculator orderPriceCalculator;

    protected final ClosePositionInformationService closePositionInformationService;

    protected List<MarketTradeSettings> marketTradeSettings;


    protected List<TradingOrderInfoPair> trapLimitPositionPairs;

    protected Boolean isStop = true;


    @Autowired
    public TradingEngineService(OrdersService ordersService, MarketService marketService, WalletService walletService, OrderPriceCalculator orderPriceCalculator, ClosePositionInformationService closePositionInformationService) {
        this.ordersService = ordersService;
        this.marketService = marketService;
        this.walletService = walletService;
        this.orderPriceCalculator = orderPriceCalculator;
        this.closePositionInformationService = closePositionInformationService;
    }

    public boolean isStop() {
        return isStop;
    }

    public abstract int runnableEngineCount();

    public abstract void saveStop();


    public void currectStart(List<TradeSettings> marketTradeSettings){
        saveStop();
        launch(marketTradeSettings);
    }


    abstract void launch(List<TradeSettings> marketTradeSettings);





    protected List<OrderInformation> createOrderInformtaion(TradeSettingsDetail tradeSettingsDetail){
        List<OrderInformation> result = new ArrayList<OrderInformation>();
        var volume = new BigDecimal(tradeSettingsDetail.getVolume());
        var distanceInPersent = new Persent(tradeSettingsDetail.getPriceOffset()) ;
        switch (tradeSettingsDetail.getTradingStrategy()){
            case ALL -> {
                result.add(new OrderInformation(volume, ESide.SELL, distanceInPersent));
                result.add(new OrderInformation(volume, ESide.BUY, distanceInPersent));
            }
            case LONG -> {
                result.add(new OrderInformation(volume, ESide.BUY, distanceInPersent));
            }
            case SHORT -> {
                result.add(new OrderInformation(volume, ESide.SELL, distanceInPersent));
            }
            default -> throw new RuntimeException("incorrect tradingStrategyValue" + tradeSettingsDetail);
        }
        return result;
    }



    protected MarketInformation createMarketInformation(TradeSettings tradeSettings) {
        return new MarketInformation(
                tradeSettings.getMarketName(),
                tradeSettings.getTradeDelay());
    }

    protected TradingService createTrapLimitOrdersService(
            MarketInformation marketInformation, Persent maximumDivination) {
        return new TradingService(
                ordersService,
                marketService,
                orderPriceCalculator,
                marketInformation,
                maximumDivination,
                log,
                isStop
        );
    }


}








