package com.example.TradeBoot.configs;


import com.example.TradeBoot.trade.calculator.OrderPriceCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:application.properties"})
public class ConfigureServices {

    @Autowired
    @Bean
    public OrderPriceCalculator orderPriceCalculator() { return  new OrderPriceCalculator();}


}
