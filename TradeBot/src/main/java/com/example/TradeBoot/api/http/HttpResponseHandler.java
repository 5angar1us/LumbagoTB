package com.example.TradeBoot.api.http;

import com.example.TradeBoot.api.extentions.RequestExcpetions.Checked.BadRequestByFtxException;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Checked.OrderAlreadyClosedException;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.UnknownErrorRequestByFtxException;
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

        log.error(String.format("%s request. Status %d. Error: %s.",
                methodName,
                Integer.valueOf(statusCode),
                responseData.error().get()
        ));

        switch (responseData.error().get()){
            default -> throw new UnknownErrorRequestByFtxException(
                    String.format("%s request. Status %d. Error: %s.",
                            methodName,
                            Integer.valueOf(statusCode),
                            responseData.error().get()
                    )
            );
        }
    }
    
    public void handleChangeRequestException(HttpResponse<String> response, FTXResponceData responseData) throws BadRequestByFtxException {
        var methodName = response.request().method();
        var statusCode = response.statusCode();
        
        log.error(String.format("%s request. Status %d. Error: %s.",
                methodName,
                Integer.valueOf(statusCode),
                responseData.error().get()
        ));

        var errorMessage = responseData.error().get();
        switch (errorMessage){
            case "Order already closed" -> throw new OrderAlreadyClosedException();
            default -> throw new UnknownErrorRequestByFtxException(
                    String.format("%s request. Status %d. Error: %s.",
                            methodName,
                            Integer.valueOf(statusCode),
                            responseData.error().get()
                    )
            );
        }


    }


}
