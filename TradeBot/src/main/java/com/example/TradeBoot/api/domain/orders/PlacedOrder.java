package com.example.TradeBoot.api.domain.orders;

import com.example.TradeBoot.api.domain.markets.ESide;

import java.math.BigDecimal;
import java.util.Date;

public class PlacedOrder {

    public PlacedOrder(){}
    public PlacedOrder(
            String id,
            String clientId,
            String market,
            EType type,
            ESide side,
            BigDecimal price,
            BigDecimal size,
            EStatus status,
            BigDecimal filledSize,
            BigDecimal remainingSize,
            boolean reduceOnly,
            boolean liquidation,
            boolean avgFillPrice,
            boolean postOnly,
            boolean ioc,
            Date createdAt,
            String future) {
        this.id = id;
        this.clientId = clientId;
        this.market = market;
        this.type = type;
        this.side = side;
        this.price = price;
        this.size = size;
        this.status = status;
        this.filledSize = filledSize;
        this.remainingSize = remainingSize;
        this.reduceOnly = reduceOnly;
        this.liquidation = liquidation;
        this.avgFillPrice = avgFillPrice;
        this.postOnly = postOnly;
        this.ioc = ioc;
        this.createdAt = createdAt;
        this.future = future;
    }

    public PlacedOrder(PlacedOrder placedOrder){
        this.id = placedOrder.id;
        this.clientId = placedOrder.clientId;
        this.market = placedOrder.market;
        this.type = placedOrder.type;
        this.side = placedOrder.side;
        this.price = placedOrder.price;
        this.size = placedOrder.size;
        this.status = placedOrder.status;
        this.filledSize = placedOrder.filledSize;
        this.remainingSize = placedOrder.remainingSize;
        this.reduceOnly = placedOrder.reduceOnly;
        this.liquidation = placedOrder.liquidation;
        this.avgFillPrice = placedOrder.avgFillPrice;
        this.postOnly = placedOrder.postOnly;
        this.ioc = placedOrder.ioc;
        this.createdAt = placedOrder.createdAt;
        this.future = placedOrder.future;
    }

    private String id;

    private String clientId;

    private String market;

    private EType type;

    private ESide side;

    private BigDecimal price;

    private BigDecimal size;

    private EStatus status;

    private BigDecimal filledSize;

    private BigDecimal remainingSize;

    private boolean reduceOnly;

    private boolean liquidation;

    private boolean avgFillPrice;

    private boolean postOnly;

    private boolean ioc;

    private Date createdAt;

    private String future;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getMarket() {
        return this.market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public EType getType() {
        return this.type;
    }

    public void setType(EType type) {
        this.type = type;
    }

    public void setType(String type) {
        this.type = EType.getByString(type);
    }

    public ESide getSide() {
        return this.side;
    }

    public void setSide(ESide side) {
        this.side = side;
    }

    public void setSide(String side) {
        this.side = ESide.getByDirection(side);
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public void setStatus(EStatus status) {
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = EStatus.getByString(status);
    }

    public BigDecimal getFilledSize() {
        return this.filledSize;
    }

    public void setFilledSize(BigDecimal filledSize) {
        this.filledSize = filledSize;
    }

    public BigDecimal getRemainingSize() {
        return this.remainingSize;
    }

    public void setRemainingSize(BigDecimal remainingSize) {
        this.remainingSize = remainingSize;
    }

    public boolean isReduceOnly() {
        return this.reduceOnly;
    }

    public void setReduceOnly(boolean reduceOnly) {
        this.reduceOnly = reduceOnly;
    }

    public boolean isLiquidation() {
        return this.liquidation;
    }

    public void setLiquidation(boolean liquidation) {
        this.liquidation = liquidation;
    }

    public boolean isAvgFillPrice() {
        return this.avgFillPrice;
    }

    public void setAvgFillPrice(boolean avgFillPrice) {
        this.avgFillPrice = avgFillPrice;
    }

    public boolean isPostOnly() {
        return this.postOnly;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public boolean isIoc() {
        return this.ioc;
    }

    public void setIoc(boolean ioc) {
        this.ioc = ioc;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getFuture() {
        return this.future;
    }

    public void setFuture(String future) {
        this.future = future;
    }

    public String toString() {
        return "PlacedOrder{id='" + this.id + "', clientId='" + this.clientId + "', market='" + this.market + "', type=" + this.type + ", side=" + this.side + ", price=" + this.price + ", size=" + this.size + ", status=" + this.status + ", filledSize=" + this.filledSize + ", remainingSize=" + this.remainingSize + ", reduceOnly=" + this.reduceOnly + ", liquidation=" + this.liquidation + ", avgFillPrice=" + this.avgFillPrice + ", postOnly=" + this.postOnly + ", ioc=" + this.ioc + ", createdAt=" + this.createdAt + ", future='" + this.future + "'}";
    }
}
