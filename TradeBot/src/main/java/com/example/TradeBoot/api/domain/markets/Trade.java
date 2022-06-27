package com.example.TradeBoot.api.domain.markets;

import java.math.BigDecimal;
import java.util.Date;

public class Trade {
    private long id;

    private boolean liquidation;

    private BigDecimal price;

    private ESide side;

    private BigDecimal size;

    private Date time;

    public Trade() {}

    public Trade(long id, boolean liquidation, BigDecimal price, String side, BigDecimal size, Date time) {
        this.id = id;
        this.liquidation = liquidation;
        this.price = price;
        this.side = ESide.getByDirection(side);
        this.size = size;
        this.time = time;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isLiquidation() {
        return this.liquidation;
    }

    public void setLiquidation(boolean liquidation) {
        this.liquidation = liquidation;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public Date getTime() {
        return this.time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}

