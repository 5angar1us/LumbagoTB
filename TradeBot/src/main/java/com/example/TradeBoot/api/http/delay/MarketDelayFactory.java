package com.example.TradeBoot.api.http.delay;

import com.example.TradeBoot.api.http.IHttpClientWorker;

public class MarketDelayFactory {


    public MarketDelayFactory(int requestLimit200, int requestLimit1000) {
        this.requestLimit200 = requestLimit200;
        this.requestLimit1000 = requestLimit1000;
    }
    private final int requestLimit200;
    private final int requestLimit1000;

    public MarketDelay create(IHttpClientWorker httpClientWorker){
        return new MarketDelay(httpClientWorker, requestLimit200, requestLimit1000);
    }
}
