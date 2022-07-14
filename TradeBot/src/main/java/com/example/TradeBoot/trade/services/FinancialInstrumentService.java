package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.api.domain.EInstrumentType;
import com.example.TradeBoot.api.services.implemetations.IFutureService;
import com.example.TradeBoot.api.services.implemetations.IMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FinancialInstrumentService  {

    @Autowired
    public FinancialInstrumentService(IMarketService IMarketService, IFutureService IFutureService) {
        this.IMarketService = IMarketService;
        this.IFutureService = IFutureService;
    }


    private IMarketService IMarketService;


    private IFutureService IFutureService;

    public List<String> getAllNames(){

       var marketNames =  IMarketService.getAllMarkets()
               .stream()
               .map(market -> market.getName());

       var futureNames = IFutureService.getAllFutures()
               .stream()
               .map(future -> future.getName());


        return Stream.concat(marketNames, futureNames)
                .collect(Collectors.toList());
    }

    public List<String> getStableNames(){

        var marketNames =  IMarketService.getAllMarkets()
                .stream()
                .map(market -> market.getName());

        var futureNames = IFutureService.getAllFutures()
                .stream()
                .filter(future -> future.getName().contains("PERP"))
                .map(future -> future.getName());


        return Stream.concat(marketNames, futureNames)
                .collect(Collectors.toList());
    }
    
    public EInstrumentType getInstrumentType(String name){
        
            int coinSeparatorIndex = name.indexOf("/");
            int futureSeparatorIndex = name.indexOf("-");
            
            if(coinSeparatorIndex != -1) return EInstrumentType.COIN;
            if(futureSeparatorIndex != -1) return EInstrumentType.FUTURE;
            
            return EInstrumentType.EMPTY;
    }
}
