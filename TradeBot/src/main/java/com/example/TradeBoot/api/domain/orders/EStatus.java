package com.example.TradeBoot.api.domain.orders;

import com.example.TradeBoot.api.utils.IEnumStringValue;

public enum EStatus implements IEnumStringValue {
    EMPTY("empty"),
    OPEN("open"),
    NEW("new"),
    CLOSED("closed");

    private final String status;

    EStatus(String type) {
        this.status = type;
    }

    public static EStatus getByString(String status) {
        return IEnumStringValue.getByString(status, values());
    }

    @Override
    public String getName() {
        return status;
    }
}
