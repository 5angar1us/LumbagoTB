package com.example.TradeBoot.api.services;

import com.example.TradeBoot.api.domain.futures.Future;
import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.api.utils.JsonModelConverter;
import com.example.TradeBoot.api.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class FutureService {

    private final HttpClientWorker httpClient;

    private final String FUTURES_PATH = "/futures";

    @Autowired
    public FutureService( HttpClientWorker httpClient) {
        this.httpClient = httpClient;
    }

    public List<Future> getAllFutures() {
        String json = this.httpClient.createGetRequest(FUTURES_PATH);
        return JsonModelConverter.convertJsonToListOfModels(Future.class, json);
    }

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
