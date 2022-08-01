package com.example.TradeBoot.trade.services.tradingEngine;

import com.example.TradeBoot.trade.model.TradeInformation;
import com.example.TradeBoot.trade.model.TradeStatus;
import com.example.TradeBoot.trade.services.ClosePositionInformationService;
import com.example.TradeBoot.trade.services.FinancialInstrumentPositionsService;
import com.example.TradeBoot.trade.services.TradingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ITradingRunnableEngine extends Runnable {

    static final Logger log =
            LoggerFactory.getLogger(ITradingRunnableEngine.class);

    Boolean isStopped();

    class Abstract implements ITradingRunnableEngine {
        protected TradeStatus tradeStatus;

        protected Boolean isStopped = true;

        protected TradingService tradingService;

        protected TradeInformation openPositionTradeInformation;
        protected ClosePositionInformationService closePositionInformationService;

        protected String market;

        protected IPositionStatus openPositionStatus;

        protected IPositionStatus closePositionStatus;

        public Abstract(
                TradingService tradingService,
                ClosePositionInformationService closePositionInformationService,
                TradeInformation openPositionTradeInformation,
                String market,
                TradeStatus tradeStatus,
                IPositionStatus openPositionStatus,
                IPositionStatus closePositionStatus) {
            this.tradingService = tradingService;
            this.closePositionInformationService = closePositionInformationService;
            this.openPositionTradeInformation = openPositionTradeInformation;
            this.market = market;
            this.tradeStatus = tradeStatus;
            this.openPositionStatus = openPositionStatus;
            this.closePositionStatus = closePositionStatus;
        }

        @Override
        public void run() {

        }

        @Override
        public Boolean isStopped() {
            return isStopped;
        }
    }

    class Base extends ITradingRunnableEngine.Abstract {

        public Base(
                TradingService tradingService,
                ClosePositionInformationService closePositionInformationService,
                TradeInformation openPositionTradeInformation,
                String market,
                TradeStatus tradeStatus,
                IPositionStatus openPositionStatus,
                IPositionStatus closePositionStatus) {

            super(
                    tradingService,
                    closePositionInformationService,
                    openPositionTradeInformation,
                    market,
                    tradeStatus,
                    openPositionStatus,
                    closePositionStatus);
        }

        @Override
        public void run() {
            final Thread currentThread = Thread.currentThread();
            final String defaultName = currentThread.getName();


            currentThread.setName(market);
            log.debug("Start Engine " + market);
            isStopped = false;

            while (this.tradeStatus.isNeedStop() == false) {

                tradingService.workWithOrders(openPositionStatus, openPositionTradeInformation);
                log.debug("Work with orders end");
                var closePositionTradeInformation = closePositionInformationService
                        .createTradeInformation(market);


                if (closePositionTradeInformation.isPresent()) {
                    tradingService.workWithOrders(closePositionStatus, closePositionTradeInformation.get());
                    log.debug("Position closed");
                }
                log.debug("Trade cycle ended");
            }
            log.debug("Stop Engine " + market);
            currentThread.setName(defaultName);

            isStopped = true;

        }

        public Boolean isStopped() {
            return isStopped;
        }
    }

    class Mock extends ITradingRunnableEngine.Abstract {

        public Mock(
                TradingService tradingService,
                ClosePositionInformationService closePositionInformationService,
                TradeInformation openPositionTradeInformation,
                String market,
                TradeStatus tradeStatus,
                IPositionStatus openPositionStatus,
                IPositionStatus closePositionStatus) {

            super(
                    tradingService,
                    closePositionInformationService,
                    openPositionTradeInformation,
                    market,
                    tradeStatus,
                    openPositionStatus,
                    closePositionStatus);
        }

        @Override
        public void run() {

            final Thread currentThread = Thread.currentThread();
            final String defaultName = currentThread.getName();
            currentThread.setName("Trade in market " + market);
            isStopped = false;

            tradingService.workWithOrders(openPositionStatus, openPositionTradeInformation);

            var closePositionTradeInformation = closePositionInformationService
                    .createTradeInformation(market);

            if (closePositionTradeInformation.isPresent()) {
                tradingService.workWithOrders(closePositionStatus, closePositionTradeInformation.get());
            }

            currentThread.setName(defaultName);
            isStopped = true;
        }


    }
}

