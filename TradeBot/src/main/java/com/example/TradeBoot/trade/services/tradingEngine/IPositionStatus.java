package com.example.TradeBoot.trade.services.tradingEngine;

import com.example.TradeBoot.trade.services.FinancialInstrumentPositionsService;

public interface IPositionStatus {

    boolean getPositionStatus(String marketName);

    class OpenPositionStatus implements IPositionStatus{
        private FinancialInstrumentPositionsService financialInstrumentPositionsService;

        public OpenPositionStatus(FinancialInstrumentPositionsService financialInstrumentPositionsService) {
            this.financialInstrumentPositionsService = financialInstrumentPositionsService;
        }

        @Override
        public boolean getPositionStatus(String marketName) {
            return financialInstrumentPositionsService.isPositionOpen(marketName);
        }
    }
    class ClosePositionStatus implements IPositionStatus{


        private FinancialInstrumentPositionsService financialInstrumentPositionsService;

        public ClosePositionStatus(FinancialInstrumentPositionsService financialInstrumentPositionsService) {
            this.financialInstrumentPositionsService = financialInstrumentPositionsService;
        }

        @Override
        public boolean getPositionStatus(String marketName) {
            return !financialInstrumentPositionsService.isPositionOpen(marketName);
        }
    }
}
