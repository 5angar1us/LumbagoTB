package com.example.TradeBoot.configs;


import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.api.http.IHttpClientWorker;
import com.example.TradeBoot.api.http.delay.GlobalDelay;
import com.example.TradeBoot.api.http.delay.MarketDelayFactory;
import com.example.TradeBoot.api.services.IPositionsService;
import com.example.TradeBoot.api.services.IWalletService;
import com.example.TradeBoot.notification.EmptyNotificationService;
import com.example.TradeBoot.notification.INotificationService;
import com.example.TradeBoot.trade.TradingRunnableEngineFactory;
import com.example.TradeBoot.trade.services.OrderPriceService;
import com.example.TradeBoot.trade.services.FinancialInstrumentPositionsService;
import com.example.TradeBoot.trade.services.FinancialInstrumentService;
import com.example.TradeBoot.trade.services.VolumeVisitor;
import com.example.TradeBoot.ui.repoositories.TradeSettingsRepository;
import com.example.TradeBoot.ui.service.BaseTradeSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class ConfigureServices {

    @Autowired
    @Bean
    public OrderPriceService orderPriceCalculator() {
        return new OrderPriceService();
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
    public BaseTradeSettingsService baseTradeSettingsService(TradeSettingsRepository tradeSettingsRepository)
    { return  new BaseTradeSettingsService(tradeSettingsRepository);}

    @Bean
    TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        return new TelegramBotsApi(DefaultBotSession.class);
    }

}
