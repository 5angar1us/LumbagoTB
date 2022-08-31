package com.example.TradeBoot.api.http.delay;

import com.example.TradeBoot.api.http.IHttpClientWorker;

public class MarketDelayFactory {

    private final int requestLimit;

    public MarketDelayFactory(int requestLimit) {
        this.requestLimit = requestLimit;
    }

    public MarketDelay create(IHttpClientWorker httpClientWorker){
        return new MarketDelay(httpClientWorker, requestLimit);
    }
}
