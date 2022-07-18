package com.example.TradeBoot.api.http;

import com.example.TradeBoot.api.extentions.RequestExcpetions.Checked.BadRequestByFtxException;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Checked.SendRequestException;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.UnknownErrorSendRequestException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientWorker {
    private final HttpClient client;
    private final HttpRequestFactory httpRequestFactory;

    private final HttpResponseHandler httpResponseHandler;

    @Autowired
    public HttpClientWorker(HttpRequestFactory httpRequestFactory, HttpResponseHandler httpResponseHandler) {
        this.httpResponseHandler = httpResponseHandler;
        this.client = HttpClient.newHttpClient();
        this.httpRequestFactory = httpRequestFactory;
    }

    public String createGetRequest(String uri) {
        HttpRequest request = httpRequestFactory.createGetRequest(uri);
        HttpResponse<String> response = sendGetRequest(request);
        return httpResponseHandler.handleGetResponse(response);
    }

    public String createPostRequest(String uri, String body) throws BadRequestByFtxException {
        HttpRequest request = httpRequestFactory.createPostRequest(uri, body);
        HttpResponse<String> response = sendChangeRequest(request);
        return httpResponseHandler.handlePostResponse(response);
    }


    public boolean createDeleteRequest(String uri) throws BadRequestByFtxException {
        HttpRequest request = httpRequestFactory.createDeleteRequest(uri);
        HttpResponse<String> response = sendChangeRequest(request);
        return  httpResponseHandler.handleDeleteResponse(response);
    }

    public boolean createDelete(String uri, String body) throws BadRequestByFtxException {
        HttpRequest request = httpRequestFactory.createDeleteRequest(uri, body);
        HttpResponse<String> response = sendChangeRequest(request);
        return  httpResponseHandler.handleDeleteResponse(response);
    }

    private HttpResponse<String> sendChangeRequest(HttpRequest httpRequest) throws SendRequestException {
        try {
            return defaultSend(httpRequest);

        } catch (IOException | InterruptedException e) {
            throw new UnknownErrorSendRequestException(httpRequest.method() + " request to '" + httpRequest.uri() + "' throws exception.", e);
        }
    }
    private HttpResponse<String> sendGetRequest(HttpRequest httpRequest) {
        try {
            return defaultSend(httpRequest);

        } catch (IOException | InterruptedException e) {
            throw new UnknownErrorSendRequestException(httpRequest.method() + " request to '" + httpRequest.uri() + "' throws exception.", e);
        }
    }
    private HttpResponse<String> defaultSend(HttpRequest httpRequest) throws IOException, InterruptedException {
        return this.client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }



}
