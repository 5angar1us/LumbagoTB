package com.example.TradeBoot.api.utils;

import com.example.TradeBoot.api.extentions.ParseToModelException;
import com.example.TradeBoot.api.extentions.ParseToJsonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.ArrayList;
import java.util.List;

public class JsonModelConverter {
    public static <T> T convertJsonToModel(Class<T> tClass, String json) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            throw new ParseToModelException("Convert throws exception", e);
        }
    }

    public static <T> List<T> convertJsonToListOfModels(Class<T> tClass, String jsonArray){
        ObjectMapper mapper = new ObjectMapper();

        CollectionType javaType = mapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, tClass);
        try {
            return mapper.readValue(jsonArray, javaType);

        } catch (JsonProcessingException e) {
            throw new ParseToModelException("Convert throws exception", e);
        }

    }

    public static String convertModelToJson(Object object) {
        ObjectWriter objectWriter = new ObjectMapper()
                .writer()
                .withDefaultPrettyPrinter();

        try {
            return objectWriter.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ParseToJsonException("Error during pars " + object.getClass(), e);
        }
    }
}