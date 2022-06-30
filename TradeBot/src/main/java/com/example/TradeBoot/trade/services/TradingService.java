package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.api.domain.markets.OrderBook;
import com.example.TradeBoot.api.domain.orders.EStatus;
import com.example.TradeBoot.api.domain.orders.OrderStatus;
import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.api.domain.orders.PlacedOrder;
import com.example.TradeBoot.api.extentions.BadImportantRequestByFtxException;
import com.example.TradeBoot.api.services.MarketService;
import com.example.TradeBoot.api.services.OrdersService;
import com.example.TradeBoot.trade.model.*;
import com.example.TradeBoot.trade.calculator.OrderPriceCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TradingService {

    static final Logger defaultLog =
            LoggerFactory.getLogger(TradingService.class);

    public TradingService(OrdersService ordersService, MarketService marketService, OrderPriceCalculator orderPriceCalculator, MarketInformation marketInformation, Persent maximumDiviantion, Logger log , TradeStatus tradeStatus) {
        this.ordersService = ordersService;
        this.marketService = marketService;
        this.orderPriceCalculator = orderPriceCalculator;
        this.marketInformation = marketInformation;
        this.maximumDiviantion = maximumDiviantion;
        this.log = log;
        this.tradeStatus = tradeStatus;
    }
    public TradingService(OrdersService ordersService, MarketService marketService, OrderPriceCalculator orderPriceCalculator, MarketInformation marketInformation, Persent maximumDiviantion) {
        this.ordersService = ordersService;
        this.marketService = marketService;
        this.orderPriceCalculator = orderPriceCalculator;
        this.marketInformation = marketInformation;
        this.maximumDiviantion = maximumDiviantion;
        this.log = defaultLog;
    }
    //generall

    private Logger log;
    private OrdersService ordersService;
    private MarketService marketService;
    private OrderPriceCalculator orderPriceCalculator;

    private MarketInformation marketInformation;

    private Persent maximumDiviantion;
    private TradeStatus tradeStatus;

    public void workWithOrders(TradeInformation tradeInformation) {
        Map<OrderInformation, OrderToPlace> ordersToPlace = getPlacedOrders(getOrderBook(), tradeInformation.getOrderInformations());
        log.info("Start place orders as ", ordersToPlace.values());
        try {
            log.info("Place orders in market" + marketInformation.getMarket());
            Map<OrderInformation, PlacedOrder> placedOrders = placeOrders(ordersToPlace);

            while (anyClosed(getOrderStatuses(placedOrders)) == false && tradeStatus.isNeedStop() == false) {

                Optional<Map<OrderInformation, OrderToPlace>> optionalOrderToPlaces = optionalCreateCorrectOrderToPlace(
                        placedOrders,
                        getOrderBook(),
                        marketInformation.getMarket());

                if (optionalOrderToPlaces.isPresent()) {

                    log.info("Close orders to change price in " + marketInformation.getMarket());
                    closeOrders(placedOrders);
                    log.info("Place orders in market " + marketInformation.getMarket() + " as " + optionalOrderToPlaces.get());
                    placedOrders = placeOrders(optionalOrderToPlaces.get());
                }
                log.info("Sleep");
                Thread.sleep(marketInformation.getTradingDelay());
            }

            log.info("Close trap orders in market " + marketInformation.getMarket());
            closeOrders(notClosedOrders(getOrderStatuses(placedOrders)));

        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } catch (BadImportantRequestByFtxException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }
    private OrderBook getOrderBook(){
        return marketService.getOrderBook(marketInformation.getMarket(), 5);
    }

    private Map<OrderInformation, OrderToPlace> getPlacedOrders(
            OrderBook orderBook,
            List<OrderInformation> orderInformations) {
        return orderPriceCalculator.createOrdersToPlaceMap(orderBook, new HashSet<>(orderInformations), marketInformation.getMarket());
    }

    private Map<OrderInformation, PlacedOrder> placeOrders(Map<OrderInformation, OrderToPlace> orderToPlaces)
            throws BadImportantRequestByFtxException {
        Map<OrderInformation, PlacedOrder> placedOrders = new HashMap<>(orderToPlaces.size());

        for (Map.Entry<OrderInformation, OrderToPlace> entryOrderToPlace : orderToPlaces.entrySet()) {
            placedOrders.put(entryOrderToPlace.getKey(), ordersService.placeOrder(entryOrderToPlace.getValue()));
        }

        return placedOrders;
    }

    private List<OrderStatus> notClosedOrders(Stream<OrderStatus> orderStatusStream) throws BadImportantRequestByFtxException {
        return orderStatusStream
                 .filter(orderStatus -> orderStatus.getStatus() != EStatus.CLOSED)
                 .collect(Collectors.toList());
    }

    private void closeOrders(Map<OrderInformation, PlacedOrder> placedOrders)
            throws BadImportantRequestByFtxException {
        for (PlacedOrder placedOrder : placedOrders.values()) {
            ordersService.cancelOrder(placedOrder.getId());
        }
    }

    private void closeOrders(List<OrderStatus> orders)
            throws BadImportantRequestByFtxException {
        for (OrderStatus placedOrder : orders) {
            ordersService.cancelOrder(placedOrder.getId());
        }
    }

    private Stream<OrderStatus> getOrderStatuses(Map<OrderInformation, PlacedOrder> placedOrders) {
        return placedOrders.values()
                .stream()
                .map(placedOrder -> ordersService.getOrderStatus(placedOrder.getId()));
    }

    private boolean anyClosed(Stream<OrderStatus> orderStatusStream) {
        return orderStatusStream.anyMatch(orderStatus -> orderStatus.getStatus() == EStatus.CLOSED);
    }

    private Optional<Map<OrderInformation, OrderToPlace>> optionalCreateCorrectOrderToPlace(
            Map<OrderInformation, PlacedOrder> orderInformationPlacedOrderMap,
            OrderBook orderBook,
            String market) {

        var pair = orderInformationPlacedOrderMap.entrySet()
                .stream()
                .findFirst()
                .orElseThrow();

        var placedOrder = pair.getValue();
        var orderInformation = pair.getKey();

        var currentPrice = orderPriceCalculator.calculateCorrectPrice(
                orderBook,
                orderInformation.getDistanceInPercent(),
                placedOrder.getSide());

        var isPriceInBoarding = orderPriceCalculator.isPriceInBoarding(
                currentPrice,
                placedOrder.getPrice(),
                maximumDiviantion);

        if (isPriceInBoarding) return Optional.empty();

        Set<OrderInformation> orderInformations = orderInformationPlacedOrderMap.keySet();

        return Optional.of(
                orderPriceCalculator.createOrdersToPlaceMap(orderBook, orderInformations, market)
        );
    }

}
