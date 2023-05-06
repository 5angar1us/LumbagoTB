package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.api.domain.EInstrumentType;
import com.example.TradeBoot.api.domain.StandardMarketEntity;
import com.example.TradeBoot.api.services.IFutureService;
import com.example.TradeBoot.api.services.IMarketService;
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

    private final IMarketService IMarketService;

    private final IFutureService IFutureService;

    public List<String> getAllNames(){

       var marketNames =  IMarketService.getAllMarkets()
               .stream()
               .map(StandardMarketEntity::getName);

       var futureNames = IFutureService.getAllFutures()
               .stream()
               .map(StandardMarketEntity::getName);


        return Stream.concat(marketNames, futureNames)
                .collect(Collectors.toList());
    }

    public List<String> getStableNames(){

        var marketNames =  IMarketService.getAllMarkets()
                .stream()
                .map(StandardMarketEntity::getName);

        var futureNames = IFutureService.getAllFutures()
                .stream()
                .map(StandardMarketEntity::getName)
                .filter(name -> name.contains("PERP"));


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
