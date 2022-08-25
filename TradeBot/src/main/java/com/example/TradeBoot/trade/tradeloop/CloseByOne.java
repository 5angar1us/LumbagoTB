package com.example.TradeBoot.trade.tradeloop;

import com.example.TradeBoot.api.services.IOrdersService;
import com.example.TradeBoot.trade.model.MarketInformation;
import com.example.TradeBoot.trade.tradeloop.interfaces.ICloseOrders;

public class CloseByOne implements ICloseOrders {


    public CloseByOne(IOrdersService.Abstract ordersService, MarketInformation marketInformation) {
        this.ordersService = ordersService;
        this.marketInformation = marketInformation;
    }

    private IOrdersService.Abstract ordersService;

    private MarketInformation marketInformation;
    @Override
    public void close() {
        ordersService.cancelAllOrderByMarketByOne(marketInformation.market());
    }
}
