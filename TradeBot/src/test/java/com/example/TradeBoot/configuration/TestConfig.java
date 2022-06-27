package com.example.TradeBoot.configuration;

import com.example.TradeBoot.api.http.auntification.Auntification;
import com.example.TradeBoot.api.http.auntification.Encoder;
import com.example.TradeBoot.api.http.auntification.HashAlgorithm;
import com.example.TradeBoot.api.http.auntification.TimeKeper;

import java.util.Optional;

public class TestConfig {
    private static final String key = "8QzSSg_FlJOTEeVjTaA2SrR0fnr3kPVDM4hYCmkk";
    private static final String secret = "aGxKOuSS2m0J_Z8MgGt40Z6YVChl_82kMwjFkqZe";

    private static final Optional<String> subAccount = Optional.of("2");

    public static final String MARKET_NAME = "GMT/USD";
    public static final String SHORT_MARKET_NAME ="AVAX/USD";

    public static Auntification getAuntification(){
        Auntification auntification = new Auntification(new Encoder(new HashAlgorithm.HmacSHa256()), new TimeKeper.Base());
        auntification.Init(key, secret, subAccount);

        return auntification;
    }
}
