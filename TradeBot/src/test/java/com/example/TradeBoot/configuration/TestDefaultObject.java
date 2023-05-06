package com.example.TradeBoot.configuration;

import com.example.TradeBoot.api.domain.account.Position;
import com.example.TradeBoot.api.domain.wallet.Balance;

import java.math.BigDecimal;

public class TestDefaultObject {

    public static Position getBuyPosition() {
        return buyPosition;
    }

    public static Balance getBalance() {
        return balance;
    }

    private static final Position buyPosition;

    private static final Balance balance;


    static {
        var sourcePosition = new Position();
        sourcePosition.setCost(new BigDecimal("0.0898"));
        sourcePosition.setEntryPrice(new BigDecimal("0.898"));
        sourcePosition.setEstimatedLiquidationPrice(new BigDecimal("0.0"));
        sourcePosition.setFuture("CEL-PERP");
        sourcePosition.setInitialMarginRequirement(new BigDecimal("0.05"));
        sourcePosition.setLongOrderSize(new BigDecimal("0.0"));
        sourcePosition.setMaintenanceMarginRequirement(new BigDecimal("0.03"));
        sourcePosition.setNetSize(new BigDecimal("0.1"));
        sourcePosition.setOpenSize(new BigDecimal("0.1"));
        sourcePosition.setRealizedPnl(new BigDecimal("0.04835125"));
        sourcePosition.setShortOrderSize(new BigDecimal("0.0"));
        sourcePosition.setSide("BUY");
        sourcePosition.setSize(new BigDecimal("0.1"));
        sourcePosition.setUnrealizedPnl(new BigDecimal("0.0"));
        sourcePosition.setCollateralUsed(new BigDecimal("0.00449"));

        buyPosition = sourcePosition;

        var sourceBalance = new Balance();
        sourceBalance.setCoin("GMT");
        sourceBalance.setFree(new BigDecimal("1.0"));
        sourceBalance.setSpotBorrow(new BigDecimal("0.0"));
        sourceBalance.setTotal(new BigDecimal("1.0"));
        sourceBalance.setUsdValue(new BigDecimal("0.85073867"));
        sourceBalance.setAvailableWithoutBorrow(new BigDecimal("1.0"));
        sourceBalance.setAvailableForWithdrawal(new BigDecimal("1.0"));

        balance = sourceBalance;

    }
}
