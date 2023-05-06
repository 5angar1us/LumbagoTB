package com.example.TradeBoot.api.utils;


import java.math.BigDecimal;

public class ModifyOrderBuilder extends AbstractParameterBuilder {

    public ModifyOrderBuilder() {
        super(2);
    }

    public ModifyOrderBuilder TargetSize(BigDecimal size) {
        addParameter("size", size);
        return this;
    }

    public ModifyOrderBuilder TargetPrice(BigDecimal price) {
        addParameter("price", price);
        return this;
    }
}
