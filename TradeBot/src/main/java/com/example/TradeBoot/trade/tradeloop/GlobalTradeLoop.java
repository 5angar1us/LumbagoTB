package com.example.TradeBoot.trade.tradeloop;

import com.example.TradeBoot.trade.model.WorkStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("ALL")
public class GlobalTradeLoop {

    static final Logger log =
            LoggerFactory.getLogger(GlobalTradeLoop.class);

    private final LocalTradeLoop placeTrapsTradeLoop;

    private final LocalTradeLoop saleProductionTradeLoop;
    private WorkStatus workStatus;


    public GlobalTradeLoop(LocalTradeLoop placeTrapsTradeLoop, LocalTradeLoop saleProductionTradeLoop,  WorkStatus workStatus) {
        this.placeTrapsTradeLoop = placeTrapsTradeLoop;
        this.saleProductionTradeLoop = saleProductionTradeLoop;
        this.workStatus = workStatus;
    }

    public void run() {
        log.debug("Run trade loop");
        while (this.workStatus.isNeedStop() == false) {

            placeTrapsTradeLoop.runTrade();

            saleProductionTradeLoop.runTrade();

            log.debug("Trade cycle iteration ended");
        }
        log.debug("Trade loop ended");
    }

}
