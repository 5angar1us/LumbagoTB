package com.example.TradeBoot.api.http;

import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.BadRequestByFtxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientWorkerWithDelay {

    static final Logger log =
            LoggerFactory.getLogger(HttpClientWorkerWithDelay.class);

    private IHttpClientWorker httpClientWorker;

    public HttpClientWorkerWithDelay(IHttpClientWorker httpClientWorker) {
        this.httpClientWorker = httpClientWorker;
    }


    public String createGetRequest(String uri) {
        return httpClientWorker.createGetRequest(uri);
    }


    public String createPostRequest(String uri, String body) throws BadRequestByFtxException {
        return httpClientWorker.createPostRequest(uri, body);
    }


    public boolean createDeleteRequest(String uri) throws BadRequestByFtxException {
        return httpClientWorker.createDeleteRequest(uri);
    }


    public boolean createDeleteRequest(String uri, String body) throws BadRequestByFtxException {
        return httpClientWorker.createDeleteRequest(uri, body);
    }
}
