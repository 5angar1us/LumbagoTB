package com.example.TradeBoot.configs;

import com.example.TradeBoot.ui.service.BaseTradeSettingsService;
import com.example.TradeBoot.ui.repoositories.TradeSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigureServices2 {

    @Autowired
    @Bean
    public BaseTradeSettingsService tradeSettingsRepositoryWrapper(TradeSettingsRepository tradeSettingsRepository)
    { return  new BaseTradeSettingsService(tradeSettingsRepository);}
}
