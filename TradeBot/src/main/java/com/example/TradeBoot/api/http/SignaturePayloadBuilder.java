package com.example.TradeBoot.api.http;

import java.net.URI;

public class SignaturePayloadBuilder {
    private String httpMethodName;
    private URI uri;
    private String body;

    public SignaturePayloadBuilder(String httpMethodName, URI uri, String body) {
        this.httpMethodName = httpMethodName;
        this.uri = uri;
        this.body = body;
    }

    public String CreateSignaturePayload(long time) {
        return new StringBuilder()
                .append(time)
                .append(httpMethodName)
                .append(uri)
                .append(body)
                .toString();
    }
}
