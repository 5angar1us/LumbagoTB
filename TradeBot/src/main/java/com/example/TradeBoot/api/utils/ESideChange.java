package com.example.TradeBoot.api.utils;

import com.example.TradeBoot.api.domain.markets.ESide;

import java.util.HashMap;
import java.util.Map;

public class ESideChange {

    private static final Map<ESide, ESide> changeMap = new HashMap<>();


    static {
        changeMap.put(ESide.BUY, ESide.SELL);
        changeMap.put(ESide.SELL, ESide.BUY);
    }


    public static ESide change(ESide side){
        return changeMap.get(side);
    }
}
