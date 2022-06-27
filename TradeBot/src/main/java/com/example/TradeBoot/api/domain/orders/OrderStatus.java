package com.example.TradeBoot.api.domain.orders;

import com.example.TradeBoot.api.domain.markets.ESide;

import java.math.BigDecimal;
import java.util.Date;

public class OrderStatus {

    private String id;

    private String market;

    private EType type;

    private ESide side;

    private BigDecimal price;

    private BigDecimal size;

    private BigDecimal filledSize;

    private BigDecimal remainingSize;

    private BigDecimal avgFillPrice;

    private EStatus status;

    private Date createdAt;

    private boolean reduceOnly;

    public void setFuture(String future) {
        this.future = future;
    }

    private String future;
    public boolean isLiquidation() {
        return liquidation;
    }

    public void setLiquidation(boolean liquidation) {
        this.liquidation = liquidation;
    }

    private boolean liquidation;
    private boolean ioc;

    private boolean postOnly;

    private String clientId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public EType getType() {
        return type;
    }

    public void setType(String type) {
        this.type = EType.getByString(type);
    }

    public ESide getSide() {
        return side;
    }

    public void setSide(String direction) {
        this.side = ESide.getByDirection(direction);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public BigDecimal getFilledSize() {
        return filledSize;
    }

    public void setFilledSize(BigDecimal filledSize) {
        this.filledSize = filledSize;
    }

    public BigDecimal getRemainingSize() {
        return remainingSize;
    }

    public void setRemainingSize(BigDecimal remainingSize) {
        this.remainingSize = remainingSize;
    }

    public BigDecimal getAvgFillPrice() {
        return avgFillPrice;
    }

    public void setAvgFillPrice(BigDecimal avgFillPrice) {
        this.avgFillPrice = avgFillPrice;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = EStatus.getByString(status);
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isReduceOnly() {
        return reduceOnly;
    }

    public void setReduceOnly(boolean reduceOnly) {
        this.reduceOnly = reduceOnly;
    }

    public boolean isIoc() {
        return ioc;
    }

    public void setIoc(boolean ioc) {
        this.ioc = ioc;
    }

    public boolean isPostOnly() {
        return postOnly;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
