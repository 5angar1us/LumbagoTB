package com.example.TradeBoot.api.http;

import com.example.TradeBoot.api.extentions.BadImportantRequestByFtxException;
import com.example.TradeBoot.api.extentions.BadRequestByFtxException;

import java.net.http.HttpResponse;

public class HttpResponseHandler {

    public String handleGetResponse(HttpResponse<String> response) {
        HttpFTXResponce ftxResponce = new HttpFTXResponce(response);

        if (ftxResponce.isSuccess() == false){
            System.out.println("Get request: " + response.body());
            throw new BadRequestByFtxException(
                    String.format("Bad request. Status %d. Error: %s. Body: %s",
                            Integer.valueOf(response.statusCode()),
                            ftxResponce.getError().get(),
                            response.body()
                    )
            );
        }

        return ftxResponce.getResult().get();
    }

    public boolean handleDeleteResponse(HttpResponse<String> response) throws BadImportantRequestByFtxException {
        HttpFTXResponce ftxResponce = new HttpFTXResponce(response);


        if (ftxResponce.isSuccess() == false){
            System.out.println("Delete request: " + response.body());

            throw new BadImportantRequestByFtxException(
                    String.format("Bad request. Status %d. Error: %s. Body: %s",
                            Integer.valueOf(response.statusCode()),
                            ftxResponce.getError().get(),
                            response.body()
                    )
            );
        }

        return ftxResponce.isSuccess();
    }

    public String handlePostResponse(HttpResponse<String> response) throws BadImportantRequestByFtxException {
        HttpFTXResponce ftxResponce = new HttpFTXResponce(response);

        if (ftxResponce.isSuccess() == false){
            System.out.println("Post request: " + response.body());
            throw new BadImportantRequestByFtxException(
                    String.format("Bad request. Status %d. Error: %s. Body: %s",
                            Integer.valueOf(response.statusCode()),
                            ftxResponce.getError().get(),
                            response.body()
                    )
            );
        }
        else {
            return ftxResponce.getResult().get();
        }
    }
}
