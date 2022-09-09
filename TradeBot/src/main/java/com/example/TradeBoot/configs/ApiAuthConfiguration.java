package com.example.TradeBoot.configs;

import com.example.TradeBoot.api.http.*;
import com.example.TradeBoot.api.http.auntification.Auntification;
import com.example.TradeBoot.api.http.auntification.Encoder;
import com.example.TradeBoot.api.http.auntification.HashAlgorithm;
import com.example.TradeBoot.api.http.auntification.TimeKeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.Objects;
import java.util.Optional;

@Configuration
public class ApiAuthConfiguration {
    @Value("${api.key}")
    private String API_KEY;

    @Value("${secret}")
    private String SECRET;

    @Autowired
    private Environment environment;

    @Bean
    public Auntification getAuth() {

        Auntification auntification = new Auntification(new Encoder(new HashAlgorithm.HmacSHa256()), new TimeKeper.Base());
        Objects.requireNonNull(auntification);


        var subAccount = Optional.of(this.environment.getProperty("subaccount"));

        auntification.Init(API_KEY, SECRET, subAccount);


        return auntification;
    }

    @Bean
    public HttpClientWorker httpClientWorker() {

        HttpRequestFactory httpRequestFactory = new HttpRequestFactory(getAuth());
        HttpResponseHandler httpResponseHandler = new HttpResponseHandler(new HttpFTXResponseParser(), new HttpResponseErrorHandler());

        return new HttpClientWorker(httpRequestFactory, new HttpSendErrorHandler(), httpResponseHandler);
    }


}
