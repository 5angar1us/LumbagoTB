package com.example.TradeBoot.api.http.delay;

import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.BadRequestByFtxException;
import com.example.TradeBoot.api.http.IHttpClientWorker;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class GlobalDelay implements IHttpClientWorker {

    static final Logger log =
            LoggerFactory.getLogger(GlobalDelay.class);

    private IHttpClientWorker httpClientWorker;

    private Bucket bucket;

    public GlobalDelay(IHttpClientWorker httpClientWorker, int requestLimitInSecond, int requestLimit) {
        this.httpClientWorker = httpClientWorker;

        Bandwidth limitInSeconds = Bandwidth.simple(requestLimitInSecond, Duration.ofMillis(1010));
        //Bandwidth limit = Bandwidth.simple(requestLimit, Duration.ofMillis(210));
        this.bucket = Bucket.builder()
                .addLimit(limitInSeconds)
                //.addLimit(limit)
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
        log.debug("Global delay end " + this);
        return httpClientWorker.createPostRequest(uri, body);
    }


    public boolean createDeleteRequest(String uri) throws BadRequestByFtxException {
        return httpClientWorker.createDeleteRequest(uri);
    }


    public boolean createDeleteRequest(String uri, String body) throws BadRequestByFtxException {
        return httpClientWorker.createDeleteRequest(uri, body);
    }
}
