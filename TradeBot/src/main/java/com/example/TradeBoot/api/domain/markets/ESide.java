package com.example.TradeBoot.api.domain.markets;

import java.util.Locale;

public enum ESide {
    EMPTY("empty"),
    SELL("sell"),
    BUY("buy");

    private final String direction;

    ESide(String direction) {
        this.direction = direction;
    }

    public static ESide getByDirection(String direction) {
        for (ESide value : values()) {
            if (value.direction.equals(direction.toLowerCase(Locale.ROOT)))
                return value;
        }
        return ESide.EMPTY;
    }
}
