package com.example.TradeBoot.ui;

import com.example.TradeBoot.ui.models.TradeSettings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TradeSettingsService {

    private TradeSettingsRepository tradeSettingsRepository;

    @Autowired
    public TradeSettingsService(TradeSettingsRepository tradeSettingsRepository) {
        this.tradeSettingsRepository = tradeSettingsRepository;
    }
    public TradeSettings update(TradeSettings tradeSettings){
        var preventDetails = tradeSettingsRepository.findById(tradeSettings.getId()).orElseThrow();

        var preventDetailsId = preventDetails.getTradeSettingsDetails().stream()
                .map(tradeSettingsDetail -> tradeSettingsDetail.getId())
                .collect(Collectors.toList());

        var targetDetailsId = tradeSettings.getTradeSettingsDetails().stream()
                .map(tradeSettingsDetail -> tradeSettingsDetail.getId())
                .collect(Collectors.toList());

        var toDelete = preventDetailsId.stream()
                .filter(i -> !targetDetailsId.contains(i))
                .collect(Collectors.toList());

        var toDeleteDetails = preventDetails.getTradeSettingsDetails().stream()
                .filter(tradeSettingsDetail -> toDelete.contains(tradeSettingsDetail))
                .collect(Collectors.toList());
        ;

        toDeleteDetails.forEach(tradeSettingsDetail -> tradeSettingsDetail.setTradeSettings(null));

        tradeSettings.addAllDetail(toDeleteDetails);

       return tradeSettingsRepository.save(tradeSettings);
    }

    public TradeSettingsRepository getRepository(){
        return tradeSettingsRepository;
    }

    public void save(TradeSettings tradeSettings) {
        tradeSettings.getTradeSettingsDetails().stream()
                .forEach(tradeSettingsDetail -> tradeSettingsDetail.setId(0));

        tradeSettingsRepository.save(tradeSettings);
    }

    public void delete(TradeSettings tradeSettings) {
        tradeSettingsRepository.delete(tradeSettings);
    }

    public Optional<TradeSettings> findById(long id) {
        return tradeSettingsRepository.findById(id);
    }

    public List<TradeSettings> findAll() {
        return StreamSupport.stream( tradeSettingsRepository.findAll().spliterator(), false)
                                .collect(Collectors.toList());

    }
}
