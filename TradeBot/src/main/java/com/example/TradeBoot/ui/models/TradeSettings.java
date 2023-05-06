package com.example.TradeBoot.ui.models;

import com.example.TradeBoot.ui.validation.BigDecimalRange;
import com.example.TradeBoot.ui.validation.CorrectMarketName;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TradeSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Market name is mandatory")
    @CorrectMarketName()
    private String marketName;

    @BigDecimalRange(minPrecision = 0, maxPrecision = 100, scale = 6)
    @Column(precision = 9, scale = 6, columnDefinition="numeric")
    private BigDecimal maximumDefinition = BigDecimal.ZERO;

    @DecimalMax(
            value = "100000",
            message = "The tradeDelay ${validatedValue} must be lover than {value}"
    )
    @DecimalMin(
            value = "0",
            message = "The tradeDelay ${validatedValue} must be higher than {value}"
    )
    private long tradeDelay;


    public List<TradeSettingsDetail> getTradeSettingsDetails() {
        return tradeSettingsDetails;
    }

    public void setTradeSettingsDetails(List<TradeSettingsDetail> tradeSettingsDetails) {
        this.tradeSettingsDetails = tradeSettingsDetails;
    }

    @OneToMany(mappedBy = "tradeSettings", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<TradeSettingsDetail> tradeSettingsDetails = new ArrayList<>();
    // standard constructors / setters / getters / toString

    public TradeSettings(String marketName, BigDecimal maximumDefinition, long tradeDelay) {
        this.marketName = marketName;
        this.maximumDefinition = maximumDefinition;
        this.tradeDelay = tradeDelay;
    }

    public TradeSettings() {
    }

    public BigDecimal getMaximumDefinition() {
        return maximumDefinition;
    }

    public void setMaximumDefinition(BigDecimal maximumDefinition) {
        this.maximumDefinition = maximumDefinition;
    }

    public long getTradeDelay() {
        return tradeDelay;
    }

    public void setTradeDelay(long tradeDelay) {
        this.tradeDelay = tradeDelay;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public int getDetailCount(){
        return getTradeSettingsDetails().size();
    }
    public void addAllDetail(List<TradeSettingsDetail> tradeSettingsDetails) {
        getTradeSettingsDetails().addAll(tradeSettingsDetails);
    }
}
