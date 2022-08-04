package com.example.TradeBoot.trade.services;

public interface IPositionStatusService {

    boolean getPositionStatus(String marketName);

    class OpenPositionStatusService implements IPositionStatusService {
        private FinancialInstrumentPositionsService financialInstrumentPositionsService;

        public OpenPositionStatusService(FinancialInstrumentPositionsService financialInstrumentPositionsService) {
            this.financialInstrumentPositionsService = financialInstrumentPositionsService;
        }

        @Override
        public boolean getPositionStatus(String marketName) {
            return financialInstrumentPositionsService.isPositionOpen(marketName);
        }
    }
    class ClosePositionStatusService implements IPositionStatusService {


        private FinancialInstrumentPositionsService financialInstrumentPositionsService;

        public ClosePositionStatusService(FinancialInstrumentPositionsService financialInstrumentPositionsService) {
            this.financialInstrumentPositionsService = financialInstrumentPositionsService;
        }

        @Override
        public boolean getPositionStatus(String marketName) {
            return financialInstrumentPositionsService.isPositionOpen(marketName) == false;
        }
    }
}
