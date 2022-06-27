package com.example.TradeBoot.api.utils;

import java.util.Locale;

public interface IEnumStringValue {

    public String getName();

    public static <T extends IEnumStringValue> T getByString(String stringValue, T[] values) {
        for (T value : values) {
            if (value.getName().equals(stringValue.toLowerCase(Locale.ROOT)))
                return value;
        }
        return values[0];
    }
}
