package com.example.TradeBoot.api.http;

import com.example.TradeBoot.api.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class HTTPWorkerTests {

    @Autowired
    private AccountService accountService;


    @Test
    void isAuthCurrent() {

        try {
            accountService.getAccountInformation();
        } catch (Exception e) {
            fail("Error with request", e);
        }
    }









}

