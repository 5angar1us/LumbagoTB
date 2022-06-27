package com.example.TradeBoot.api.services;

import com.example.TradeBoot.api.domain.wallet.Balance;
import com.example.TradeBoot.api.domain.wallet.Coin;
import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.api.utils.JsonModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
public class WalletService {

    private final HttpClientWorker worker;

    private final String WALLET_PATH = "/wallet";

    @Autowired
    public WalletService(HttpClientWorker worker) {
        this.worker = worker;
    }

    public List<Coin> getAllCoins() {

        String uri = UriComponentsBuilder.newInstance()
                .path(WALLET_PATH)
                .path("/coins")
                .toUriString();
        String json = this.worker.createGetRequest(uri);
        return JsonModelConverter.convertJsonToListOfModels(Coin.class, json);
    }

    public List<Balance> getBalances() {
        String uri = UriComponentsBuilder.newInstance()
                .path(WALLET_PATH)
                .path("/balances")
                .toUriString();

        String json = this.worker.createGetRequest(uri);
        return JsonModelConverter.convertJsonToListOfModels(Balance.class, json);
    }

    public Optional<Balance> getBalanceByMarket(String market) {

        String currentCoinName = getCoinName(market);

        return getBalances().stream()
                .filter(balance -> balance.getCoin().equals(currentCoinName))
                .findFirst();
    }

    public Balance getBalanceByMarketOrTrow(String market) {
        Optional<Balance> balance = getBalanceByMarket(market);
        return balance.orElseThrow();
    }


    private String getCoinName(String market) {
        int separatorIndex = market.indexOf("/");
        return market.substring(0, separatorIndex);
    }
}
