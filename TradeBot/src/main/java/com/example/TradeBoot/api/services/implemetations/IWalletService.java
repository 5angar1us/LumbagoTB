package com.example.TradeBoot.api.services.implemetations;

import com.example.TradeBoot.api.domain.wallet.Balance;
import com.example.TradeBoot.api.domain.wallet.Coin;
import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.api.utils.JsonModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

public interface IWalletService {
    List<Coin> getAllCoins();

    List<Balance> getBalances();

    Optional<Balance> getBalanceByMarket(String market);

    Balance getBalanceByMarketOrTrow(String market);

    abstract class Abstract implements IWalletService{
        @Override
        public Optional<Balance> getBalanceByMarket(String market) {

            String currentCoinName = getCoinName(market);

            return getBalances().stream()
                    .filter(balance -> balance.getCoin().equals(currentCoinName))
                    .findFirst();
        }

        @Override
        public Balance getBalanceByMarketOrTrow(String market) {
            Optional<Balance> balance = getBalanceByMarket(market);
            return balance.orElseThrow();
        }

        protected String getCoinName(String market) {
            int coinSeparatorIndex = market.indexOf("/");

            return market.substring(0, coinSeparatorIndex);
        }
    }

    @Service
    class Base extends Abstract {

        private final HttpClientWorker worker;

        private final String WALLET_PATH = "/wallet";

        @Autowired
        public Base(HttpClientWorker worker) {
            this.worker = worker;
        }

        @Override
        public List<Coin> getAllCoins() {

            String uri = UriComponentsBuilder.newInstance()
                    .path(WALLET_PATH)
                    .path("/coins")
                    .toUriString();
            String json = this.worker.createGetRequest(uri);
            return JsonModelConverter.convertJsonToListOfModels(Coin.class, json);
        }

        @Override
        public List<Balance> getBalances() {
            String uri = UriComponentsBuilder.newInstance()
                    .path(WALLET_PATH)
                    .path("/balances")
                    .toUriString();

            String json = this.worker.createGetRequest(uri);
            return JsonModelConverter.convertJsonToListOfModels(Balance.class, json);
        }
    }

    class Mock extends Abstract {

        private List<Coin> coins;
        private List<Balance> balances;

        @Override
        public List<Coin> getAllCoins() {
            return coins;
        }

        @Override
        public List<Balance> getBalances() {
            return balances;
        }

        public void setCoins(List<Coin> coins) {
            this.coins = coins;
        }

        public void setBalances(List<Balance> balances) {
            this.balances = balances;
        }
    }
}
