package com.example.TradeBoot.api;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.utils.OrderCancellationBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OrderCancellationBuilderTest {


    @Test
    public void canCreate() throws JsonProcessingException {
        String market = "FTT/USD";
        ESide side = ESide.BUY;
        boolean isLimitedOrdersOnly = false;

        String cancellationParameters = new OrderCancellationBuilder()
                .TargetMarket(market)
                .TargetSide(side)
                .isLimitOrdersOnly(isLimitedOrdersOnly)
                .toString();

        cancellationParameters = cancellationParameters + "[";

        JsonNode node = new ObjectMapper().readTree(cancellationParameters);
        var marketValue = node.get("market").textValue();
        var sideValue = node.get("side").textValue();
        var limitOrdersOnlyValue = node.path("limitOrdersOnly").textValue();

        assertAll(
                () -> assertEquals(market, marketValue),
                () -> assertEquals(side.toString(), sideValue),
                () -> assertEquals(String.valueOf(isLimitedOrdersOnly), limitOrdersOnlyValue)
        );
    }
    @Test
    public void canAddTwoParameters() throws JsonProcessingException {
        String market = "FTT/USD";
        String market2 = market+"2";

        String cancellationParameters = new OrderCancellationBuilder()
                .TargetMarket(market)
                .TargetMarket(market2)
                .toString();

        JsonNode node = new ObjectMapper().readTree(cancellationParameters);
        var marketValue = node.get("market").toPrettyString().replace("\"","");

        assertEquals(market2,marketValue);
    }




}
