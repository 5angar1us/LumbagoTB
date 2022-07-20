package com.example.TradeBoot.api.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;
import java.util.Optional;

public class HttpFTXResponseParser {

    public HttpFTXResponseParser() {

    }

    public FTXResponceData getResponseData(HttpResponse<String> response) {
        JsonNode node = readNode(response);
        var result = getNode(node, "result");
        var error = parseMessage(getNode(node, "error"));
        var success = isSuccess(node);


        return new FTXResponceData(success, result, error);
    }

    private boolean isSuccess(JsonNode node) {
        return node.get("success").asText().equals("true");
    }

    private Optional<String> getNode(JsonNode node, String targetNodeName) {
        var currentNode = node.get(targetNodeName);
        if (currentNode == null) {
            return Optional.empty();
        }
        return Optional.of(currentNode.toPrettyString());
    }

    private Optional<String> parseMessage(Optional<String> nodeBody) {
        if (nodeBody.isEmpty()) return Optional.empty();

        var value = nodeBody.get();
        value = value.replace("\"", "");
        return Optional.of(value);
    }

    private JsonNode readNode(HttpResponse<String> response) {
        try {
            return new ObjectMapper().readTree(response.body());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Processing json '" + response.body() + "' throw exception", e);
        }
    }


}

record FTXResponceData(boolean isSuccess, Optional<String> result, Optional<String> error) {

}
