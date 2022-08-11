package com.example.TradeBoot.api.http;

import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.UnceckedIOException;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.UnknownErrorSendRequestException;
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
            log.error(message, e);
            throw new UnceckedIOException(message, e);

        } catch (InterruptedException e) {

            var message = getErrorMessage(httpRequest, e);
            log.error(message, e);
            throw new UnknownErrorSendRequestException(message, e);

        }
    }

    private String getErrorMessage(HttpRequest httpRequest, Exception e) {
        return String.format("Sending a %s request to '%s' throws exception. ErrorName: %s. ErrorMessage: '%s'",
                httpRequest.method(),
                httpRequest.uri(),
                e.getClass().getSimpleName(),
                e.getMessage()
        );
    }
}
