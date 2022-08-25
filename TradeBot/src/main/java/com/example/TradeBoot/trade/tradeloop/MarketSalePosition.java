package com.example.TradeBoot.trade.tradeloop;

import com.example.TradeBoot.trade.model.MarketInformation;
import com.example.TradeBoot.trade.services.ClosePositionInformationService;
import com.example.TradeBoot.trade.services.FinancialInstrumentPositionsService;

public class MarketSalePosition {

    FinancialInstrumentPositionsService financialInstrumentPositionsService;
    ClosePositionInformationService closePositionInformationService;

    MarketInformation marketInformation;

    public void t(){

       var positionSize = financialInstrumentPositionsService.getPositionNetSize(marketInformation.market());

        var closePositionTradeInformation = closePositionInformationService
                .createTradeInformation(positionSize);

       if(closePositionTradeInformation.isPresent()){

       }


    }
}
