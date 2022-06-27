package com.example.TradeBoot.api.http;

import com.example.TradeBoot.api.extentions.BadImportantRequestByFtxException;
import com.example.TradeBoot.api.extentions.BadRequestByFtxException;
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
        HttpResponse<String> response = sendDefaultRequest(request);
        return httpResponseHandler.handleGetResponse(response);
    }

    public String createPostRequest(String uri, String body) throws BadImportantRequestByFtxException {
        HttpRequest request = httpRequestFactory.createPostRequest(uri, body);
        HttpResponse<String> response = sendDefaultRequest(request);
        return httpResponseHandler.handlePostResponse(response);
    }


    public boolean createDeleteRequest(String uri) throws BadImportantRequestByFtxException {
        HttpRequest request = httpRequestFactory.createDeleteRequest(uri);
        HttpResponse<String> response = sendDeleteRequest(request);
        return  httpResponseHandler.handleDeleteResponse(response);
    }

    public boolean createDelete(String uri, String body) throws BadImportantRequestByFtxException {
        HttpRequest request = httpRequestFactory.createDeleteRequest(uri, body);
        HttpResponse<String> response = sendDeleteRequest(request);
        return  httpResponseHandler.handleDeleteResponse(response);
    }

    private HttpResponse<String> sendDeleteRequest(HttpRequest httpRequest) throws BadImportantRequestByFtxException {
        try {
            return defaultSend(httpRequest);

        } catch (IOException | InterruptedException e) {
            throw new BadImportantRequestByFtxException("Delete request to '" + httpRequest.uri() + "' throws exception.", e);
        }
    }
    private HttpResponse<String> sendDefaultRequest(HttpRequest httpRequest){
        try {
            return defaultSend(httpRequest);

        } catch (IOException | InterruptedException e) {
            throw new BadRequestByFtxException(httpRequest.method() + " request to '" + httpRequest.uri() + "' throws exception.", e);
        }
    }
    private HttpResponse<String> defaultSend(HttpRequest httpRequest) throws IOException, InterruptedException {
        return this.client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }



}
