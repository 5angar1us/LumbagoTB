package com.example.TradeBoot.api.domain.account;

import com.example.TradeBoot.api.domain.markets.ESide;

import java.math.BigDecimal;

public class Position {
    private BigDecimal cost;

    private BigDecimal cumulativeBuySize;

    private BigDecimal cumulativeSellSize;

    private BigDecimal entryPrice;

    private BigDecimal estimatedLiquidationPrice;

    private String future;

    private BigDecimal initialMarginRequirement;

    private BigDecimal longOrderSize;

    private BigDecimal maintenanceMarginRequirement;

    private BigDecimal netSize;

    private BigDecimal openSize;

    private BigDecimal realizedPnl;

    private BigDecimal recentAverageOpenPrice;

    private BigDecimal recentBreakEvenPrice;

    private BigDecimal recentPnl;

    private BigDecimal shortOrderSize;

    private ESide side;

    private BigDecimal size;

    private BigDecimal unrealizedPnl;

    private BigDecimal collateralUsed;

    public BigDecimal getCost() {
        return this.cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getCumulativeBuySize() {
        return this.cumulativeBuySize;
    }

    public void setCumulativeBuySize(BigDecimal cumulativeBuySize) {
        this.cumulativeBuySize = cumulativeBuySize;
    }

    public BigDecimal getCumulativeSellSize() {
        return this.cumulativeSellSize;
    }

    public void setCumulativeSellSize(BigDecimal cumulativeSellSize) {
        this.cumulativeSellSize = cumulativeSellSize;
    }

    public BigDecimal getEntryPrice() {
        return this.entryPrice;
    }

    public void setEntryPrice(BigDecimal entryPrice) {
        this.entryPrice = entryPrice;
    }

    public BigDecimal getEstimatedLiquidationPrice() {
        return this.estimatedLiquidationPrice;
    }

    public void setEstimatedLiquidationPrice(BigDecimal estimatedLiquidationPrice) {
        this.estimatedLiquidationPrice = estimatedLiquidationPrice;
    }

    public String getFuture() {
        return this.future;
    }

    public void setFuture(String future) {
        this.future = future;
    }

    public BigDecimal getInitialMarginRequirement() {
        return this.initialMarginRequirement;
    }

    public void setInitialMarginRequirement(BigDecimal initialMarginRequirement) {
        this.initialMarginRequirement = initialMarginRequirement;
    }

    public BigDecimal getLongOrderSize() {
        return this.longOrderSize;
    }

    public void setLongOrderSize(BigDecimal longOrderSize) {
        this.longOrderSize = longOrderSize;
    }

    public BigDecimal getMaintenanceMarginRequirement() {
        return this.maintenanceMarginRequirement;
    }

    public void setMaintenanceMarginRequirement(BigDecimal maintenanceMarginRequirement) {
        this.maintenanceMarginRequirement = maintenanceMarginRequirement;
    }

    public BigDecimal getNetSize() {
        return this.netSize;
    }

    public void setNetSize(BigDecimal netSize) {
        this.netSize = netSize;
    }

    public BigDecimal getOpenSize() {
        return this.openSize;
    }

    public void setOpenSize(BigDecimal openSize) {
        this.openSize = openSize;
    }

    public BigDecimal getRealizedPnl() {
        return this.realizedPnl;
    }

    public void setRealizedPnl(BigDecimal realizedPnl) {
        this.realizedPnl = realizedPnl;
    }

    public BigDecimal getRecentAverageOpenPrice() {
        return this.recentAverageOpenPrice;
    }

    public void setRecentAverageOpenPrice(BigDecimal recentAverageOpenPrice) {
        this.recentAverageOpenPrice = recentAverageOpenPrice;
    }

    public BigDecimal getRecentBreakEvenPrice() {
        return this.recentBreakEvenPrice;
    }

    public void setRecentBreakEvenPrice(BigDecimal recentBreakEvenPrice) {
        this.recentBreakEvenPrice = recentBreakEvenPrice;
    }

    public BigDecimal getRecentPnl() {
        return this.recentPnl;
    }

    public void setRecentPnl(BigDecimal recentPnl) {
        this.recentPnl = recentPnl;
    }

    public BigDecimal getShortOrderSize() {
        return this.shortOrderSize;
    }

    public void setShortOrderSize(BigDecimal shortOrderSize) {
        this.shortOrderSize = shortOrderSize;
    }

    public ESide getSide() {
        return this.side;
    }

    public void setSide(String side) {
        this.side = ESide.getByDirection(side);
    }

    public BigDecimal getSize() {
        return this.size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public BigDecimal getUnrealizedPnl() {
        return this.unrealizedPnl;
    }

    public void setUnrealizedPnl(BigDecimal unrealizedPnl) {
        this.unrealizedPnl = unrealizedPnl;
    }

    public BigDecimal getCollateralUsed() {
        return this.collateralUsed;
    }

    public void setCollateralUsed(BigDecimal collateralUsed) {
        this.collateralUsed = collateralUsed;
    }
}
