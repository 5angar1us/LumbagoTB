package com.example.TradeBoot.trade.services.tradingEngine;

import com.example.TradeBoot.trade.model.TradeInformation;
import com.example.TradeBoot.trade.model.TradeStatus;
import com.example.TradeBoot.trade.services.ClosePositionInformationService;
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

        public Abstract(
                TradingService tradingService,
                ClosePositionInformationService closePositionInformationService,
                TradeInformation openPositionTradeInformation,
                String market,
                TradeStatus tradeStatus) {
            this.tradingService = tradingService;
            this.closePositionInformationService = closePositionInformationService;
            this.openPositionTradeInformation = openPositionTradeInformation;
            this.market = market;
            this.tradeStatus = tradeStatus;
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
                TradeStatus tradeStatus) {

            super(
                    tradingService,
                    closePositionInformationService,
                    openPositionTradeInformation,
                    market,
                    tradeStatus);
        }

        @Override
        public void run() {
            final Thread currentThread = Thread.currentThread();
            final String defaultName = currentThread.getName();

            currentThread.setName(market + " " + openPositionTradeInformation.getBaseSide());
            log.debug("Start Engine " + market + " " + openPositionTradeInformation.getBaseSide());
            isStopped = false;

            while (this.tradeStatus.isNeedStop() == false) {

                tradingService.workWithOrders(openPositionTradeInformation);
                log.debug("work with orders end");
                var closePositionTradeInformation = closePositionInformationService.createTradeInformation(
                        openPositionTradeInformation.getBaseSide(),
                        market);


                if (closePositionTradeInformation.isPresent()) {
                    tradingService.workWithOrders(closePositionTradeInformation.get());
                }
            }
            log.debug("Stop Engine " + market + " " + openPositionTradeInformation.getBaseSide());
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
                TradeStatus tradeStatus) {

            super(
                    tradingService,
                    closePositionInformationService,
                    openPositionTradeInformation,
                    market,
                    tradeStatus);
        }

        @Override
        public void run() {

            final Thread currentThread = Thread.currentThread();
            final String defaultName = currentThread.getName();
            currentThread.setName("Trade in market " + market + " " + openPositionTradeInformation.getBaseSide());
            isStopped = false;

            tradingService.workWithOrders(openPositionTradeInformation);

            var closePositionTradeInformation = closePositionInformationService.createTradeInformation(
                    openPositionTradeInformation.getBaseSide(),
                    market);

            if (closePositionTradeInformation.isPresent()) {
                tradingService.workWithOrders(closePositionTradeInformation.get());
            }

            currentThread.setName(defaultName);
            isStopped = true;
        }


    }
}

