package com.example.TradeBoot.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigureServices2 {

    @Autowired
    @Bean
    public TradeSettingsRepositoryWrapper tradeSettingsRepositoryWrapper(TradeSettingsRepository tradeSettingsRepository)
    { return  new TradeSettingsRepositoryWrapper(tradeSettingsRepository);}
}
