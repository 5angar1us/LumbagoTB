package com.example.TradeBoot.api.extentions.RequestExcpetions;

public class UnceckedIOException extends RuntimeException {

    public UnceckedIOException(String s, Exception e) {
        super(s, e);
    }

    public UnceckedIOException(String s) {
        super(s);
    }
}
