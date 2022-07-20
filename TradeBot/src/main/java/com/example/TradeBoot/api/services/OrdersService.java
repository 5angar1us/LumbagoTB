package com.example.TradeBoot.api.services;

import com.example.TradeBoot.api.domain.orders.*;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Checked.BadRequestByFtxException;
import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.api.http.IHttpClientWorker;
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

    private final IHttpClientWorker httpClient;

    private static final String ORDERS_PATH = "/orders";

    @Autowired
    public OrdersService(IHttpClientWorker httpClient) {
        this.httpClient = httpClient;
    }

    public List<Order> getOpenOrders(String marketName) {
        if (Strings.isNullOrEmpty(marketName))
            throw new IllegalArgumentException("marketName");

        String uri = UriComponentsBuilder.newInstance()
                .path(ORDERS_PATH)
                .query("market={keyword}")
                .buildAndExpand(marketName)
                .toUriString();

        String json = this.httpClient.createGetRequest(uri);
        return JsonModelConverter.convertJsonToListOfModels(Order.class, json);
    }

    private String placeOrderURI = UriComponentsBuilder.newInstance()
            .path(ORDERS_PATH)
            .toUriString();

    public PlacedOrder placeOrder(OrderToPlace order) throws BadRequestByFtxException {
        String placeOrderJson = JsonModelConverter.convertModelToJson(order);
        String placedOrderJson = this.httpClient.createPostRequest(placeOrderURI, placeOrderJson);
        return JsonModelConverter.convertJsonToModel(PlacedOrder.class, placedOrderJson);
    }


    public boolean cancelOrder(String orderId) throws BadRequestByFtxException {
        if (Strings.isNullOrEmpty(orderId))
            throw new IllegalArgumentException("orderId");

        String uri = UriComponentsBuilder.newInstance()
                .path(ORDERS_PATH)
                .path("/").pathSegment(orderId)
                .toUriString();

        return this.httpClient.createDeleteRequest(uri);
    }

    public boolean cancelAllOrder() throws BadRequestByFtxException {
        String uri = UriComponentsBuilder.newInstance()
                .path(ORDERS_PATH)
                .toUriString();

        return this.httpClient.createDeleteRequest(uri);
    }

    public boolean cancelAllOrderBy(OrderCancellationBuilder builder) throws BadRequestByFtxException {
        String uri = UriComponentsBuilder.newInstance()
                .path(ORDERS_PATH)
                .toUriString();

        String body = builder.toString();

        return this.httpClient.createDelete(uri, body);
    }

    public boolean cancelAllOrderByMarket(String marketName) throws BadRequestByFtxException {
        if (Strings.isNullOrEmpty(marketName))
            throw new IllegalArgumentException("marketName");

        OrderCancellationBuilder builder = new OrderCancellationBuilder()
                .TargetMarket(marketName);

        return cancelAllOrderBy(builder);
    }

    public Order getOrderStatus(String orderId) {
        String uri = UriComponentsBuilder.newInstance()
                .path(ORDERS_PATH)
                .path("/").path(orderId)
                .toUriString();

        String json = this.httpClient.createGetRequest(uri);
        return JsonModelConverter.convertJsonToModel(Order.class, json);
    }

    public PlacedOrder modifyOrderBy(ModifyOrderBuilder builder, String orderId) throws BadRequestByFtxException {
        String uri = UriComponentsBuilder.newInstance()
                .path(ORDERS_PATH)
                .path("/").pathSegment(orderId)
                .path("/modify")
                .toUriString();

        String body = builder.toString();
        String json = this.httpClient.createPostRequest(uri, body);
        return JsonModelConverter.convertJsonToModel(PlacedOrder.class, json);
    }
    public PlacedOrder modifyOrderPrice(String orderID, BigDecimal price) throws BadRequestByFtxException {

        var builder = new ModifyOrderBuilder()
                .TargetPrice(price);

        return modifyOrderBy(builder, orderID);

    }
}
