package com.example.TradeBoot.api.http;

import com.example.TradeBoot.configuration.TestConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class HTTPWorkerTests {

    private static HttpRequestFactory httpRequestFactory;

    @BeforeAll
    static void init() {
        httpRequestFactory = new HttpRequestFactory(TestConfig.getAuntification());
    }

    @Test
    void isAuthCurrent() {

        String uri = "/account";

        var request = httpRequestFactory.createGetRequest(uri);

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            HttpFTXResponseHandler httpFTXResponseHandler = new HttpFTXResponseHandler();
            var responceData = httpFTXResponseHandler.getResponseData(response);

            assertEquals(true, responceData.isSuccess());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Get request to '" + uri + "' throws exception.", e);
        }
    }









}

