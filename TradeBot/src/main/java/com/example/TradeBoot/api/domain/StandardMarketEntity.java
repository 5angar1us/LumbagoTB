package com.example.TradeBoot.api.domain;

import java.math.BigDecimal;

public abstract class StandardMarketEntity{
    private String name;

    private boolean enabled;

    private boolean postOnly;

    private BigDecimal priceIncrement;

    private BigDecimal sizeIncrement;

    private BigDecimal last;

    private BigDecimal bid;

    private BigDecimal ask;

    private String type;

    private String underlying;

    private BigDecimal change1h;

    private BigDecimal change24h;

    private BigDecimal changeBod;

    private BigDecimal volumeUsd24h;

    public StandardMarketEntity() {}

    public StandardMarketEntity(
            String name,
            boolean enabled,
            boolean postOnly,
            BigDecimal priceIncrement,
            BigDecimal sizeIncrement,
            BigDecimal last,
            BigDecimal bid,
            BigDecimal ask,
            String type,
            String underlying,
            BigDecimal change1h,
            BigDecimal change24h,
            BigDecimal changeBod,
            BigDecimal volumeUsd24h)
    {
        this.name = name;
        this.enabled = enabled;
        this.postOnly = postOnly;
        this.priceIncrement = priceIncrement;
        this.sizeIncrement = sizeIncrement;
        this.last = last;
        this.bid = bid;
        this.ask = ask;
        this.type = type;
        this.underlying = underlying;
        this.change1h = change1h;
        this.change24h = change24h;
        this.changeBod = changeBod;
        this.volumeUsd24h = volumeUsd24h;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isPostOnly() {
        return this.postOnly;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public BigDecimal getPriceIncrement() {
        return this.priceIncrement;
    }

    public void setPriceIncrement(BigDecimal priceIncrement) {
        this.priceIncrement = priceIncrement;
    }

    public BigDecimal getSizeIncrement() {
        return this.sizeIncrement;
    }

    public void setSizeIncrement(BigDecimal sizeIncrement) {
        this.sizeIncrement = sizeIncrement;
    }

    public BigDecimal getLast() {
        return this.last;
    }

    public void setLast(BigDecimal last) {
        this.last = last;
    }

    public BigDecimal getBid() {
        return this.bid;
    }

    public void setBid(BigDecimal bid) {
        this.bid = bid;
    }

    public BigDecimal getAsk() {
        return this.ask;
    }

    public void setAsk(BigDecimal ask) {
        this.ask = ask;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnderlying() {
        return this.underlying;
    }

    public void setUnderlying(String underlying) {
        this.underlying = underlying;
    }

    public BigDecimal getChange1h() {
        return this.change1h;
    }

    public void setChange1h(BigDecimal change1h) {
        this.change1h = change1h;
    }

    public BigDecimal getChange24h() {
        return this.change24h;
    }

    public void setChange24h(BigDecimal change24h) {
        this.change24h = change24h;
    }

    @Override
    public String toString() {
        return "StandardMarketEntity{" +
                "name='" + name + '\'' +
                ", enabled=" + enabled +
                ", postOnly=" + postOnly +
                ", priceIncrement=" + priceIncrement +
                ", sizeIncrement=" + sizeIncrement +
                ", last=" + last +
                ", bid=" + bid +
                ", ask=" + ask +
                ", type='" + type + '\'' +
                ", underlying='" + underlying + '\'' +
                ", change1h=" + change1h +
                ", change24h=" + change24h +
                ", changeBod=" + changeBod +
                ", volumeUsd24h=" + volumeUsd24h +
                '}';
    }

    public BigDecimal getChangeBod() {
        return this.changeBod;
    }

    public void setChangeBod(BigDecimal changeBod) {
        this.changeBod = changeBod;
    }

    public BigDecimal getVolumeUsd24h() {
        return this.volumeUsd24h;
    }

    public void setVolumeUsd24h(BigDecimal volumeUsd24h) {
        this.volumeUsd24h = volumeUsd24h;
    }
}
