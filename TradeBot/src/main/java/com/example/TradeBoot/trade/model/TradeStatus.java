package com.example.TradeBoot.trade.model;

public class TradeStatus {

    public TradeStatus(boolean isNeedStop) {
        this.isNeedStop = isNeedStop;
    }

    public boolean isNeedStop() {
        return isNeedStop;
    }

    public void setNeedStop(boolean needStop) {
        isNeedStop = needStop;
    }

    private boolean isNeedStop;
}
