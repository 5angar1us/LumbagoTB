package com.example.TradeBoot.trade.model;

import com.example.TradeBoot.BigDecimalUtils;
import com.example.TradeBoot.api.domain.markets.ESide;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderInformation {

    private BigDecimal volume;

    private ESide side;

    private Persent distanceInPercent;

    public OrderInformation(BigDecimal volume, ESide side, Persent distanceInPercent) {
        setVolume(volume);
        this.side = side;
        this.distanceInPercent = distanceInPercent;
    }

    public Persent getDistanceInPercent() {
        return distanceInPercent;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public ESide getSide() {
        return side;
    }

    private void setDistanceInPercent(Persent distanceInPercent) {
        this.distanceInPercent = distanceInPercent;
    }

    private void setVolume(BigDecimal volume) {
        if (BigDecimalUtils.check(volume, BigDecimalUtils.EOperator.LESS_THAN_OR_EQUALS , BigDecimal.ZERO))
            throw new IllegalArgumentException("volume");

        this.volume = volume;
    }

    @Override
    public String toString() {
        return "OrderInformation{" +
                "volume=" + volume +
                ", side=" + side +
                ", distanceInPercent=" + distanceInPercent +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderInformation that = (OrderInformation) o;

        return BigDecimalUtils.check(getVolume(), BigDecimalUtils.EOperator.EQUALS, that.getVolume())
                && getSide() == that.getSide()
                && getDistanceInPercent().equals(that.getDistanceInPercent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVolume(), getSide(), getDistanceInPercent());
    }
}
