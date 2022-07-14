package com.example.TradeBoot.api.http;

import com.example.TradeBoot.TradeBootApplication;
import com.example.TradeBoot.api.extentions.BadImportantRequestByFtxException;
import com.example.TradeBoot.api.extentions.BadRequestByFtxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpResponse;

public class HttpResponseHandler {

    static final Logger log =
            LoggerFactory.getLogger(HttpResponseHandler.class);
    HttpFTXResponseHandler ftxResponseHandler = new HttpFTXResponseHandler();
    public String handleGetResponse(HttpResponse<String> response) {
        var responseData = ftxResponseHandler.getResponseData(response);

        if (responseData.isSuccess() == false){
            log.error("Get request: " + response.body());
            throw new BadRequestByFtxException(
                    String.format("Bad request. Status %d. Error: %s. Body: %s",
                            Integer.valueOf(response.statusCode()),
                            responseData.error().get(),
                            response.body()
                    )
            );
        }

        return responseData.result().get();
    }

    public boolean handleDeleteResponse(HttpResponse<String> response) throws BadImportantRequestByFtxException {
        var responseData = ftxResponseHandler.getResponseData(response);


        if (responseData.isSuccess() == false){
            log.error("Delete request: " + response.body());

            throw new BadImportantRequestByFtxException(
                    String.format("Bad request. Status %d. Error: %s. Body: %s",
                            Integer.valueOf(response.statusCode()),
                            responseData.error().get(),
                            response.body()
                    )
            );
        }

        return responseData.isSuccess();
    }

    public String handlePostResponse(HttpResponse<String> response) throws BadImportantRequestByFtxException {
        var responseData = ftxResponseHandler.getResponseData(response);

        if (responseData.isSuccess() == false){
            log.error("Post request: " + response.body());
            throw new BadImportantRequestByFtxException(
                    String.format("Bad request. Status %d. Error: %s. Body: %s",
                            Integer.valueOf(response.statusCode()),
                            responseData.error().get(),
                            response.body()
                    )
            );
        }
        else {
            return responseData.result().get();
        }
    }
}
