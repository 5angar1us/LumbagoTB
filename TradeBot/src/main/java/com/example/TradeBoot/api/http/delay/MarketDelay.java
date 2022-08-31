package com.example.TradeBoot.api.http.delay;

import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.BadRequestByFtxException;
import com.example.TradeBoot.api.http.IHttpClientWorker;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class MarketDelay implements IHttpClientWorker {
    static final Logger log =
            LoggerFactory.getLogger(MarketDelay.class);

    private IHttpClientWorker httpClientWorker;

    private Bucket bucket;

    public MarketDelay(IHttpClientWorker httpClientWorker, int requestLimit) {
        this.httpClientWorker = httpClientWorker;

        Refill refill = Refill.intervally(requestLimit, Duration.ofMillis(200));
        Bandwidth limit = Bandwidth.classic(requestLimit, refill);
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }


    public String createGetRequest(String uri) {
        return httpClientWorker.createGetRequest(uri);
    }


    public String createPostRequest(String uri, String body) throws BadRequestByFtxException {

        try {
            bucket.asBlocking().consume(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return httpClientWorker.createPostRequest(uri, body);
    }


    public boolean createDeleteRequest(String uri) throws BadRequestByFtxException {
        return httpClientWorker.createDeleteRequest(uri);
    }


    public boolean createDeleteRequest(String uri, String body) throws BadRequestByFtxException {
        return httpClientWorker.createDeleteRequest(uri, body);
    }

}