package com.example.TradeBoot.configs;


import com.example.TradeBoot.api.http.HttpClientWorkerWithDelay;
import com.example.TradeBoot.api.http.IHttpClientWorker;
import com.example.TradeBoot.api.services.IPositionsService;
import com.example.TradeBoot.api.services.IWalletService;
import com.example.TradeBoot.api.services.OrdersService;
import com.example.TradeBoot.trade.calculator.OrderPriceCalculator;
import com.example.TradeBoot.trade.services.CoinHandler;
import com.example.TradeBoot.trade.services.FinancialInstrumentPositionsService;
import com.example.TradeBoot.trade.services.FinancialInstrumentService;
import com.example.TradeBoot.trade.services.FutureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class ConfigureServices {

    @Autowired
    @Bean
    public OrderPriceCalculator orderPriceCalculator() {
        return new OrderPriceCalculator();
    }

    @Autowired
    @Bean
    public CoinHandler coinHandler(IWalletService walletService) {
        return new CoinHandler(walletService);
    }

    @Autowired
    @Bean
    public FutureHandler futureHandler(IPositionsService iPositionsService) {
        return new FutureHandler(iPositionsService);
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

}
