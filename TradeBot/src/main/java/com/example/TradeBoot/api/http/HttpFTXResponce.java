package com.example.TradeBoot.api.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;
import java.util.Optional;

public class HttpFTXResponce {

    private final Boolean success;
    private final Optional<String> result;
    private final Optional<String> error;

    public Boolean isSuccess() {
        return success;
    }

    public Optional<String> getResult() {
        return result;
    }

    public Optional<String> getError() {
        return error;
    }

    public HttpFTXResponce(HttpResponse<String> response){
        JsonNode node = readNode(response);
        result = GetNode(node,"result");
        error = GetNode(node,"error");
        success = isSuccess(node);
    }

    private boolean isSuccess(JsonNode node){
        return node.get("success").asText().equals("true");
    }

    private Optional<String> GetNode(JsonNode node, String targetNodeName){
        var currentNode = node.get(targetNodeName);
        if(currentNode == null){
            return Optional.empty();
        }
        return  Optional.of(currentNode.toPrettyString());
    }

    private JsonNode readNode(HttpResponse<String> response){
        try {
            return new ObjectMapper().readTree(response.body());
        }
        catch (JsonProcessingException e){
            throw new RuntimeException("Processing json '" + response.body() + "' throw exception", e);
        }
    }

}
