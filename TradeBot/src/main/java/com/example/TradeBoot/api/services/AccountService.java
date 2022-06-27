package com.example.TradeBoot.api.services;

import com.example.TradeBoot.api.domain.account.AccountInformation;
import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.api.utils.JsonModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final HttpClientWorker httpClient;

    @Autowired
    public AccountService(HttpClientWorker httpClient) {
        this.httpClient = httpClient;
    }



    public AccountInformation getAccountInformation() {
        String json = this.httpClient.createGetRequest("/account");
        return JsonModelConverter.convertJsonToModel(AccountInformation.class, json);
    }
}

