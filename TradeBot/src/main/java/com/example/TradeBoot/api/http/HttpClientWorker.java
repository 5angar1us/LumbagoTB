package com.example.TradeBoot.api.http;

import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.BadRequestByFtxException;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.UnceckedIOException;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.UnknownErrorSendRequestException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientWorker implements IHttpClientWorker {
    private final HttpClient client;
    private final HttpRequestFactory httpRequestFactory;

    private final HttpResponseHandler httpResponseHandler;

    @Autowired
    public HttpClientWorker(HttpRequestFactory httpRequestFactory, HttpResponseHandler httpResponseHandler) {
        this.httpResponseHandler = httpResponseHandler;
        this.client = HttpClient.newHttpClient();
        this.httpRequestFactory = httpRequestFactory;
    }

    @Override
    public String createGetRequest(String uri) {
        HttpRequest request = httpRequestFactory.createGetRequest(uri);
        HttpResponse<String> response = sendRequest(request);
        return httpResponseHandler.handleGetResponse(response);
    }

    @Override
    public String createPostRequest(String uri, String body) throws BadRequestByFtxException {
        HttpRequest request = httpRequestFactory.createPostRequest(uri, body);
        HttpResponse<String> response = sendRequest(request);
        return httpResponseHandler.handlePostResponse(response);
    }
    
    @Override
    public boolean createDeleteRequest(String uri) throws BadRequestByFtxException {
        HttpRequest request = httpRequestFactory.createDeleteRequest(uri);
        HttpResponse<String> response = sendRequest(request);
        return httpResponseHandler.handleDeleteResponse(response);
    }

    @Override
    public boolean createDeleteRequest(String uri, String body) throws BadRequestByFtxException {
        HttpRequest request = httpRequestFactory.createDeleteRequest(uri, body);
        HttpResponse<String> response = sendRequest(request);
        return httpResponseHandler.handleDeleteResponse(response);
    }

    private HttpResponse<String> sendRequest(HttpRequest httpRequest) {
        try {
            return this.client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        }
        catch (IOException e){
            var message = httpRequest.method() + " request to '" + httpRequest.uri() + "' throws exception." +
                    " Error name " + e.getClass().getSimpleName() + ". Error message " + e.getMessage() + ".";
            throw new UnceckedIOException(message, e);
        }

        catch (InterruptedException e) {
            var message = httpRequest.method() + " request to '" + httpRequest.uri() + "' throws exception." +
                    " Error name " + e.getClass().getSimpleName() + ". Error message " + e.getMessage() + ".";
            throw new UnknownErrorSendRequestException(message, e);
        }
    }
}
