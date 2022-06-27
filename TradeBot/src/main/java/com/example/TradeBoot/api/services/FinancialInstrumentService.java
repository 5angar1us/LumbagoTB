package com.example.TradeBoot.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FinancialInstrumentService {

    @Autowired
    public FinancialInstrumentService(MarketService marketService, FutureService futureService) {
        this.marketService = marketService;
        this.futureService = futureService;
    }


    private MarketService marketService;


    private FutureService futureService;

    public List<String> getAllNames(){

       var marketNames =  marketService.getAllMarkets()
               .stream()
               .map(market -> market.getName());

       var futureNames = futureService.getAllFutures()
               .stream()
               .map(future -> future.getName());


        return Stream.concat(marketNames, futureNames)
                .collect(Collectors.toList());
    }

    public List<String> getStableNames(){

        var marketNames =  marketService.getAllMarkets()
                .stream()
                .map(market -> market.getName());

        var futureNames = futureService.getAllFutures()
                .stream()
                .filter(future -> future.getName().contains("PERP"))
                .map(future -> future.getName());


        return Stream.concat(marketNames, futureNames)
                .collect(Collectors.toList());
    }
}
