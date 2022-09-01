package com.example.TradeBoot.api.services;

import com.example.TradeBoot.api.domain.orders.EStatus;
import com.example.TradeBoot.api.domain.orders.Order;
import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.api.domain.orders.PlacedOrder;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.BadRequestByFtxException;
import com.example.TradeBoot.api.http.IHttpClientWorker;
import com.example.TradeBoot.api.utils.JsonModelConverter;
import com.example.TradeBoot.api.utils.ModifyOrderBuilder;
import com.example.TradeBoot.api.utils.OrderCancellationBuilder;
import com.example.TradeBoot.api.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public interface IOrdersService {
    List<Order> getOpenOrdersBy(String marketName);

    PlacedOrder placeOrder(OrderToPlace order) throws BadRequestByFtxException;

    boolean cancelOrder(String orderId) throws BadRequestByFtxException;

    boolean cancelAllOrder() throws BadRequestByFtxException;

    boolean cancelAllOrderBy(OrderCancellationBuilder builder) throws BadRequestByFtxException;

    Order getOrderStatus(String orderId);

    PlacedOrder modifyOrderBy(ModifyOrderBuilder builder, String orderId) throws BadRequestByFtxException;

    abstract class Abstract implements IOrdersService{
        public void cancelAllOrderByMarketByOne(String marketName) throws BadRequestByFtxException{
            getOpenOrdersBy(marketName).stream()
                    .filter(order -> order.getStatus() != EStatus.CLOSED)
                    .forEach(order -> cancelOrder(order.getId()));

        }
        public PlacedOrder modifyOrderPrice(PlacedOrder placedOrder, BigDecimal price) throws BadRequestByFtxException {

            var builder = new ModifyOrderBuilder()
                    .TargetPrice(price);
            //.TargetSize(placedOrder.getSize());

            return modifyOrderBy(builder, placedOrder.getId());

        }

        public boolean cancelAllOrderByMarket(String marketName) throws BadRequestByFtxException {
            if (Strings.isNullOrEmpty(marketName))
                throw new IllegalArgumentException("marketName");

            OrderCancellationBuilder builder = new OrderCancellationBuilder()
                    .TargetMarket(marketName);

            return cancelAllOrderBy(builder);
        }
    }

    @Service
    class Base extends IOrdersService.Abstract {

        private final IHttpClientWorker httpClient;

        private static final String ORDERS_PATH = "/orders";

        @Autowired
        public Base(IHttpClientWorker httpClient) {
            this.httpClient = httpClient;
        }

        @Override
        public List<Order> getOpenOrdersBy(String marketName) {
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

        @Override
        public PlacedOrder placeOrder(OrderToPlace order) throws BadRequestByFtxException {
            String placeOrderJson = JsonModelConverter.convertModelToJson(order);
            String placedOrderJson = this.httpClient.createPostRequest(placeOrderURI, placeOrderJson);
            return JsonModelConverter.convertJsonToModel(PlacedOrder.class, placedOrderJson);
        }


        @Override
        public boolean cancelOrder(String orderId) throws BadRequestByFtxException {
            if (Strings.isNullOrEmpty(orderId))
                throw new IllegalArgumentException("orderId");

            String uri = UriComponentsBuilder.newInstance()
                    .path(ORDERS_PATH)
                    .path("/").pathSegment(orderId)
                    .toUriString();

            return this.httpClient.createDeleteRequest(uri);
        }

        @Override
        public boolean cancelAllOrder() throws BadRequestByFtxException {
            String uri = UriComponentsBuilder.newInstance()
                    .path(ORDERS_PATH)
                    .toUriString();

            return this.httpClient.createDeleteRequest(uri);
        }

        @Override
        public boolean cancelAllOrderBy(OrderCancellationBuilder builder) throws BadRequestByFtxException {
            String uri = UriComponentsBuilder.newInstance()
                    .path(ORDERS_PATH)
                    .toUriString();

            String body = builder.toString();

            return this.httpClient.createDeleteRequest(uri, body);
        }

        @Override
        public Order getOrderStatus(String orderId) {
            String uri = UriComponentsBuilder.newInstance()
                    .path(ORDERS_PATH)
                    .path("/").path(orderId)
                    .toUriString();

            String json = this.httpClient.createGetRequest(uri);
            return JsonModelConverter.convertJsonToModel(Order.class, json);
        }

        @Override
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
    }

    class Mock extends IOrdersService.Abstract{

        List<Order> placedOrders = new ArrayList<>();

        StringBuilder log = new StringBuilder();

        int index = 0;

        @Override
        public List<Order> getOpenOrdersBy(String marketName) {
            return placedOrders;
        }

        @Override
        public PlacedOrder placeOrder(OrderToPlace order) throws BadRequestByFtxException {

            var currentOrder = new PlacedOrder();
            currentOrder.setMarket(order.getMarket());
            currentOrder.setSide(order.getSide());
            currentOrder.setType(order.getType());
            currentOrder.setSize(order.getSize());
            currentOrder.setId(String.valueOf(index));
            index++;

            log.append("Place order:" + order);
            placedOrders.add(currentOrder);

            return currentOrder;
        }

        @Override
        public boolean cancelOrder(String orderId) throws BadRequestByFtxException {

            log.append("Cancel order by id="+ orderId);
            var orderToDelete = placedOrders.stream().filter(order -> order.getId() == orderId).findFirst();

            var result = false;
            if(orderToDelete.isPresent()){

                placedOrders.remove(orderToDelete.get());
                result = true;
            }
            log.append(" Cancellation status=" +result + "\n");
            return result;
        }

        @Override
        public boolean cancelAllOrder() throws BadRequestByFtxException {
            log.append("Cancel All Orders");

            var result = false;
            if(placedOrders.size() > 0){

                placedOrders.clear();
                result = true;
            }
            log.append(" Cancellation status=" +result + "\n");
            return result;
        }

        @Override
        public boolean cancelAllOrderBy(OrderCancellationBuilder builder) throws BadRequestByFtxException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Order getOrderStatus(String orderId) {
            log.append("Get order status by id=" + orderId + "\n");
            return placedOrders.stream().filter(order -> order.getId() == orderId).findFirst().orElseThrow();
        }

        @Override
        public PlacedOrder modifyOrderBy(ModifyOrderBuilder builder, String orderId) throws BadRequestByFtxException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return log.toString();
        }
    }
}
