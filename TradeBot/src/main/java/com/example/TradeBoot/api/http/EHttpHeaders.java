package com.example.TradeBoot.api.http;

import com.example.TradeBoot.api.utils.IEnumStringValue;

public enum EHttpHeaders implements IEnumStringValue {

    FTX_KEY("FTX-KEY"),
    FTX_SIGN("FTX-SIGN"),
    FTX_TS("FTX-TS"),
    FTX_SUBACCOUNT("FTX-SUBACCOUNT");

    private final String header;

    EHttpHeaders(String type) {
        this.header = type;
    }

    public static EHttpHeaders getByString(String status) {
        return IEnumStringValue.getByString(status, values());
    }

    public String getName() {
        return header;
    }

    @Override
    public String toString() {
        return header;
    }
}
