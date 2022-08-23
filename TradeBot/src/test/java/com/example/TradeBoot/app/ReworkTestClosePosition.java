package com.example.TradeBoot.app;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.configuration.TestDefaultObject;
import com.example.TradeBoot.configuration.TestServiceInstances;
import com.example.TradeBoot.api.services.IWalletService;
import com.example.TradeBoot.api.services.IPositionsService;
import com.example.TradeBoot.trade.model.OrderInformation;
import com.example.TradeBoot.trade.model.Persent;
import com.example.TradeBoot.trade.model.TradeInformation;
import com.example.TradeBoot.trade.services.ClosePositionInformationService;
import com.example.TradeBoot.trade.services.FinancialInstrumentPositionsService;
import com.example.TradeBoot.trade.services.VolumeVisitor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class ReworkTestClosePosition {

    private static ClosePositionInformationService closePositionInformationService;

    private static FinancialInstrumentPositionsService financialInstrumentPositionsService;

    private static IWalletService.Mock mockWalletService;

    private static IPositionsService.Mock mockPositionsService;

    @BeforeAll
    static void init() {
        mockWalletService = new IWalletService.Mock();
        mockPositionsService = new IPositionsService.Mock();

        var coinHandler = new VolumeVisitor.CoinVolumeVisitor(TestServiceInstances.getWalletService());
        var futureHandler = new VolumeVisitor.FutureVolumeVisitor(TestServiceInstances.getPositionsService());

        closePositionInformationService = new ClosePositionInformationService(
                mockWalletService,
                TestServiceInstances.getFinancialInstrumentService(),
                mockPositionsService, coinHandler, futureHandler);
    }

    @Test
    public void canCloseFuture() {

        var defaultPosition = TestDefaultObject.getBuyPosition();

        var marketName = defaultPosition.getFuture();

        mockPositionsService.setPositions(List.of(defaultPosition));

        var positionSize = financialInstrumentPositionsService.getPositionNetSize(marketName);
        var tradeInformation = closePositionInformationService.createTradeInformation(positionSize)
                .get();

        var expectedOrderInformation = new OrderInformation(new BigDecimal("0.1"), ESide.SELL, new Persent(0));
        var expectedTradeInformation = new TradeInformation(List.of(expectedOrderInformation));

        assertThat(tradeInformation).isEqualTo(expectedTradeInformation);
    }

    @Test
    public void canCloseCoin()
    {
        var defaultBalance = TestDefaultObject.getBalance();

        var marketName = defaultBalance.getCoin() + "/USD";

        mockWalletService.setBalances(List.of(defaultBalance));

        var positionSize = financialInstrumentPositionsService.getPositionNetSize(marketName);
        var tradeInformation = closePositionInformationService.createTradeInformation(positionSize)
                .get();


        var expectedOrderInformation = new OrderInformation(new BigDecimal("1.0"), ESide.SELL, new Persent(0));
        var expectedTradeInformation = new TradeInformation(List.of(expectedOrderInformation));

        assertThat(tradeInformation).isEqualTo(expectedTradeInformation);
    }

}
