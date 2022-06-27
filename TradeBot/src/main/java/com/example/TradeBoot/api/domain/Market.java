package com.example.TradeBoot.api.domain;

import java.math.BigDecimal;

public class Market extends StandardMarketEntity {

    public BigDecimal getPriceHigh24h() { return priceHigh24h; }

    public void setPriceHigh24h(BigDecimal priceHigh24h) { this.priceHigh24h = priceHigh24h; }

    public BigDecimal getPriceLow24h() { return priceLow24h; }

    public void setPriceLow24h(BigDecimal priceLow24h) { this.priceLow24h = priceLow24h; }

    public boolean getIsEtfMarket() { return isEtfMarket; }

    public void setEtfMarket(boolean etfMarket) { isEtfMarket = etfMarket; }

    public BigDecimal getMinProvideSize() {
        return this.minProvideSize;
    }

    public void setMinProvideSize(BigDecimal minProvideSize) {
        this.minProvideSize = minProvideSize;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getBaseCurrency() {
        return this.baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getQuoteCurrency() {
        return this.quoteCurrency;
    }

    public void setQuoteCurrency(String quoteCurrency) {
        this.quoteCurrency = quoteCurrency;
    }

    public boolean isRestricted() {
        return this.restricted;
    }

    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

    public boolean isHighLeverageFeeExempt() {
        return this.highLeverageFeeExempt;
    }

    public void setHighLeverageFeeExempt(boolean highLeverageFeeExempt) {
        this.highLeverageFeeExempt = highLeverageFeeExempt;
    }

    public BigDecimal getQuoteVolume24h() {
        return this.quoteVolume24h;
    }

    public void setQuoteVolume24h(BigDecimal quoteVolume24h) {
        this.quoteVolume24h = quoteVolume24h;
    }

    public boolean isTokenizedEquity() {
        return this.tokenizedEquity;
    }

    public void setTokenizedEquity(boolean tokenizedEquity) {
        this.tokenizedEquity = tokenizedEquity;
    }

    public BigDecimal getLargeOrderThreshold() {
        return this.largeOrderThreshold;
    }

    public void setLargeOrderThreshold(BigDecimal largeOrderThreshold) {
        this.largeOrderThreshold = largeOrderThreshold;
    }

    public String getFutureType() {
        return futureType;
    }

    public void setFutureType(String futureType) {
        this.futureType = futureType;
    }

    public boolean isEtfMarket() {
        return isEtfMarket;
    }

    private BigDecimal minProvideSize;

    private BigDecimal price;

    private String baseCurrency; // maybe null

    private String quoteCurrency; // maybe null

    private boolean restricted;

    private boolean highLeverageFeeExempt;

    private BigDecimal quoteVolume24h;

    private boolean tokenizedEquity;
    @Override
    public String toString() {
        return "Market{" +
                "minProvideSize=" + minProvideSize +
                ", price=" + price +
                ", baseCurrency='" + baseCurrency + '\'' +
                ", quoteCurrency='" + quoteCurrency + '\'' +
                ", restricted=" + restricted +
                ", highLeverageFeeExempt=" + highLeverageFeeExempt +
                ", quoteVolume24h=" + quoteVolume24h +
                ", tokenizedEquity=" + tokenizedEquity +
                ", largeOrderThreshold=" + largeOrderThreshold +
                ", isEtfMarket=" + isEtfMarket +
                ", priceHigh24h=" + priceHigh24h +
                ", priceLow24h=" + priceLow24h +
                "} " + super.toString();
    }

    private BigDecimal largeOrderThreshold;

    private boolean isEtfMarket;

    private BigDecimal priceHigh24h;

    private BigDecimal priceLow24h;


    private String futureType;
}