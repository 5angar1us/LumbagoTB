package com.example.TradeBoot.api.services;

import com.example.TradeBoot.api.domain.account.Position;
import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.api.utils.JsonModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PositionsService {

    private HttpClientWorker httpClient;

    @Autowired
    public PositionsService(HttpClientWorker httpClient) {
        this.httpClient = httpClient;
    }

    public List<Position> getAllPositions() {
        String json = this.httpClient.createGetRequest("/positions");
        return JsonModelConverter.convertJsonToListOfModels(Position.class,json);
    }

    public Optional<Position> getPositionByMarket(String market){
        return getAllPositions().stream()
                .filter(position -> position.getFuture().equals(market))
                .findFirst();
    }
    public Position getPositionByMarketOrTrow(String market){
        return getAllPositions().stream()
                .filter(position -> position.getFuture().equals(market))
                .findFirst()
                .orElseThrow();
    }
}
