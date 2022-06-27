package com.example.TradeBoot.api.services;

import com.example.TradeBoot.api.domain.orders.OpenOrder;
import com.example.TradeBoot.api.domain.orders.OrderStatus;
import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.api.domain.orders.PlacedOrder;
import com.example.TradeBoot.api.extentions.BadImportantRequestByFtxException;
import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.api.utils.JsonModelConverter;
import com.example.TradeBoot.api.utils.ModifyOrderBuilder;
import com.example.TradeBoot.api.utils.OrderCancellationBuilder;
import com.example.TradeBoot.api.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrdersService {

    private final HttpClientWorker httpClient;

    private static final String ORDERS_PATH = "/orders";

    @Autowired
    public OrdersService(HttpClientWorker httpClient) {
        this.httpClient = httpClient;
    }

    public List<OpenOrder> getOpenOrders(String marketName) {
        if (Strings.isNullOrEmpty(marketName))
            throw new IllegalArgumentException("marketName");

        String uri = UriComponentsBuilder.newInstance()
                .path(ORDERS_PATH)
                .query("market={keyword}")
                .buildAndExpand(marketName)
                .toUriString();

        String json = this.httpClient.createGetRequest(uri);
        return JsonModelConverter.convertJsonToListOfModels(OpenOrder.class, json);
    }

    public PlacedOrder placeOrder(OrderToPlace order) throws BadImportantRequestByFtxException {
        String uri = UriComponentsBuilder.newInstance()
                .path(ORDERS_PATH)
                .toUriString();

        String placeOrderJson = JsonModelConverter.convertModelToJson(order);
        String placedOrderJson = this.httpClient.createPostRequest(uri, placeOrderJson);
        return JsonModelConverter.convertJsonToModel(PlacedOrder.class, placedOrderJson);
    }


    public boolean cancelOrder(String orderId) throws BadImportantRequestByFtxException {
        if (Strings.isNullOrEmpty(orderId))
            throw new IllegalArgumentException("orderId");

        String uri = UriComponentsBuilder.newInstance()
                .path(ORDERS_PATH)
                .path("/").pathSegment(orderId)
                .toUriString();

        return this.httpClient.createDeleteRequest(uri);
    }

    public boolean cancelAllOrder() throws BadImportantRequestByFtxException {
        String uri = UriComponentsBuilder.newInstance()
                .path(ORDERS_PATH)
                .toUriString();

        return this.httpClient.createDeleteRequest(uri);
    }

    public boolean cancelAllOrderBy(OrderCancellationBuilder builder) throws BadImportantRequestByFtxException {
        String uri = UriComponentsBuilder.newInstance()
                .path(ORDERS_PATH)
                .toUriString();

        String body = builder.toString();

        return this.httpClient.createDelete(uri, body);
    }

    public boolean cancelAllOrderByMarket(String marketName) throws BadImportantRequestByFtxException {
        if (Strings.isNullOrEmpty(marketName))
            throw new IllegalArgumentException("marketName");

        OrderCancellationBuilder builder = new OrderCancellationBuilder()
                .TargetMarket(marketName);

        return cancelAllOrderBy(builder);
    }

    public OrderStatus getOrderStatus(String orderId) {
        String uri = UriComponentsBuilder.newInstance()
                .path(ORDERS_PATH)
                .path("/").path(orderId)
                .toUriString();

        String json = this.httpClient.createGetRequest(uri);
        return JsonModelConverter.convertJsonToModel(OrderStatus.class, json);
    }

    public PlacedOrder modifyOrderBy(ModifyOrderBuilder builder, String orderId) throws BadImportantRequestByFtxException {
        String uri = UriComponentsBuilder.newInstance()
                .path(ORDERS_PATH)
                .path("/").pathSegment(orderId)
                .path("/modify")
                .toUriString();

        String body = builder.toString();
        String json = this.httpClient.createPostRequest(uri, body);
        return JsonModelConverter.convertJsonToModel(PlacedOrder.class, json);
    }
    public PlacedOrder modifyOrderPrice(String orderID, BigDecimal price) throws BadImportantRequestByFtxException {

        var builder = new ModifyOrderBuilder()
                .TargetPrice(price);

        return modifyOrderBy(builder, orderID);

    }
}
