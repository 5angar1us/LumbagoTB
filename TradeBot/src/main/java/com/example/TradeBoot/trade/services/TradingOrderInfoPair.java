package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.trade.model.TradeInformation;

record TradingOrderInfoPair(TradingService tradingService, TradeInformation tradeInformation, String market) {
}
