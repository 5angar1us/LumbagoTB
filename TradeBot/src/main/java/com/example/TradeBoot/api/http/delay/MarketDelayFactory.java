package com.example.TradeBoot.api.http.delay;

import com.example.TradeBoot.api.http.IHttpClientWorker;

public class MarketDelayFactory {


    public MarketDelayFactory(int requestLimit, int requestLimitForSecond) {
        this.requestLimit = requestLimit;
        this.requestLimitForSecond = requestLimitForSecond;
    }
    private final int requestLimit;
    private final int requestLimitForSecond;

    public MarketDelay create(IHttpClientWorker httpClientWorker){
        return new MarketDelay(httpClientWorker, requestLimit, requestLimitForSecond);
    }
}
