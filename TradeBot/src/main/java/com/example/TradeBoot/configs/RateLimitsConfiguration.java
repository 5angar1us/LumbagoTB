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

    @Value("${limits.global1000}")
    private int globalLimit1000;

    @Value("${limits.global200}")
    private int globalLimit200;
    @Value("${limits.market200}")
    private int marketLimit200;

    @Value("${limits.market1000}")
    private int marketLimit1000;

    @Autowired
    @Bean
    @Primary
    public IHttpClientWorker httpClientWorkerWithDelay(HttpClientWorker httpClientWorker){
        return new GlobalDelay(httpClientWorker, globalLimit1000, marketLimit200);
    }
    @Autowired
    @Bean
    public MarketDelayFactory marketDelayFactory(){
        return new MarketDelayFactory(marketLimit200, marketLimit1000);
    }
}
