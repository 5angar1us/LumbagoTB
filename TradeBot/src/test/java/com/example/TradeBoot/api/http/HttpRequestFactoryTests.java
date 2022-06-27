package com.example.TradeBoot.api.http;

import com.example.TradeBoot.api.http.auntification.Auntification;
import com.example.TradeBoot.api.http.auntification.Encoder;
import com.example.TradeBoot.api.http.auntification.HashAlgorithm;
import com.example.TradeBoot.api.http.auntification.TimeKeper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class HttpRequestFactoryTests {

    private String key = "LR0RQT6bKjrUNh38eCw9jYC89VDAbRkCogAc_XAm";
    private String secret = "T4lPid48QtjNxjLUFOcUZghD7CUJ7sTVsfuvQZF2";


    HttpRequestFactory CreateMockHttpRequestFactory(String key, String secret, long timeStamp) {
        Auntification auntification = new Auntification(new Encoder(new HashAlgorithm.HmacSHa256()), new TimeKeper.Mock(timeStamp));
        auntification.Init(key, secret, Optional.empty());

        return  new HttpRequestFactory(auntification);
    }

    @Test
    void isTestPostRequestCurrect() {
        String uri = "/orders";
        var timeStamp = 1588591856950L;
        var body = "{\"market\": \"BTC-PERP\", \"side\": \"buy\", \"price\": 8500, \"size\": 1, \"type\": \"limit\", \"reduceOnly\": false, \"ioc\": false, \"postOnly\": false, \"clientId\": null}";

        var httpRequestFactory = CreateMockHttpRequestFactory(key, secret, timeStamp);

        var request = httpRequestFactory.createPostRequest(uri, body);

        var headers = request.headers();
        var requestApiKey = headers.firstValue("FTX-KEY").get();
        var requestSignature = headers.firstValue("FTX-SIGN").get();
        var requestTS = Long.parseLong(headers.firstValue("FTX-TS").get());

        var targetSignature = "c4fbabaf178658a59d7bbf57678d44c369382f3da29138f04cd46d3d582ba4ba";
        assertAll("Should return auth information: API_KEY, signature, TS",
                () -> assertEquals(key, requestApiKey), //API_KEY
                () -> assertEquals(targetSignature, requestSignature), //signature
                () -> assertEquals(1588591856950L, requestTS) //TS
        );

    }

    @Test
    void isTestAuthRequestCurrect() {
        long timeStamp = 1588591511721L;
        String uri = "/markets";

        var httpClient = CreateMockHttpRequestFactory(key, secret, timeStamp);

        var request = httpClient.createGetRequest(uri);

        var headers = request.headers();
        var requestApiKey = headers.firstValue("FTX-KEY").get();
        var requestSignature = headers.firstValue("FTX-SIGN").get();
        var requestTS = Long.parseLong(headers.firstValue("FTX-TS").get());

        assertAll("Should return auth information: API_KEY, signature, TS",
                () -> assertEquals("LR0RQT6bKjrUNh38eCw9jYC89VDAbRkCogAc_XAm", requestApiKey), //API_KEY
                () -> assertEquals("dbc62ec300b2624c580611858d94f2332ac636bb86eccfa1167a7777c496ee6f", requestSignature), //signature
                () -> assertEquals(1588591511721L, requestTS) //TS
        );
    }
}
