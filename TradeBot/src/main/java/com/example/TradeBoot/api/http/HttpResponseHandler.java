package com.example.TradeBoot.api.http;

import com.example.TradeBoot.api.extentions.ParseToModelException;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.*;
import uk.org.lidalia.slf4jext.Logger;
import uk.org.lidalia.slf4jext.LoggerFactory;

import java.net.http.HttpResponse;

@SuppressWarnings("ALL")
public class HttpResponseHandler {
    static final Logger logger =
            LoggerFactory.getLogger(HttpResponseHandler.class);


    public HttpResponseHandler(HttpFTXResponseParser ftxResponseParser, HttpResponseErrorHandler responseErrorHandler) {
        this.ftxResponseParser = ftxResponseParser;
        this.responseErrorHandler = responseErrorHandler;
    }

    HttpFTXResponseParser ftxResponseParser;

    HttpResponseErrorHandler responseErrorHandler;

    public boolean handleDeleteResponse(HttpResponse<String> response) throws BadRequestByFtxException {
        return  defaultHandleResponse(response).isSuccess();
    }

    public String handleGetResponse(HttpResponse<String> response){
        return handleResponse(response);
    }

    public String handlePostResponse(HttpResponse<String> response) throws BadRequestByFtxException {
       return handleResponse(response);
    }

    public String handleResponse(HttpResponse<String> response) throws BadRequestByFtxException {
        return defaultHandleResponse(response).result().get();
    }

    public FTXResponceData defaultHandleResponse(HttpResponse<String> response) throws BadRequestByFtxException{
        try {
            var responseData = ftxResponseParser.getResponseData(response);
            if (responseData.isSuccess() == false){
                responseErrorHandler.handleRequestException(response, responseData);
            }
            return responseData;
        } catch (ParseToModelException e){

            var errorMessage = String.format("Stage: Parse response. ErrorMessage: %s." +
                            " Method: %s. Status: %d. Request uri: %s",
                    e.getMessage(),
                    response.request().method(),
                    response.statusCode(),
                    response.request().uri()
            );

            logger.error(errorMessage, e);
            throw e;
        }
    }

}
