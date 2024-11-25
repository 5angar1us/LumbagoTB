package com.example.TradeBoot.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

public abstract class AbstractParameterBuilder {
    protected final HashMap<String, Object> parameters;

    public AbstractParameterBuilder(int parameterCount){
        parameters = new HashMap<>(parameterCount);
    }

    protected void addParameter(String parameterName, Object value) {
        parameters.put(parameterName, value);
    }

    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(parameters);
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }

    private record Parameter(String name, String value) {
    }
}
