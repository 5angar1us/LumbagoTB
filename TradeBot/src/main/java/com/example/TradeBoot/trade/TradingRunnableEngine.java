package com.example.TradeBoot.trade;

import com.example.TradeBoot.trade.services.TradeLoopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TradingRunnableEngine implements Runnable {

    static final Logger log =
            LoggerFactory.getLogger(TradingRunnableEngine.class);

    protected Boolean isStopped = true;

    protected String marketName;

    protected TradeLoopService tradeLoopService;

    public TradingRunnableEngine(String marketName, TradeLoopService tradeLoopService) {
        this.marketName = marketName;
        this.tradeLoopService = tradeLoopService;
    }

    @Override
    public void run() {
        final Thread currentThread = Thread.currentThread();
        final String defaultName = currentThread.getName();

        currentThread.setName(marketName);
        log.debug("Start Engine " + marketName);
        isStopped = false;

        try {
            tradeLoopService.run();
        } catch (Exception e) {
            isStopped = true;
            throw e;
        }


        log.debug("Stop Engine " + marketName);
        currentThread.setName(defaultName);
        isStopped = true;

    }

    public Boolean isStopped() {
        return isStopped;
    }
}

