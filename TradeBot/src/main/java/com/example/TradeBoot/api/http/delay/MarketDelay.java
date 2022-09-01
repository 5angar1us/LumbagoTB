package com.example.TradeBoot.api.http.delay;

import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.BadRequestByFtxException;
import com.example.TradeBoot.api.http.IHttpClientWorker;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class MarketDelay implements IHttpClientWorker {
    static final Logger log =
            LoggerFactory.getLogger(MarketDelay.class);

    private IHttpClientWorker httpClientWorker;

    private Bucket bucket;

    public MarketDelay(IHttpClientWorker httpClientWorker, int requestLimit200, int requestLimit1000) {
        this.httpClientWorker = httpClientWorker;

        Bandwidth limit200 = Bandwidth.simple(requestLimit200, Duration.ofMillis(210));
        Bandwidth limit1000 = Bandwidth.simple(requestLimit1000, Duration.ofMillis(1010));
        this.bucket = Bucket.builder()
                .addLimit(limit1000)
                .addLimit(limit200)
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
        var message = String.format(
                "Market delay end. AvailableTokens: %d. Pointer: %s",
                bucket.getAvailableTokens(),
                this);

        log.debug(message);
        return httpClientWorker.createPostRequest(uri, body);
    }


    public boolean createDeleteRequest(String uri) throws BadRequestByFtxException {
        return httpClientWorker.createDeleteRequest(uri);
    }


    public boolean createDeleteRequest(String uri, String body) throws BadRequestByFtxException {
        return httpClientWorker.createDeleteRequest(uri, body);
    }

}