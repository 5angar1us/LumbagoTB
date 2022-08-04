package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.trade.model.TradeInformation;
import com.example.TradeBoot.trade.model.TradeStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TradeLoopService {

    static final Logger log =
            LoggerFactory.getLogger(TradeLoopService.class);

    private TradeService tradeService;
    private TradeStatus tradeStatus;

    private IPositionStatusService openPositionStatus;

    private IPositionStatusService closePositionStatus;

    private ClosePositionInformationService closePositionInformationService;

    private TradeInformation openPositionTradeInformation;

    private String market;

    public TradeLoopService(TradeService tradeService, TradeStatus tradeStatus, IPositionStatusService openPositionStatus, IPositionStatusService closePositionStatus, ClosePositionInformationService closePositionInformationService, TradeInformation openPositionTradeInformation, String market) {
        this.tradeService = tradeService;
        this.tradeStatus = tradeStatus;
        this.openPositionStatus = openPositionStatus;
        this.closePositionStatus = closePositionStatus;
        this.closePositionInformationService = closePositionInformationService;
        this.openPositionTradeInformation = openPositionTradeInformation;
        this.market = market;
    }

    public void run() {
        log.debug("Run trade loop");
        while (this.tradeStatus.isNeedStop() == false) {

            tradeService.trade(openPositionStatus, openPositionTradeInformation);
            log.debug("P");
            var closePositionTradeInformation = closePositionInformationService
                    .createTradeInformation(market);


            if (closePositionTradeInformation.isPresent()) {
                tradeService.trade(closePositionStatus, closePositionTradeInformation.get());
                log.debug("Position closed");
            }
            log.debug("Trade cycle iteration ended");
        }
        log.debug("Trade loop ended");
    }
}
