package com.example.TradeBoot.ui.models;

import com.example.TradeBoot.api.utils.IEnumStringValue;

import java.util.Locale;

public enum TradingStrategy {
    ALL("All"),
    LONG("Long"),
    SHORT("Short");

    public String getName() {
        return name;
    }
    private final String name;

    TradingStrategy(String name) {
        this.name = name;
    }

    public static TradingStrategy getByStringName(String textValue) {
        for (var value : values()) {
            if (value.getName().equals(textValue))
                return value;
        }
        return values()[0];
    }
}
