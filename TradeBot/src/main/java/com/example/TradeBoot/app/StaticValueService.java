package com.example.TradeBoot.app;

import com.example.TradeBoot.trade.model.MarketTradeSettings;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaticValueService {

    public List<MarketTradeSettings> getTradeSettings() {
        return TradeSettingsRepostiory.get();
    }

    public void setTradeSettings(List<MarketTradeSettings> marketTradeSettings) {
        TradeSettingsRepostiory.set(marketTradeSettings);
    }

    public MarketTradeSettings getTradeSettingsByMarketName(String name){
        var tradeSetting = TradeSettingsRepostiory.get()
                .stream()
                .filter(marketTradeSettings -> marketTradeSettings.getMarket().equals(name))
                .findFirst();

        if( tradeSetting.isPresent()) return tradeSetting.get();


        var t = new MarketTradeSettings();
        t.setMarket(name);
        TradeSettingsRepostiory.add(t);

        return t;
    }
}
