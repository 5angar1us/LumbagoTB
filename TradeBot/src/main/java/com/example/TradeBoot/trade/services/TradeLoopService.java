package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.trade.model.TradeInformation;
import com.example.TradeBoot.trade.model.WorkStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TradeLoopService {

    static final Logger log =
            LoggerFactory.getLogger(TradeLoopService.class);

    private TradeService tradeService;
    private WorkStatus workStatus;

    private IPositionStatusService openPositionStatus;

    private IPositionStatusService closePositionStatus;

    private ClosePositionInformationService closePositionInformationService;

    private TradeInformation openPositionTradeInformation;

    private String market;

    public TradeLoopService(TradeService tradeService, WorkStatus workStatus, IPositionStatusService openPositionStatus, IPositionStatusService closePositionStatus, ClosePositionInformationService closePositionInformationService, TradeInformation openPositionTradeInformation, String market) {
        this.tradeService = tradeService;
        this.workStatus = workStatus;
        this.openPositionStatus = openPositionStatus;
        this.closePositionStatus = closePositionStatus;
        this.closePositionInformationService = closePositionInformationService;
        this.openPositionTradeInformation = openPositionTradeInformation;
        this.market = market;
    }

    public void run() {
        log.debug("Run trade loop");
        while (this.workStatus.isNeedStop() == false) {

            tradeService.runTrade(openPositionStatus, openPositionTradeInformation);

            var closePositionTradeInformation = closePositionInformationService
                    .createTradeInformation(market);


            if (closePositionTradeInformation.isPresent()) {
                log.debug("Position opened");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                tradeService.runTrade(closePositionStatus, closePositionTradeInformation.get());
                log.debug("Position closed");
            }
            log.debug("Trade cycle iteration ended");
        }
        log.debug("Trade loop ended");
    }

}
