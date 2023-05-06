package com.example.TradeBoot.configs;

import com.example.TradeBoot.trade.services.FinancialInstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class ServiceUtils {
    private static ServiceUtils instance;

    @Autowired
    private FinancialInstrumentService financialInstrumentService;

    @PostConstruct
    public void fillInstance() {
        instance = this;
    }

    public static FinancialInstrumentService getFinancialInstrumentService() {
        return instance.financialInstrumentService;
    }
}
