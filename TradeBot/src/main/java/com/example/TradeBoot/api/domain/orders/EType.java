package com.example.TradeBoot.api.domain.orders;

import com.example.TradeBoot.api.utils.IEnumStringValue;

public enum EType implements IEnumStringValue{
    EMPTY("empty"),
    LIMIT("limit"),
    MARKET("market");

    private final String type;

    EType(String type) {
        this.type = type;
    }

    public static EType getByString(String status) {
        return IEnumStringValue.getByString(status, values());
    }

    @Override
    public String getName() {
        return type;
    }
}
