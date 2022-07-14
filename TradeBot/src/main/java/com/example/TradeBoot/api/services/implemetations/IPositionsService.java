package com.example.TradeBoot.api.services.implemetations;

import com.example.TradeBoot.api.domain.account.Position;
import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.api.utils.JsonModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface IPositionsService {
    List<Position> getAllPositions();

    Optional<Position> getPositionByMarket(String market);

    Position getPositionByMarketOrTrow(String market);


    abstract class Abstract implements IPositionsService {

        @Override
        public Optional<Position> getPositionByMarket(String market){
            return getAllPositions().stream()
                    .filter(position -> position.getFuture().equals(market))
                    .findFirst();
        }
        @Override
        public Position getPositionByMarketOrTrow(String market){
            return getAllPositions().stream()
                    .filter(position -> position.getFuture().equals(market))
                    .findFirst()
                    .orElseThrow();
        }
    }

    @Service
    class Base extends Abstract {

        private HttpClientWorker httpClient;

        @Autowired
        public Base(HttpClientWorker httpClient) {
            this.httpClient = httpClient;
        }

        @Override
        public List<Position> getAllPositions() {
            String json = this.httpClient.createGetRequest("/positions");
            return JsonModelConverter.convertJsonToListOfModels(Position.class,json);
        }
    }

    class Mock extends Abstract{
        public void setPositions(List<Position> positions) {
            this.positions = positions;
        }

        private List<Position> positions;
        @Override
        public List<Position> getAllPositions() {
            return positions;
        }

    }
}
