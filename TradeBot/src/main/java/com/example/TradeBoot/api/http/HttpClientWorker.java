package com.example.TradeBoot.api.http;

import com.example.TradeBoot.api.extentions.RequestExcpetions.BadRequestByFtxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientWorker implements IHttpClientWorker {

    static final Logger log =
            LoggerFactory.getLogger(HttpClientWorker.class);
    private final HttpClient client;
    private final HttpRequestFactory httpRequestFactory;

    private final HttpSendErrorHandler httpSendErrorHandler;

    private final HttpResponseHandler httpResponseHandler;

    @Autowired
    public HttpClientWorker(HttpRequestFactory httpRequestFactory, HttpSendErrorHandler httpSendErrorHandler, HttpResponseHandler httpResponseHandler) {
        this.httpSendErrorHandler = httpSendErrorHandler;
        this.httpResponseHandler = httpResponseHandler;
        this.client = HttpClient.newHttpClient();
        this.httpRequestFactory = httpRequestFactory;
    }

    @Override
    public String createGetRequest(String uri) {
        HttpRequest request = httpRequestFactory.createGetRequest(uri);
        HttpResponse<String> response = httpSendErrorHandler.sendResponseOrHandleError(client,request);
        return httpResponseHandler.handleGetResponse(response);
    }

    @Override
    public String createPostRequest(String uri, String body) throws BadRequestByFtxException {
        log.debug("Create post request");
        HttpRequest request = httpRequestFactory.createPostRequest(uri, body);
        HttpResponse<String> response = httpSendErrorHandler.sendResponseOrHandleError(client,request);
        return httpResponseHandler.handlePostResponse(response);
    }

    @Override
    public boolean createDeleteRequest(String uri) throws BadRequestByFtxException {
        HttpRequest request = httpRequestFactory.createDeleteRequest(uri);
        HttpResponse<String> response = httpSendErrorHandler.sendResponseOrHandleError(client,request);
        return httpResponseHandler.handleDeleteResponse(response);
    }

    @Override
    public boolean createDeleteRequest(String uri, String body) throws BadRequestByFtxException {
        HttpRequest request = httpRequestFactory.createDeleteRequest(uri, body);
        HttpResponse<String> response = httpSendErrorHandler.sendResponseOrHandleError(client,request);
        return httpResponseHandler.handleDeleteResponse(response);
    }

}
