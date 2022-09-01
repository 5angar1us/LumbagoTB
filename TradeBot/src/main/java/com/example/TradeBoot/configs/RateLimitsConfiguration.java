package com.example.TradeBoot.configs;

import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.api.http.IHttpClientWorker;
import com.example.TradeBoot.api.http.delay.GlobalDelay;
import com.example.TradeBoot.api.http.delay.MarketDelayFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:application.properties"})
public class RateLimitsConfiguration {

    @Value("${limits.global}")
    private int globalLimit;

    @Value("${limits.market}")
    private int marketLimit;

    @Value("${limits.marketForSecond}")
    private int marketLimitForSecond;

    @Autowired
    @Bean
    @Primary
    public IHttpClientWorker httpClientWorkerWithDelay(HttpClientWorker httpClientWorker){
        return new GlobalDelay(httpClientWorker, globalLimit, marketLimit);
    }

    
    @Autowired
    @Bean
    public MarketDelayFactory marketDelayFactory(){
        return new MarketDelayFactory(marketLimit, marketLimitForSecond);
    }
}
