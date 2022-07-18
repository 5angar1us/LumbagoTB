package com.example.TradeBoot.api.services;

import com.example.TradeBoot.api.domain.futures.Future;
import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.api.utils.JsonModelConverter;
import com.example.TradeBoot.api.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public interface IFutureService {
    List<Future> getAllFutures();

    Future getFutures(String futureName);

    @Service
    class Base implements IFutureService {

        private final HttpClientWorker httpClient;

        private final String FUTURES_PATH = "/futures";

        @Autowired
        public Base(HttpClientWorker httpClient) {
            this.httpClient = httpClient;
        }

        @Override
        public List<Future> getAllFutures() {
            String json = this.httpClient.createGetRequest(FUTURES_PATH);
            return JsonModelConverter.convertJsonToListOfModels(Future.class, json);
        }

        @Override
        public Future getFutures(String futureName) {
            if (Strings.isNullOrEmpty(futureName))
                throw new IllegalArgumentException("futureName");

            String uri = UriComponentsBuilder.newInstance()
                    .path(FUTURES_PATH)
                    .path("/").pathSegment(futureName)
                    .toUriString();

            String json = this.httpClient.createGetRequest(uri);
            return JsonModelConverter.convertJsonToModel(Future.class, json);
        }

    }

    class Mock implements IFutureService {

        private List<Future> futures;
        private Future future;

        public void setFutures(List<Future> futures) {
            this.futures = futures;
        }

        public void setFuture(Future future) {
            this.future = future;
        }

        @Override
        public List<Future> getAllFutures() {
            return futures;
        }

        @Override
        public Future getFutures(String futureName) {
            return future;
        }
    }
}
