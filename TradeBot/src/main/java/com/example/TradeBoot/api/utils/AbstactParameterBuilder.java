package com.example.TradeBoot.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

public abstract class AbstactParameterBuilder {
    protected HashMap<String, String> parameters;

    public AbstactParameterBuilder(int parameterCount){
        parameters = new HashMap<>(parameterCount);
    }

    protected void addParameter(String parameterName, String value) {
        parameters.put(parameterName, value);
    }

    public String toString() {
        try {
            String json = new ObjectMapper().writeValueAsString(parameters);
            return json;
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }

    private record Parameter(String name, String value) {
    }
}
