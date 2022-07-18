package com.example.TradeBoot.api.utils;

import com.example.TradeBoot.api.extentions.MapperConvertException;
import com.example.TradeBoot.api.extentions.ParseToJsonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonJsonModelConverter {

    public static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static <T> T convertJsonToModel(Class<T> tClass, String json) {
        return new Gson().fromJson(json, tClass);
    }

    public static <T> List<T> convertJsonToListOfModels(Class<T> tClass, String jsonArray) {
        Type listType = new TypeToken<ArrayList<T>>(){}.getType();
        return new Gson().fromJson(jsonArray, listType);

    }

    public static String convertModelToJson(Object object) {
        return gson.toJson(object);
    }

}
