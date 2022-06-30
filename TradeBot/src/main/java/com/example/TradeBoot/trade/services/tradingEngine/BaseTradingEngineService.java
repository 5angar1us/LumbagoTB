package com.example.TradeBoot.trade.services.tradingEngine;

import com.example.TradeBoot.ExtendedExecutor;
import com.example.TradeBoot.api.services.MarketService;
import com.example.TradeBoot.api.services.OrdersService;
import com.example.TradeBoot.api.services.WalletService;
import com.example.TradeBoot.trade.calculator.OrderPriceCalculator;
import com.example.TradeBoot.trade.services.ClosePositionInformationService;
import com.example.TradeBoot.ui.ITradeSettingsService;
import com.example.TradeBoot.ui.models.TradeSettings;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BaseTradingEngineService extends AbstractTradingEngineService {

    public BaseTradingEngineService(
            OrdersService ordersService,
            MarketService marketService,
            WalletService walletService,
            OrderPriceCalculator orderPriceCalculator,
            ClosePositionInformationService closePositionInformationService,
            ITradeSettingsService tradeSettingsService)
    {
        super(
                ordersService,
                marketService,
                walletService,
                orderPriceCalculator,
                closePositionInformationService,
                tradeSettingsService);
    }





    void launch(List<TradeSettings> marketTradeSettings) {
        this.engines.clear();

        tradeStatus.setNeedStop(false);

        this.trapLimitPositionPairs = marketTradeSettings.stream()
                .map(this::createTrapLimitPositionPairs)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        this.executorService = new ExtendedExecutor(trapLimitPositionPairs.size());

        this.engines = trapLimitPositionPairs.stream()
                .map(tradingOrderInfoPair -> {
                    return new ITradingRunnableEngine.Base(
                            tradingOrderInfoPair.tradingService(),
                            closePositionInformationService,
                            tradingOrderInfoPair.tradeInformation(),
                            tradingOrderInfoPair.market(),
                            tradeStatus);
                })
                .collect(Collectors.toList());

        Objects.requireNonNull(this.executorService);

        engines.forEach(executorService::submit);
    }




}
