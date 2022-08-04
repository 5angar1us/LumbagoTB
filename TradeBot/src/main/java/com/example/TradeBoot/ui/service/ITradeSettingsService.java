package com.example.TradeBoot.ui.service;

import com.example.TradeBoot.ui.models.TradeSettings;

import java.util.List;
import java.util.Optional;

public interface ITradeSettingsService {
    TradeSettings update(TradeSettings tradeSettings);

    void save(TradeSettings tradeSettings);

    void delete(TradeSettings tradeSettings);

    Optional<TradeSettings> findById(long id);

    List<TradeSettings> findAll();
}
