package com.example.TradeBoot.api.domain.wallet;

import java.math.BigDecimal;

public class Balance {
    private String coin;

    private BigDecimal free;

    private BigDecimal spotBorrow;

    private BigDecimal total;

    private BigDecimal usdValue;

    private BigDecimal availableWithoutBorrow;

    public BigDecimal getAvailableForWithdrawal() {
        return availableForWithdrawal;
    }

    public void setAvailableForWithdrawal(BigDecimal availableForWithdrawal) {
        this.availableForWithdrawal = availableForWithdrawal;
    }

    private BigDecimal availableForWithdrawal;

    public String getCoin() {
        return this.coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public BigDecimal getFree() {
        return this.free;
    }

    public void setFree(BigDecimal free) {
        this.free = free;
    }

    public BigDecimal getSpotBorrow() {
        return this.spotBorrow;
    }

    public void setSpotBorrow(BigDecimal spotBorrow) {
        this.spotBorrow = spotBorrow;
    }

    public BigDecimal getTotal() {
        return this.total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getUsdValue() {
        return this.usdValue;
    }

    public void setUsdValue(BigDecimal usdValue) {
        this.usdValue = usdValue;
    }

    public BigDecimal getAvailableWithoutBorrow() {
        return this.availableWithoutBorrow;
    }

    public void setAvailableWithoutBorrow(BigDecimal availableWithoutBorrow) {
        this.availableWithoutBorrow = availableWithoutBorrow;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "coin='" + coin + '\'' +
                ", free=" + free +
                ", spotBorrow=" + spotBorrow +
                ", total=" + total +
                ", usdValue=" + usdValue +
                ", availableWithoutBorrow=" + availableWithoutBorrow +
                ", availableForWithdrawal=" + availableForWithdrawal +
                '}';
    }
}
