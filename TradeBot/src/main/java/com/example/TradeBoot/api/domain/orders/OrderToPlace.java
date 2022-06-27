package com.example.TradeBoot.api.domain.orders;

import com.example.TradeBoot.api.domain.markets.ESide;

import java.math.BigDecimal;
import java.util.Locale;

public class OrderToPlace {
    @Override
    public String toString() {
        return "OrderToPlace{" +
                "market='" + market + '\'' +
                ", side=" + side +
                ", price=" + price +
                ", type=" + type +
                ", size=" + size +
                ", reduceOnly=" + reduceOnly +
                ", ioc=" + ioc +
                ", postOnly=" + postOnly +
                ", clientId='" + clientId + '\'' +
                '}';
    }

    private String market;

    private ESide side;

    private BigDecimal price;

    private EType type;

    private BigDecimal size;

    private boolean reduceOnly;

    private boolean ioc;

    private boolean postOnly;

    private String clientId;

    public OrderToPlace(String market, ESide side, BigDecimal price, EType type, BigDecimal size) {
        this.market = market;
        this.side = side;
        this.price = price;
        this.type = type;
        this.size = size;
    }

    public String getMarket() {
        return this.market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getSide() {
        return this.side.name().toLowerCase(Locale.ROOT);
    }

    public void setSide(String side) {
        this.side = ESide.getByDirection(side);
    }

    public void setSide(ESide side) {
        this.side = side;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getType() {
        return this.type.name().toLowerCase(Locale.ROOT);
    }

    public void setType(String type) {
        this.type = EType.getByString(type);
    }

    public void setType(EType type) {
        this.type = type;
    }

    public BigDecimal getSize() {
        return this.size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public boolean isReduceOnly() {
        return this.reduceOnly;
    }

    public void setReduceOnly(boolean reduceOnly) {
        this.reduceOnly = reduceOnly;
    }

    public boolean isIoc() {
        return this.ioc;
    }

    public void setIoc(boolean ioc) {
        this.ioc = ioc;
    }

    public boolean isPostOnly() {
        return this.postOnly;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
