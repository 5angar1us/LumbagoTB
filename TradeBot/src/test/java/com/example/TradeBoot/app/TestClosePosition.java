package com.example.TradeBoot.app;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.configuration.TestDefaultObject;
import com.example.TradeBoot.configuration.TestServiceInstances;
import com.example.TradeBoot.api.services.implemetations.IWalletService;
import com.example.TradeBoot.api.services.implemetations.IPositionsService;
import com.example.TradeBoot.trade.model.OrderInformation;
import com.example.TradeBoot.trade.model.Persent;
import com.example.TradeBoot.trade.model.TradeInformation;
import com.example.TradeBoot.trade.services.ClosePositionInformationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class TestClosePosition {

    private static ClosePositionInformationService closePositionInformationService;

    private static IWalletService.Mock mockWalletService;

    private static IPositionsService.Mock mockPositionsService;

    @BeforeAll
    static void init() {
        mockWalletService = new IWalletService.Mock();
        mockPositionsService = new IPositionsService.Mock();

        closePositionInformationService = new ClosePositionInformationService(
                mockWalletService,
                TestServiceInstances.getFinancialInstrumentService(),
                mockPositionsService);
    }

    @Test
    public void canCloseFuture() {

        var defaultPosition = TestDefaultObject.getBuyPosition();

        var baseSide = ESide.BUY;
        var marketName = defaultPosition.getFuture();

        mockPositionsService.setPositions(List.of(defaultPosition));


        var tradeInformation = closePositionInformationService.createTradeInformation(baseSide, marketName)
                .get();


        var expectedOrderInformation = new OrderInformation(new BigDecimal("0.1"), ESide.SELL, new Persent(0));
        var expectedTradeInformation = new TradeInformation(List.of(expectedOrderInformation));

        assertThat(tradeInformation).isEqualTo(expectedTradeInformation);
    }

    @Test
    public void canCloseCoin()
    {
        var defaultBalance = TestDefaultObject.getBalance();

        var baseSide = ESide.BUY;
        var marketName = defaultBalance.getCoin() + "/USD";

        mockWalletService.setBalances(List.of(defaultBalance));


        var tradeInformation = closePositionInformationService.createTradeInformation(baseSide, marketName)
                .get();


        var expectedOrderInformation = new OrderInformation(new BigDecimal("1.0"), ESide.SELL, new Persent(0));
        var expectedTradeInformation = new TradeInformation(List.of(expectedOrderInformation));

        assertThat(tradeInformation).isEqualTo(expectedTradeInformation);
    }

}
