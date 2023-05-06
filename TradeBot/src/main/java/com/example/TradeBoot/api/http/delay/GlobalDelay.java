package com.example.TradeBoot.api.http.delay;

import com.example.TradeBoot.api.extentions.RequestExcpetions.BadRequestByFtxException;
import com.example.TradeBoot.api.http.IHttpClientWorker;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class GlobalDelay implements IHttpClientWorker {

    static final Logger log =
            LoggerFactory.getLogger(GlobalDelay.class);

    private final IHttpClientWorker httpClientWorker;

    private final Bucket bucket;

    public GlobalDelay(IHttpClientWorker httpClientWorker, int requestLimit1000, int requestLimit200) {
        this.httpClientWorker = httpClientWorker;

        Bandwidth limit1000 = Bandwidth.simple(requestLimit1000, Duration.ofMillis(1010));
        Bandwidth limit200 = Bandwidth.simple(requestLimit200, Duration.ofMillis(210));
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

            synchronized (bucket){

                bucket.asBlocking().consume(1);
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        synchronized (bucket){
            
            var message = String.format(
                    "Global delay end. AvailableTokens: %d. Pointer: %s",
                    bucket.getAvailableTokens(),
                    this);
            log.debug(message);
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
