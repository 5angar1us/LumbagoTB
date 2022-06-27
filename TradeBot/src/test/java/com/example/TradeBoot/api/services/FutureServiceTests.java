package com.example.TradeBoot.api.services;

import com.example.TradeBoot.api.domain.futures.Future;
import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.configuration.TestServiceInstances;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FutureServiceTests {

    private static HttpClientWorker httpClient;

    private static FutureService futureService;

    @BeforeAll
    static void init() {
        httpClient = TestServiceInstances.getHttpClient();
        futureService = TestServiceInstances.getFutureService();
    }

    @Test
    public void canGetAllFutures(){
        var futures = futureService.getAllFutures();
        for (Future future : futures) {
            System.out.println(future.getName());
        }
    }

    @Test
    public void canGetPerpFutures(){

        var futureNames = futureService.getAllFutures()
                .stream()
                .filter(future -> future.getName().contains("PERP"))
                .map(future -> future.getName());

        futureNames.forEach(name -> System.out.println(name));
    }



}
