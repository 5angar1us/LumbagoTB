package com.example.TradeBoot.api.http;

import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.*;

import org.springframework.boot.logging.LogLevel;
import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jext.Logger;
import uk.org.lidalia.slf4jext.LoggerFactory;

import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;

public class HttpResponseErrorHandler {

    static final Logger logger =
            LoggerFactory.getLogger(HttpResponseErrorHandler.class);

    public void handleRequestException(HttpResponse<String> response, FTXResponceData responseData) {
        var methodName = response.request().method();
        var statusCode = response.statusCode();


        String detail = "";
        var errorMessage = String.format("Stage: Handling response. ErrorMessage: %s." +
                        " Method: %s. Status: %d. Request uri: %s",
                responseData.error().get(),
                methodName,
                statusCode,
                response.request().uri()
        );


        var apiErrorMessage = responseData.error().get();
        ExceptionData exceptionData = switch (apiErrorMessage) {
            case "Order already closed" -> new ExceptionData(new OrderAlreadyClosedException(), Level.ERROR, "");
            case "Please retry request" -> new ExceptionData(new RetryRequestException(), Level.ERROR, "");
            case "Order already queued for cancellation" ->
                    new ExceptionData(new OrderAlreadyQueuedForCancellationException(), Level.ERROR, "");
            case "Not logged in: Invalid signature", "Invalid signature" -> {
                var headers = response.request().headers();
                var details = String.format("Headers: %s, %s, %s, %s.",
                        getMessageTuple(headers, EHttpHeaders.FTX_KEY.getName()),
                        getMessageTuple(headers, EHttpHeaders.FTX_SIGN.getName()),
                        getMessageTuple(headers, EHttpHeaders.FTX_TS.getName()),
                        getMessageTuple(headers, EHttpHeaders.FTX_SUBACCOUNT.getName())
                );
                yield new ExceptionData(new InvalidSignatureException(errorMessage + details), Level.ERROR, "");
            }
            default -> {
                if (apiErrorMessage.contains("An unexpected error occurred, please try again later")) {
                    yield new ExceptionData(new UnexpectedErrorException(apiErrorMessage), Level.ERROR, "");
                }
                if (apiErrorMessage.contains("Do not send more than 2 orders")) {
                    yield new ExceptionData(new DoNotSendMoreThanExeption(apiErrorMessage), Level.ERROR, "");
                }
                yield new ExceptionData(new UnknownErrorRequestByFtxException(errorMessage), Level.ERROR, "");
            }
        };

        var logMessage = String.format("%s. %s",
                errorMessage,
                detail);

        logger.log(exceptionData.logLevel, logMessage);
        throw exceptionData.exception;
    }

    private String getMessageTuple(HttpHeaders httpHeaders, String headerName) {
        var headerValue = httpHeaders.firstValue(headerName)
                .orElse("null");

        return String.format("[ %s : %s ]",
                headerName,
                headerValue
        );
    }

    record ExceptionData(BadRequestByFtxException exception, Level logLevel, String errorDetail) {
    }
}
