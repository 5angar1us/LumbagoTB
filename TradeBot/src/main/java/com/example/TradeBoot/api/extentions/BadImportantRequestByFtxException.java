package com.example.TradeBoot.api.extentions;

public class BadImportantRequestByFtxException extends Exception {
    public BadImportantRequestByFtxException(String s, Exception e) {
        super(s, e);
    }

    public BadImportantRequestByFtxException(String s) {
        super(s);
    }
}
