package com.example.TradeBoot.api.http;

import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.*;

import java.net.http.HttpResponse;

public class HttpResponseHandler {

    public HttpResponseHandler(HttpFTXResponseParser ftxResponseParser, HttpResponseErrorHandler responseErrorHandler) {
        this.ftxResponseParser = ftxResponseParser;
        this.responseErrorHandler = responseErrorHandler;
    }

    HttpFTXResponseParser ftxResponseParser;

    HttpResponseErrorHandler responseErrorHandler;

    public boolean handleDeleteResponse(HttpResponse<String> response) throws BadRequestByFtxException {
        var responseData = ftxResponseParser.getResponseData(response);

        if (responseData.isSuccess() == false){
            responseErrorHandler.handleRequestException(response, responseData);
        }

        return responseData.isSuccess();
    }

    public String handleGetResponse(HttpResponse<String> response){
        return handleResponse(response);
    }

    public String handlePostResponse(HttpResponse<String> response) throws BadRequestByFtxException {
       return handleResponse(response);
    }

    public String handleResponse(HttpResponse<String> response) throws BadRequestByFtxException {
        var responseData = ftxResponseParser.getResponseData(response);

        if (responseData.isSuccess() == false){
            responseErrorHandler.handleRequestException(response, responseData);
        }

        return responseData.result().get();
    }


}
