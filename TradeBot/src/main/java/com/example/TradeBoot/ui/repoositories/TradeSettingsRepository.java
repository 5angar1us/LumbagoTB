package com.example.TradeBoot.ui.repoositories;

import com.example.TradeBoot.ui.models.TradeSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface TradeSettingsRepository extends JpaRepository<TradeSettings, Long> {}

