package com.example.TradeBoot.api.services;

import com.example.TradeBoot.api.domain.account.AccountInformation;
import com.example.TradeBoot.api.domain.account.Position;
import com.example.TradeBoot.api.http.IHttpClientWorker;
import com.example.TradeBoot.api.utils.JsonModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final IHttpClientWorker httpClient;

    @Autowired
    public AccountService(IHttpClientWorker httpClient) {
        this.httpClient = httpClient;
    }



    public AccountInformation getAccountInformation() {
        String json = this.httpClient.createGetRequest("/account");
        return JsonModelConverter.convertJsonToModel(AccountInformation.class, json);
    }

    public List<Position> getAllPositions() {
        String json = this.httpClient.createGetRequest("/api/positions");
        return JsonModelConverter.convertJsonToListOfModels(Position.class, json);
    }
}

