package com.example.TradeBoot.api.domain.orders;

import com.example.TradeBoot.api.domain.markets.ESide;

import java.math.BigDecimal;
import java.util.Date;

public class OpenOrder {
    private Date createdAt;

    private BigDecimal filledSize;

    private String future;

    private String id;

    private String market;

    private BigDecimal price;

    private BigDecimal avgFillPrice;

    private BigDecimal remainingSize;

    private ESide side;

    private BigDecimal size;

    private EStatus status;

    private String type;

    private boolean reduceOnly;

    @Override
    public String toString() {
        return "OpenOrder{" +
                "createdAt=" + createdAt +
                ", filledSize=" + filledSize +
                ", future='" + future + '\'' +
                ", id='" + id + '\'' +
                ", market='" + market + '\'' +
                ", price=" + price +
                ", avgFillPrice=" + avgFillPrice +
                ", remainingSize=" + remainingSize +
                ", side=" + side +
                ", size=" + size +
                ", status=" + status +
                ", type='" + type + '\'' +
                ", reduceOnly=" + reduceOnly +
                ", liquidation=" + liquidation +
                ", ioc=" + ioc +
                ", postOnly=" + postOnly +
                ", clientId='" + clientId + '\'' +
                '}';
    }

    private boolean liquidation;

    private boolean ioc;

    private boolean postOnly;

    private String clientId;

    public boolean isLiquidation() { return liquidation; }

    public void setLiquidation(boolean liquidation) { this.liquidation = liquidation; }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getFilledSize() {
        return this.filledSize;
    }

    public void setFilledSize(BigDecimal filledSize) {
        this.filledSize = filledSize;
    }

    public String getFuture() {
        return this.future;
    }

    public void setFuture(String future) {
        this.future = future;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarket() {
        return this.market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAvgFillPrice() {
        return this.avgFillPrice;
    }

    public void setAvgFillPrice(BigDecimal avgFillPrice) {
        this.avgFillPrice = avgFillPrice;
    }

    public BigDecimal getRemainingSize() {
        return this.remainingSize;
    }

    public void setRemainingSize(BigDecimal remainingSize) {
        this.remainingSize = remainingSize;
    }

    public void setSide(String side) {
        this.side = ESide.getByDirection(side);
    }

    public ESide getSide() {
        return this.side;
    }

    public void setSide(ESide side) {
        this.side = side;
    }

    public BigDecimal getSize() {
        return this.size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public EStatus getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = EStatus.getByString(status);
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
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