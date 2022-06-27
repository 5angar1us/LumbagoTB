package com.example.TradeBoot.api.domain.wallet;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class Coin {
    private String bep2Asset;

    private boolean canConvert;

    private boolean canDeposit;

    private boolean canWithdraw;

    private boolean collateral;

    private boolean isEtf;

    private boolean hidden;

    private BigDecimal collateralWeight;

    public BigDecimal getImfWeight() {
        return imfWeight;
    }

    public void setImfWeight(BigDecimal imfWeight) {
        this.imfWeight = imfWeight;
    }

    private BigDecimal imfWeight;

    private BigDecimal indexPrice;

    private String creditTo;

    private String erc20Contract;

    private boolean fiat;

    private boolean hasTag;

    private String id;

    private boolean isToken;

    private List<String> methods;

    private String name;

    private String splMint;

    private String trc20Contract;

    private boolean usdFungible;

    private boolean spotMargin;

    private boolean tokenizedEquity;

    private boolean nftQuoteCurrencyEligible;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String imageUrl;

    public String getBep2Asset() {
        return this.bep2Asset;
    }

    public void setBep2Asset(String bep2Asset) {
        this.bep2Asset = bep2Asset;
    }

    public boolean isCanConvert() {
        return this.canConvert;
    }

    public void setCanConvert(boolean canConvert) {
        this.canConvert = canConvert;
    }

    public boolean isCanDeposit() {
        return this.canDeposit;
    }

    public void setCanDeposit(boolean canDeposit) {
        this.canDeposit = canDeposit;
    }

    public boolean isCanWithdraw() {
        return this.canWithdraw;
    }

    public void setCanWithdraw(boolean canWithdraw) {
        this.canWithdraw = canWithdraw;
    }

    public boolean isCollateral() {
        return this.collateral;
    }

    public void setCollateral(boolean collateral) {
        this.collateral = collateral;
    }

    public BigDecimal getCollateralWeight() {
        return this.collateralWeight;
    }

    public void setCollateralWeight(BigDecimal collateralWeight) {
        this.collateralWeight = collateralWeight;
    }

    public String getCreditTo() {
        return this.creditTo;
    }

    public void setCreditTo(String creditTo) {
        this.creditTo = creditTo;
    }

    public String getErc20Contract() {
        return this.erc20Contract;
    }

    public void setErc20Contract(String erc20Contract) {
        this.erc20Contract = erc20Contract;
    }

    public boolean isFiat() {
        return this.fiat;
    }

    public void setFiat(boolean fiat) {
        this.fiat = fiat;
    }

    public boolean isHasTag() {
        return this.hasTag;
    }

    public void setHasTag(boolean hasTag) {
        this.hasTag = hasTag;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isToken() {
        return this.isToken;
    }

    public void setIsToken(boolean token) {
        this.isToken = token;
    }

    public List<String> getMethods() {
        return this.methods;
    }

    public void setMethods(String[] methods) {
        this.methods = Arrays.asList(methods);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSplMint() {
        return this.splMint;
    }

    public void setSplMint(String splMint) {
        this.splMint = splMint;
    }

    public String getTrc20Contract() {
        return this.trc20Contract;
    }

    public void setTrc20Contract(String trc20Contract) {
        this.trc20Contract = trc20Contract;
    }

    public boolean isUsdFungible() {
        return this.usdFungible;
    }

    public void setUsdFungible(boolean usdFungible) {
        this.usdFungible = usdFungible;
    }

    public boolean isEtf() {
        return this.isEtf;
    }

    public void setIsEtf(boolean etf) {
        this.isEtf = etf;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isSpotMargin() {
        return this.spotMargin;
    }

    public void setSpotMargin(boolean spotMargin) {
        this.spotMargin = spotMargin;
    }

    public boolean isNftQuoteCurrencyEligible() {
        return this.nftQuoteCurrencyEligible;
    }

    public void setNftQuoteCurrencyEligible(boolean nftQuoteCurrencyEligible) {
        this.nftQuoteCurrencyEligible = nftQuoteCurrencyEligible;
    }

    public BigDecimal getIndexPrice() {
        return this.indexPrice;
    }

    public void setIndexPrice(BigDecimal indexPrice) {
        this.indexPrice = indexPrice;
    }

    public boolean isTokenizedEquity() {
        return this.tokenizedEquity;
    }

    public void setTokenizedEquity(boolean tokenizedEquity) {
        this.tokenizedEquity = tokenizedEquity;
    }

    @Override
    public String toString() {
        return "Coin{" +
                "bep2Asset='" + bep2Asset + '\'' +
                ", canConvert=" + canConvert +
                ", canDeposit=" + canDeposit +
                ", canWithdraw=" + canWithdraw +
                ", collateral=" + collateral +
                ", isEtf=" + isEtf +
                ", hidden=" + hidden +
                ", collateralWeight=" + collateralWeight +
                ", imfWeight=" + imfWeight +
                ", indexPrice=" + indexPrice +
                ", creditTo='" + creditTo + '\'' +
                ", erc20Contract='" + erc20Contract + '\'' +
                ", fiat=" + fiat +
                ", hasTag=" + hasTag +
                ", id='" + id + '\'' +
                ", isToken=" + isToken +
                ", methods=" + methods +
                ", name='" + name + '\'' +
                ", splMint='" + splMint + '\'' +
                ", trc20Contract='" + trc20Contract + '\'' +
                ", usdFungible=" + usdFungible +
                ", spotMargin=" + spotMargin +
                ", tokenizedEquity=" + tokenizedEquity +
                ", nftQuoteCurrencyEligible=" + nftQuoteCurrencyEligible +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}