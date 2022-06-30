package com.example.TradeBoot.trade.services.tradingEngine;

import com.example.TradeBoot.trade.model.TradeInformation;
import com.example.TradeBoot.trade.services.TradingService;

public record TradingOrderInfoPair(TradingService tradingService, TradeInformation tradeInformation, String market) {
}
