package com.example.TradeBoot.ui;

import com.example.TradeBoot.ui.models.TradeSettings;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface TradeSettingsRepository extends CrudRepository<TradeSettings, Long> {}

