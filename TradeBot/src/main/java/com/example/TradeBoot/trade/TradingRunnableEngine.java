package com.example.TradeBoot.trade;

import com.example.TradeBoot.trade.tradeloop.GlobalTradeLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TradingRunnableEngine implements Runnable {

    static final Logger log =
            LoggerFactory.getLogger(TradingRunnableEngine.class);

    protected Boolean isStopped = true;

    protected String marketName;

    protected GlobalTradeLoop globalTradeLoop;

    public TradingRunnableEngine(String marketName, GlobalTradeLoop globalTradeLoop) {
        this.marketName = marketName;
        this.globalTradeLoop = globalTradeLoop;
    }

    @Override
    public void run() {
        final Thread currentThread = Thread.currentThread();
        final String defaultName = currentThread.getName();

        currentThread.setName(marketName);
        log.debug("Start Engine " + marketName);
        isStopped = false;

        globalTradeLoop.run();


        log.debug("Stop Engine " + marketName);
        currentThread.setName(defaultName);
        isStopped = true;

    }

    public Boolean isStopped() {
        return isStopped;
    }
}

