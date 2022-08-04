package com.example.TradeBoot.configs;


import com.example.TradeBoot.api.services.IPositionsService;
import com.example.TradeBoot.api.services.IWalletService;
import com.example.TradeBoot.trade.calculator.OrderPriceCalculator;
import com.example.TradeBoot.trade.services.FinancialInstrumentPositionsService;
import com.example.TradeBoot.trade.services.FinancialInstrumentService;
import com.example.TradeBoot.trade.services.VolumeVisitor;
import com.example.TradeBoot.ui.repoositories.TradeSettingsRepository;
import com.example.TradeBoot.ui.service.BaseTradeSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigureServices {

    @Autowired
    @Bean
    public OrderPriceCalculator orderPriceCalculator() {
        return new OrderPriceCalculator();
    }

    @Autowired
    @Bean
    public VolumeVisitor.CoinVolumeVisitor coinHandler(IWalletService walletService) {
        return new VolumeVisitor.CoinVolumeVisitor(walletService);
    }

    @Autowired
    @Bean
    public VolumeVisitor.FutureVolumeVisitor futureHandler(IPositionsService iPositionsService) {
        return new VolumeVisitor.FutureVolumeVisitor(iPositionsService);
    }

    @Autowired
    @Bean
    public FinancialInstrumentPositionsService financialInstrumentPositionsService(
            FinancialInstrumentService financialInstrumentService,
            IWalletService walletService,
            IPositionsService iPositionsService) {
        return new FinancialInstrumentPositionsService(financialInstrumentService,
                walletService,
                iPositionsService);
    }

    @Autowired
    @Bean
    public BaseTradeSettingsService baseTradeSettingsService(TradeSettingsRepository tradeSettingsRepository)
    { return  new BaseTradeSettingsService(tradeSettingsRepository);}
}
