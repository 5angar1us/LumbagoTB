package com.example.TradeBoot.ui.service;

import com.example.TradeBoot.ui.models.TradeSettings;
import com.example.TradeBoot.ui.service.ITradeSettingsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MockTradeSettingsService implements ITradeSettingsService {

    public void setTradeSettings(List<TradeSettings> tradeSettings) {
        this.tradeSettings = tradeSettings;
    }

    private List<TradeSettings> tradeSettings = new ArrayList<>();

    public MockTradeSettingsService(List<TradeSettings> tradeSettings) {
        this.tradeSettings = tradeSettings;
    }

    public MockTradeSettingsService() {
    }

    @Override
    public TradeSettings update(TradeSettings tradeSettings) {
        return null;
    }

    @Override
    public void save(TradeSettings tradeSettings) {

    }

    @Override
    public void delete(TradeSettings tradeSettings) {

    }

    @Override
    public Optional<TradeSettings> findById(long id) {
        return Optional.empty();
    }

    @Override
    public List<TradeSettings> findAll() {
        return tradeSettings;
    }
}
