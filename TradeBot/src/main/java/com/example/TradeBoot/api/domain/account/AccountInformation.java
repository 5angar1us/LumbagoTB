package com.example.TradeBoot.api.domain.account;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class AccountInformation {

    private boolean backstopProvider;

    private BigDecimal collateral;

    private BigDecimal freeCollateral;

    private BigDecimal initialMarginRequirement;

    private boolean liquidating;

    private BigDecimal maintenanceMarginRequirement;

    private BigDecimal makerFee;

    private BigDecimal marginFraction;

    private BigDecimal openMarginFraction;

    private BigDecimal takerFee;

    private BigDecimal totalAccountValue;

    private BigDecimal totalPositionSize;

    private BigDecimal totalAccountNav;

    private String username;

    private BigDecimal leverage;

    @Override
    public String toString() {
        return "AccountInformation{" +
                "backstopProvider=" + backstopProvider +
                ", collateral=" + collateral +
                ", freeCollateral=" + freeCollateral +
                ", initialMarginRequirement=" + initialMarginRequirement +
                ", liquidating=" + liquidating +
                ", maintenanceMarginRequirement=" + maintenanceMarginRequirement +
                ", makerFee=" + makerFee +
                ", marginFraction=" + marginFraction +
                ", openMarginFraction=" + openMarginFraction +
                ", takerFee=" + takerFee +
                ", totalAccountValue=" + totalAccountValue +
                ", totalPositionSize=" + totalPositionSize +
                ", username='" + username + '\'' +
                ", leverage=" + leverage +
                ", positions=" + positions +
                ", accountIdentifier=" + accountIdentifier +
                ", accountType='" + accountType + '\'' +
                ", positionLimitUsed='" + positionLimitUsed + '\'' +
                ", spotLendingEnabled=" + spotLendingEnabled +
                ", spotMarginWithdrawalsEnabled=" + spotMarginWithdrawalsEnabled +
                ", spotMarginEnabled=" + spotMarginEnabled +
                ", chargeInterestOnNegativeUsd=" + chargeInterestOnNegativeUsd +
                ", useFttCollateral=" + useFttCollateral +
                ", positionLimit=" + positionLimit +
                ", futuresLeverage=" + futuresLeverage +
                '}';
    }

    private List<Position> positions;

    private BigDecimal accountIdentifier;

    private String accountType;

    private String positionLimitUsed;

    private boolean spotLendingEnabled;

    private boolean spotMarginWithdrawalsEnabled;

    private boolean spotMarginEnabled;

    private boolean chargeInterestOnNegativeUsd;

    private boolean useFttCollateral;

    private BigDecimal positionLimit;

    private BigDecimal futuresLeverage;

    public void setFuturesLeverage(BigDecimal futuresLeverage) {
        this.futuresLeverage = futuresLeverage;
    }
    public void setPositionLimitUsed(String positionLimitUsed) {
        this.positionLimitUsed = positionLimitUsed;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setSpotMarginEnabled(boolean spotMarginEnabled) {
        this.spotMarginEnabled = spotMarginEnabled;
    }

    public void setSpotMarginWithdrawalsEnabled(boolean spotMarginWithdrawalsEnabled) {
        this.spotMarginWithdrawalsEnabled = spotMarginWithdrawalsEnabled;
    }

    public void setSpotLendingEnabled(boolean spotLendingEnabled) {
        this.spotLendingEnabled = spotLendingEnabled;
    }

    public void setChargeInterestOnNegativeUsd(boolean chargeInterestOnNegativeUsd) {
        this.chargeInterestOnNegativeUsd = chargeInterestOnNegativeUsd;
    }

    public void setUseFttCollateral(boolean useFttCollateral) {
        this.useFttCollateral = useFttCollateral;
    }

    public void setPositionLimit(BigDecimal positionLimit) {
        this.positionLimit = positionLimit;
    }

    public BigDecimal getAccountIdentifier() {
        return accountIdentifier;
    }

    public void setAccountIdentifier(BigDecimal accountIdentifier) {
        this.accountIdentifier = accountIdentifier;
    }

    public boolean isBackstopProvider() {
        return this.backstopProvider;
    }

    public void setBackstopProvider(boolean backstopProvider) {
        this.backstopProvider = backstopProvider;
    }

    public BigDecimal getCollateral() {
        return this.collateral;
    }

    public void setCollateral(BigDecimal collateral) {
        this.collateral = collateral;
    }

    public BigDecimal getFreeCollateral() {
        return this.freeCollateral;
    }

    public void setFreeCollateral(BigDecimal freeCollateral) {
        this.freeCollateral = freeCollateral;
    }

    public BigDecimal getInitialMarginRequirement() {
        return this.initialMarginRequirement;
    }

    public void setInitialMarginRequirement(BigDecimal initialMarginRequirement) {
        this.initialMarginRequirement = initialMarginRequirement;
    }

    public BigDecimal getLeverage() {
        return this.leverage;
    }

    public void setLeverage(BigDecimal leverage) {
        this.leverage = leverage;
    }

    public boolean isLiquidating() {
        return this.liquidating;
    }

    public void setLiquidating(boolean liquidating) {
        this.liquidating = liquidating;
    }

    public BigDecimal getMaintenanceMarginRequirement() {
        return this.maintenanceMarginRequirement;
    }

    public void setMaintenanceMarginRequirement(BigDecimal maintenanceMarginRequirement) {
        this.maintenanceMarginRequirement = maintenanceMarginRequirement;
    }

    public BigDecimal getMakerFee() {
        return this.makerFee;
    }

    public void setMakerFee(BigDecimal makerFee) {
        this.makerFee = makerFee;
    }

    public BigDecimal getMarginFraction() {
        return this.marginFraction;
    }

    public void setMarginFraction(BigDecimal marginFraction) {
        this.marginFraction = marginFraction;
    }

    public BigDecimal getOpenMarginFraction() {
        return this.openMarginFraction;
    }

    public void setOpenMarginFraction(BigDecimal openMarginFraction) {
        this.openMarginFraction = openMarginFraction;
    }

    public BigDecimal getTakerFee() {
        return this.takerFee;
    }

    public void setTakerFee(BigDecimal takerFee) {
        this.takerFee = takerFee;
    }

    public BigDecimal getTotalAccountValue() {
        return this.totalAccountValue;
    }

    public void setTotalAccountValue(BigDecimal totalAccountValue) {
        this.totalAccountValue = totalAccountValue;
    }

    public BigDecimal getTotalPositionSize() {
        return this.totalPositionSize;
    }

    public void setTotalPositionSize(BigDecimal totalPositionSize) {
        this.totalPositionSize = totalPositionSize;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Position> getPositions() {
        return this.positions;
    }

    public void setPositions(Position[] positions) {
        this.positions = Arrays.asList(positions);
    }

    public BigDecimal getTotalAccountNav() {
        return totalAccountNav;
    }

    public void setTotalAccountNav(BigDecimal totalAccountNav) {
        this.totalAccountNav = totalAccountNav;
    }
}


