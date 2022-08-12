package com.example.TradeBoot.api.http;

import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;

public class HttpResponseErrorHandler {

    static final Logger log =
            LoggerFactory.getLogger(HttpResponseErrorHandler.class);

    public void handleRequestException(HttpResponse<String> response, FTXResponceData responseData) {
        var methodName = response.request().method();
        var statusCode = response.statusCode();

        var errorMessage = String.format("Response from %s request. Status %d. ErrorMessage: %s.",
                methodName,
                Integer.valueOf(statusCode),
                responseData.error().get()
        );

        log.error(errorMessage);

        var apiErrorMessage = responseData.error().get();
        switch (apiErrorMessage) {
            case "Order already closed" -> throw new OrderAlreadyClosedException();
            case "Please retry request" -> throw new RetryRequestException();
            case "Order already queued for cancellation" -> throw new OrderAlreadyQueuedForCancellationException();
            case "Not logged in: Invalid signature", "Invalid signature" -> {
                var headers = response.headers();
                var details = String.format("Headers: %s, %s, %s, %s.",
                        getMessageTuple(headers, EHttpHeaders.FTX_KEY.getName()),
                        getMessageTuple(headers, EHttpHeaders.FTX_SIGN.getName()),
                        getMessageTuple(headers, EHttpHeaders.FTX_TS.getName()),
                        getMessageTuple(headers, EHttpHeaders.FTX_SUBACCOUNT.getName())
                );
                log.error(details);
                throw new InvalidSignatureException(errorMessage + details);
            }
            default -> {
                if (apiErrorMessage.contains("An unexpected error occurred, please try again later")) {
                    throw new UnexpectedErrorException(apiErrorMessage);
                }

                throw new UnknownErrorRequestByFtxException(errorMessage);
            }
        }
    }

    private String getMessageTuple(HttpHeaders httpHeaders, String headerName) {
        var headerValue = httpHeaders.firstValue(headerName)
                .orElse("null");

        return String.format("[ %s : %s ]",
                headerName,
                headerValue
        );
    }
}
