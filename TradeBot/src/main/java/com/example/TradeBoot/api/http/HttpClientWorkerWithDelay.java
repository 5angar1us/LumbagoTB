package com.example.TradeBoot.api.http;

import com.example.TradeBoot.api.extentions.RequestExcpetions.Checked.BadRequestByFtxException;

import java.util.HashMap;
import java.util.Map;

public class HttpClientWorkerWithDelay {

    private final long MINIMUM_DELAY_MS = 100;

    private Map<String, Long> lastRequestToMarketTime = new HashMap<>();

    private IHttpClientWorker httpClientWorker;

    public HttpClientWorkerWithDelay(IHttpClientWorker httpClientWorker) {
        this.httpClientWorker = httpClientWorker;
    }


    public String createGetRequest(String uri) {
        return httpClientWorker.createGetRequest(uri);
    }


    public String createPostRequest(String uri, String body, String market) throws BadRequestByFtxException {

        synchronized (httpClientWorker) {
            try {


                Long lastRequestTime = lastRequestToMarketTime.get(market);
                if(lastRequestTime != null){
                    long currentTime = System.currentTimeMillis();
                    var delayBetweenLastRequest = currentTime - lastRequestTime;
                    if (delayBetweenLastRequest < MINIMUM_DELAY_MS) {
                        Thread.sleep(MINIMUM_DELAY_MS - delayBetweenLastRequest);
                    }
                }
                var requestResult = httpClientWorker.createPostRequest(uri, body);
                lastRequestToMarketTime.put(market, System.currentTimeMillis());

                return requestResult;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public boolean createDeleteRequest(String uri) throws BadRequestByFtxException {
        return httpClientWorker.createDeleteRequest(uri);
    }


    public boolean createDeleteRequest(String uri, String body) throws BadRequestByFtxException {
        return httpClientWorker.createDeleteRequest(uri, body);
    }
}
