package com.example.TradeBoot.api.utils;


import java.math.BigDecimal;

public class ModifyOrderBuilder extends AbstactParameterBuilder {

    public ModifyOrderBuilder() {
        super(2);
    }

    public ModifyOrderBuilder TargetSize(BigDecimal size) {
        addParameter("size", String.valueOf(size));
        return this;
    }

    public ModifyOrderBuilder TargetPrice(BigDecimal price) {
        addParameter("price", String.valueOf(price));
        return this;
    }
}
