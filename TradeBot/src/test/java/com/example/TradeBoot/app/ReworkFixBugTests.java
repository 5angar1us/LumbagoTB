package com.example.TradeBoot.app;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.domain.orders.EStatus;
import com.example.TradeBoot.api.domain.orders.Order;
import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.api.services.IMarketService;
import com.example.TradeBoot.api.services.IOrdersService;
import com.example.TradeBoot.configuration.TestServiceInstances;
import com.example.TradeBoot.trade.model.OrderInformation;
import com.example.TradeBoot.trade.model.Persent;
import com.example.TradeBoot.trade.services.OrderPriceService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReworkFixBugTests {

    IOrdersService.Base ordersService;
    IMarketService marketService;

    OrderPriceService orderPriceService;

    @BeforeAll
    public void init(){
            ordersService = TestServiceInstances.getOrdersService();
            marketService = TestServiceInstances.getMarketService();
            orderPriceService = TestServiceInstances.getOrderPriceCalculator();
    }

    @Test
    public void t() throws InterruptedException {
        var markets = new ArrayList<String>();
        markets.add("TONCOIN-PERP");
        markets.add("CELO-PERP");
        markets.add("CEL-PERP");

        List<Order> placed = new ArrayList<>();

        var orders = createOrder(markets.get(2));
        System.out.println(orders.values().size());
        List<Order> statuses = new ArrayList<>();
        do{
            placed.clear();

            for (OrderToPlace value : orders.values()) {
                placed.add(ordersService.placeOrder(value));
                Thread.sleep(1500);
                System.out.println(placed);
            }

            statuses = getOrderStatuses(placed).collect(Collectors.toList());

            statuses.stream().forEach(order -> System.out.println(order.getStatus()));

        } while (anyClosed(statuses.stream()));
        ordersService.cancelOrder(placed.get(0).getId());
    }
    @Test
    public void t2(){
        ordersService.cancelAllOrderByMarket("CEL-PERP");
    }
    private Map<OrderInformation, OrderToPlace> createOrder(String marketName){
        var orderBook = marketService.getOrderBook(marketName, 5);
        var bestBid = orderBook.getBestBid().getPrice();
        var persentDistance = new Persent(3);
        var size = 1L;

        List<OrderInformation> orderInformations = new ArrayList<>();
        orderInformations.add(new OrderInformation(BigDecimal.valueOf(size), ESide.BUY, persentDistance));

        return orderPriceService.createOrdersToPlaceMap(orderBook, orderInformations, marketName);
    }

    private Stream<Order> getOrderStatuses(List<Order> placedOrders) {
        return placedOrders
                .stream()
                .map(placedOrder -> ordersService.getOrderStatus(placedOrder.getId()));
    }

    private boolean anyClosed(Stream<Order> orderStatusStream) {
        return orderStatusStream.anyMatch(orderStatus -> orderStatus.getStatus() == EStatus.CLOSED);
    }

}
