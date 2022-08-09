package com.example.TradeBoot.api.http;

import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpResponse;

public class HttpResponseHandler {

    static final Logger log =
            LoggerFactory.getLogger(HttpResponseHandler.class);
    HttpFTXResponseParser ftxResponseParser = new HttpFTXResponseParser();
    public String handleGetResponse(HttpResponse<String> response){
        var responseData = ftxResponseParser.getResponseData(response);
       
        if (responseData.isSuccess() == false){
            handleGetException(response, responseData);
        }

        return responseData.result().get();
    }

    public boolean handleDeleteResponse(HttpResponse<String> response) throws BadRequestByFtxException {
        var responseData = ftxResponseParser.getResponseData(response);


        if (responseData.isSuccess() == false){
            handleChangeRequestException(response, responseData);
        }

        return responseData.isSuccess();
    }

    public String handlePostResponse(HttpResponse<String> response) throws BadRequestByFtxException {
        var responseData = ftxResponseParser.getResponseData(response);

        if (responseData.isSuccess() == false){
            handleChangeRequestException(response, responseData);
        }

        return responseData.result().get();
    }
    
    public void handleGetException(HttpResponse<String> response, FTXResponceData responseData){
        var methodName = response.request().method();
        var statusCode = response.statusCode();

        var message = String.format("%s request. Status %d. Error: %s.",
                methodName,
                Integer.valueOf(statusCode),
                responseData.error().get()
        );

        log.error(message);

        switch (responseData.error().get()){
            case "Please retry request" -> throw new RetryRequestException();
            default -> throw new UnknownErrorRequestByFtxException(message);
        }

    }
    
    public void handleChangeRequestException(HttpResponse<String> response, FTXResponceData responseData){
        var methodName = response.request().method();
        var statusCode = response.statusCode();

        var errorMessage = String.format("%s request. Status %d. Error: %s.",
                methodName,
                Integer.valueOf(statusCode),
                responseData.error().get()
        );

        log.error(errorMessage);

        var apiErrorMessage = responseData.error().get();
        switch (apiErrorMessage){
            case "Order already closed" -> throw new OrderAlreadyClosedException();
            default -> defaultHandleRequestException(apiErrorMessage, errorMessage);
        }


    }

    public void defaultHandleRequestException(String apiErrorMessage, String errorMessage){
        switch (apiErrorMessage){
            case "Not logged in: Invalid signature" -> throw new InvalidSignatureException(errorMessage);
            default -> throw new UnknownErrorRequestByFtxException(errorMessage);
        }
    }


}
