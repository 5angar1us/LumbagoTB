package com.example.TradeBoot.api.domain.futures;

import com.example.TradeBoot.api.domain.StandardMarketEntity;

import java.math.BigDecimal;
import java.util.Date;

public class Future extends StandardMarketEntity {
    private BigDecimal volume;

    private BigDecimal imfFactor;

    private BigDecimal lowerBound;

    private BigDecimal upperBound;

    private BigDecimal openInterest;

    private BigDecimal openInterestUsd;

    private BigDecimal mark;

    private BigDecimal positionLimitWeight;

    private BigDecimal index;

    private BigDecimal marginPrice;

    private String description;

    private String underlyingDescription;

    private String expiryDescription;

    private String moveStart;

    private String group;

    private boolean expired;

    private boolean perpetual;

    private Date expiry;

    private boolean closeOnly;

    private BigDecimal imfWeight;

    public BigDecimal getVolume() {
        return this.volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getImfFactor() {
        return this.imfFactor;
    }

    public void setImfFactor(BigDecimal imfFactor) {
        this.imfFactor = imfFactor;
    }

    public BigDecimal getLowerBound() {
        return this.lowerBound;
    }

    public void setLowerBound(BigDecimal lowerBound) {
        this.lowerBound = lowerBound;
    }

    public BigDecimal getOpenInterest() {
        return this.openInterest;
    }

    public void setOpenInterest(BigDecimal openInterest) {
        this.openInterest = openInterest;
    }

    public BigDecimal getOpenInterestUsd() {
        return this.openInterestUsd;
    }

    public void setOpenInterestUsd(BigDecimal openInterestUsd) {
        this.openInterestUsd = openInterestUsd;
    }

    public BigDecimal getMark() {
        return this.mark;
    }

    public void setMark(BigDecimal mark) {
        this.mark = mark;
    }

    public BigDecimal getPositionLimitWeight() {
        return this.positionLimitWeight;
    }

    public void setPositionLimitWeight(BigDecimal positionLimitWeight) {
        this.positionLimitWeight = positionLimitWeight;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isExpired() {
        return this.expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isPerpetual() {
        return this.perpetual;
    }

    public void setPerpetual(boolean perpetual) {
        this.perpetual = perpetual;
    }

    public Date getExpiry() {
        return this.expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public BigDecimal getIndex() {
        return this.index;
    }

    public void setIndex(BigDecimal index) {
        this.index = index;
    }

    public BigDecimal getUpperBound() {
        return this.upperBound;
    }

    public void setUpperBound(BigDecimal upperBound) {
        this.upperBound = upperBound;
    }

    public String getUnderlyingDescription() {
        return this.underlyingDescription;
    }

    public void setUnderlyingDescription(String underlyingDescription) {
        this.underlyingDescription = underlyingDescription;
    }

    public String getExpiryDescription() {
        return this.expiryDescription;
    }

    public void setExpiryDescription(String expiryDescription) {
        this.expiryDescription = expiryDescription;
    }

    public String getMoveStart() {
        return this.moveStart;
    }

    public void setMoveStart(String moveStart) {
        this.moveStart = moveStart;
    }

    public BigDecimal getMarginPrice() {
        return this.marginPrice;
    }

    public void setMarginPrice(BigDecimal marginPrice) {
        this.marginPrice = marginPrice;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public BigDecimal getImfWeight() {
        return imfWeight;
    }

    public void setImfWeight(BigDecimal imfWeight) {
        this.imfWeight = imfWeight;
    }

    public boolean isCloseOnly() {
        return closeOnly;
    }

    public void setCloseOnly(boolean closeOnly) {
        this.closeOnly = closeOnly;
    }
}
