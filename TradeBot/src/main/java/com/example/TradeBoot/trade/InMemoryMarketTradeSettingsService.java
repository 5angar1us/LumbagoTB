package com.example.TradeBoot.trade;

import com.example.TradeBoot.trade.model.MarketTradeSettings;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InMemoryMarketTradeSettingsService {

    public List<MarketTradeSettings> getTradeSettings() {
        return InMemoryTradeSettingsRepostiory.get();
    }

    public void setTradeSettings(List<MarketTradeSettings> marketTradeSettings) {
        InMemoryTradeSettingsRepostiory.set(marketTradeSettings);
    }

    public MarketTradeSettings getTradeSettingsByMarketName(String name){
        var tradeSetting = InMemoryTradeSettingsRepostiory.get()
                .stream()
                .filter(marketTradeSettings -> marketTradeSettings.getMarket().equals(name))
                .findFirst();

        if( tradeSetting.isPresent()) return tradeSetting.get();


        var t = new MarketTradeSettings();
        t.setMarket(name);
        InMemoryTradeSettingsRepostiory.add(t);

        return t;
    }
}
