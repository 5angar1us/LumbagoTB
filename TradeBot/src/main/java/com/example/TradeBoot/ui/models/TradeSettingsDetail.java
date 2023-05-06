package com.example.TradeBoot.ui.models;

import com.example.TradeBoot.ui.validation.BigDecimalRange;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Entity
public class TradeSettingsDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private TradingStrategy tradingStrategy = TradingStrategy.ALL;

    @DecimalMax(
            value = "2147483647",
            message = "The volume {validatedValue} must be lover than {value}"
    )
    @DecimalMin(
            value = "0",
            message = "The volume {validatedValue} must be higher than {value}"
    )
    private int volume;

    @BigDecimalRange(minPrecision = 0, maxPrecision = 100, scale = 6)
    @Column(precision = 9, scale = 6, columnDefinition="numeric")
    private BigDecimal priceOffset;

    @ManyToOne
    @JoinColumn(name = "trades_settings_id")
    private TradeSettings tradeSettings;

    public TradeSettingsDetail() {
    }

    public TradeSettingsDetail(TradingStrategy tradingStrategy, int volume, BigDecimal offset) {
        this.id = -1;
        this.tradingStrategy = tradingStrategy;
        this.volume = volume;
        this.priceOffset = offset;
    }

    public TradeSettingsDetail(int id, TradingStrategy tradingStrategy, int volume, BigDecimal priceOffset, TradeSettings tradeSettings) {
        this.id = id;
        this.tradingStrategy = tradingStrategy;
        this.volume = volume;
        this.priceOffset = priceOffset;
        this.tradeSettings = tradeSettings;
    }

    public TradeSettingsDetail(TradingStrategy tradingStrategy, int volume, BigDecimal offset, TradeSettings tradeSettings) {
        this.id = -1;
        this.tradingStrategy = tradingStrategy;
        this.volume = volume;
        this.priceOffset = offset;
        this.tradeSettings = tradeSettings;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public BigDecimal getPriceOffset() {
        return priceOffset;
    }

    public void setPriceOffset(BigDecimal priceOffset) {
        this.priceOffset = priceOffset;
    }

    public TradeSettings getTradeSettings() {
        return tradeSettings;
    }

    public void setTradeSettings(TradeSettings tradeSettings) {
        this.tradeSettings = tradeSettings;
    }

    public TradingStrategy getTradingStrategy() {
        return tradingStrategy;
    }

    public void setTradingStrategy(TradingStrategy tradingStrategy) {
        this.tradingStrategy = tradingStrategy;
    }

    @Override
    public String toString() {
        return "TradeSettingsDetail{" +
                "id=" + id +
                ", tradingStrategy=" + tradingStrategy +
                ", volume=" + volume +
                ", priceOffset=" + priceOffset +
                ", tradeSettings=" + tradeSettings +
                '}';
    }
}
