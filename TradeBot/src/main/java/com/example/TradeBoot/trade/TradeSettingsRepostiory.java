package com.example.TradeBoot.trade;

import com.example.TradeBoot.trade.model.MarketTradeSettings;

import java.util.ArrayList;
import java.util.List;

public class TradeSettingsRepostiory {

    private static List<MarketTradeSettings> _Market_tradeSettings = new ArrayList<>();

    public static List<MarketTradeSettings> get() {
        return _Market_tradeSettings;
    }

    public static void set(List<MarketTradeSettings> marketTradeSettings) {
        _Market_tradeSettings = marketTradeSettings;
    }

    public static void add(MarketTradeSettings marketTradeSettings){
        _Market_tradeSettings.add(marketTradeSettings);
    }

}
