package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.api.http.IHttpClientWorker;
import com.example.TradeBoot.api.http.delay.MarketDelayFactory;
import com.example.TradeBoot.api.services.IMarketService;
import com.example.TradeBoot.api.services.IOrdersService;
import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.trade.ExtendedExecutor;
import com.example.TradeBoot.trade.TradingRunnableEngine;
import com.example.TradeBoot.trade.TradingRunnableEngineFactory;
import com.example.TradeBoot.trade.model.*;
import com.example.TradeBoot.trade.tradeloop.*;
import com.example.TradeBoot.ui.service.ITradeSettingsService;
import com.example.TradeBoot.ui.models.TradeSettings;
import com.example.TradeBoot.ui.models.TradeSettingsDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@Service
public class TradingEngineService {

    static final Logger log =
            LoggerFactory.getLogger(TradingEngineService.class);

    public TradingEngineService(ITradeSettingsService tradeSettingsService, TradingRunnableEngineFactory tradingRunnableEngineFactory) {
        this.tradeSettingsService = tradeSettingsService;
        this.tradingRunnableEngineFactory = tradingRunnableEngineFactory;
    }

    private final ITradeSettingsService tradeSettingsService;
    private final TradingRunnableEngineFactory tradingRunnableEngineFactory;

    private ExtendedExecutor executorService;
    private final WorkStatus globalWorkStatus = new WorkStatus(true);
    private List<TradingRunnableEngine> engines = new ArrayList<>();



    public long runnableEnginesCount() {

        if (engines.size() == 0) return 0;

        return engines.stream()
                .filter(engine -> engine.isStopped() == false)
                .count();
    }

    public void saveStop() {
        globalWorkStatus.setNeedStop(true);

        if (this.executorService == null)
            return;

        this.executorService.shutdown();
    }

    public boolean isDone(long timeout, TimeUnit timeUnit) throws InterruptedException {
        if (executorService == null) return true;

        return executorService.awaitTermination(timeout, timeUnit);
    }

    public boolean isStop() {
        return globalWorkStatus.isNeedStop();
    }


    public void correctStart() {
        saveStop();
        launch();
    }

    private void launch() {
        this.engines.clear();

        globalWorkStatus.setNeedStop(false);

        this.engines = tradingRunnableEngineFactory.createTradingRunnableEngines(
                tradeSettingsService.findAll(),
                globalWorkStatus);

        this.executorService = new ExtendedExecutor(engines.size());

        Objects.requireNonNull(this.executorService);

        engines.forEach(executorService::submit);
    }


}








