package com.example.TradeBoot.api.http;

import com.example.TradeBoot.api.extentions.RequestExcpetions.Checked.BadRequestByFtxException;

public class HttpClientWorkerWithDelay implements IHttpClientWorker {

    private long lastRequestTime = System.currentTimeMillis();

    private final long MINIMUM_DELAY_MS = 100;

    private IHttpClientWorker httpClientWorker;

    public HttpClientWorkerWithDelay(IHttpClientWorker httpClientWorker) {
        this.httpClientWorker = httpClientWorker;
    }

    @Override
    public String createGetRequest(String uri) {
        return httpClientWorker.createGetRequest(uri);
    }

    @Override
    public String createPostRequest(String uri, String body) throws BadRequestByFtxException {

        synchronized (httpClientWorker) {
            try {
                long currentTime = System.currentTimeMillis();
                var delayBetweenLastRequest = currentTime - lastRequestTime;
                if (delayBetweenLastRequest < MINIMUM_DELAY_MS) {
                    Thread.sleep(MINIMUM_DELAY_MS - delayBetweenLastRequest);
                }

                var requestResult = httpClientWorker.createPostRequest(uri, body);
                lastRequestTime = System.currentTimeMillis();
                return requestResult;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean createDeleteRequest(String uri) throws BadRequestByFtxException {
        return httpClientWorker.createDeleteRequest(uri);
    }

    @Override
    public boolean createDelete(String uri, String body) throws BadRequestByFtxException {
        return httpClientWorker.createDelete(uri, body);
    }
}
