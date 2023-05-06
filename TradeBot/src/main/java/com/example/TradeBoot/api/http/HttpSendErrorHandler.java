package com.example.TradeBoot.api.http;

import com.example.TradeBoot.api.extentions.RequestExcpetions.UnceckedIOException;
import com.example.TradeBoot.api.extentions.RequestExcpetions.UnknownErrorSendRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpSendErrorHandler {
    static final Logger log =
            LoggerFactory.getLogger(HttpSendErrorHandler.class);

    public HttpResponse<String> sendResponseOrHandleError(HttpClient client, HttpRequest httpRequest){
        try {
            return client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {

            var message = getErrorMessage(httpRequest, e);
            log.debug(message);
            throw new UnceckedIOException(message, e);

        } catch (InterruptedException e) {

            var message = getErrorMessage(httpRequest, e);
            log.error(message);
            throw new UnknownErrorSendRequestException(message, e);

        }
    }

    private String getErrorMessage(HttpRequest httpRequest, Exception e) {
        return String.format("Stage: Sending request. ErrorMessage: '%s'. ErrorName: %s. Method: %s. Request uri: %s.",
                e.getMessage(),
                e.getClass().getSimpleName(),
                httpRequest.method(),
                httpRequest.uri()
        );
    }
}
