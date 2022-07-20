package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.domain.markets.OrderBook;
import com.example.TradeBoot.api.domain.orders.*;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Checked.BadRequestByFtxException;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Checked.OrderAlreadyClosedException;
import com.example.TradeBoot.api.services.IMarketService;
import com.example.TradeBoot.api.services.OrdersService;
import com.example.TradeBoot.trade.calculator.OrderPriceCalculator;
import com.example.TradeBoot.trade.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TradingService {

    static final Logger defaultLog =
            LoggerFactory.getLogger(TradingService.class);

    public TradingService(OrdersService ordersService, IMarketService marketService, OrderPriceCalculator orderPriceCalculator, MarketInformation marketInformation, Persent maximumDiviantion, TradeStatus tradeStatus, Logger log) {
        this(ordersService, marketService, orderPriceCalculator, marketInformation, maximumDiviantion, tradeStatus);

        this.log = log;
    }

    public TradingService(OrdersService ordersService, IMarketService marketService, OrderPriceCalculator orderPriceCalculator, MarketInformation marketInformation, Persent maximumDiviantion, TradeStatus tradeStatus) {
        this(ordersService, marketService, orderPriceCalculator, marketInformation, maximumDiviantion);

        this.tradeStatus = tradeStatus;
    }

    public TradingService(OrdersService ordersService, IMarketService marketService, OrderPriceCalculator orderPriceCalculator, MarketInformation marketInformation, Persent maximumDiviantion) {
        this.ordersService = ordersService;
        this.marketService = marketService;
        this.orderPriceCalculator = orderPriceCalculator;
        this.marketInformation = marketInformation;
        this.maximumDiviantion = maximumDiviantion;

        this.log = defaultLog;
        this.tradeStatus = new TradeStatus(false);
    }
    //generall

    private Logger log;
    private OrdersService ordersService;
    private IMarketService marketService;
    private OrderPriceCalculator orderPriceCalculator;

    private MarketInformation marketInformation;

    private Persent maximumDiviantion;
    private TradeStatus tradeStatus;

    public void workWithOrders(TradeInformation tradeInformation) {
        Map<OrderInformation, OrderToPlace> ordersToPlace = getPlacedOrders(getOrderBook(), tradeInformation.getOrderInformations());
        log.debug("Start place orders as ", ordersToPlace.values());
        try {
            log.debug("Place orders in market" + marketInformation.getMarket());
            Map<OrderInformation, PlacedOrder> placedOrders = placeOrders(ordersToPlace);

            long start = System.currentTimeMillis();
            while (anyClosed(getOrderStatuses(placedOrders)) == false && tradeStatus.isNeedStop() == false) {

                Optional<Map<OrderInformation, OrderToPlace>> optionalOrderToPlaces = optionalCreateCorrectOrderToPlace(
                        placedOrders,
                        getOrderBook(),
                        marketInformation.getMarket());

                if (optionalOrderToPlaces.isPresent()) {

                    log.debug("Close orders to change price in " + marketInformation.getMarket());
                    closeOrders(placedOrders);
                    log.debug("Place orders in market " + marketInformation.getMarket() + " as " + optionalOrderToPlaces.get());
                    placedOrders = placeOrders(optionalOrderToPlaces.get());
                }
                long workTime = (System.currentTimeMillis() - start);
                long currentSlepTime = marketInformation.getTradingDelay() - workTime;


                if (currentSlepTime > 0) {
                    log.debug("Sleep" + currentSlepTime);
                    Thread.sleep(currentSlepTime);
                }

                start = System.currentTimeMillis();
            }

            log.debug("Close trap orders in market " + marketInformation.getMarket());
            closeOrders(notClosedOrders(getOrderStatuses(placedOrders)));

        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } catch (OrderAlreadyClosedException e) {
            log.error(e.getMessage());
            try {
                closeOrders(getOrdersBySide(marketInformation.getMarket(), tradeInformation.getBaseSide()));
            } catch (OrderAlreadyClosedException ex) {
                throw new RuntimeException(ex);
            } catch (BadRequestByFtxException ex) {
                throw new RuntimeException(ex);
            }

        } catch (BadRequestByFtxException e) {

            throw new RuntimeException(e);
        }

    }

    private OrderBook getOrderBook() {
        return marketService.getOrderBook(marketInformation.getMarket(), 5);
    }

    private Map<OrderInformation, OrderToPlace> getPlacedOrders(
            OrderBook orderBook,
            List<OrderInformation> orderInformations) {
        return orderPriceCalculator.createOrdersToPlaceMap(orderBook, new HashSet<>(orderInformations), marketInformation.getMarket());
    }

    private Map<OrderInformation, PlacedOrder> placeOrders(Map<OrderInformation, OrderToPlace> orderToPlaces)
            throws BadRequestByFtxException {
        Map<OrderInformation, PlacedOrder> placedOrders = new HashMap<>(orderToPlaces.size());

        for (Map.Entry<OrderInformation, OrderToPlace> entryOrderToPlace : orderToPlaces.entrySet()) {
            placedOrders.put(entryOrderToPlace.getKey(), ordersService.placeOrder(entryOrderToPlace.getValue()));
        }

        return placedOrders;
    }

    private List<Order> notClosedOrders(Stream<Order> orderStatusStream) {
        return orderStatusStream
                .filter(orderStatus -> orderStatus.getStatus() != EStatus.CLOSED)
                .collect(Collectors.toList());
    }

    private void closeOrders(Map<OrderInformation, PlacedOrder> placedOrders)
            throws BadRequestByFtxException {
        for (PlacedOrder placedOrder : placedOrders.values()) {
            ordersService.cancelOrder(placedOrder.getId());
        }
    }

    private void closeOrders(List<Order> orders)
            throws BadRequestByFtxException {
        for (Order placedOrder : orders) {
            ordersService.cancelOrder(placedOrder.getId());
        }
    }

//    private void closeOrders(List<OpenOrder> orders)
//            throws BadRequestByFtxException {
//        for (OpenOrder placedOrder : orders) {
//            ordersService.cancelOrder(placedOrder.getId());
//        }
//    }

    private Stream<Order> getOrderStatuses(Map<OrderInformation, PlacedOrder> placedOrders) {
        return placedOrders.values()
                .stream()
                .map(placedOrder -> ordersService.getOrderStatus(placedOrder.getId()));
    }

    private List<Order> getOrdersBySide(String marketName, ESide side) {
        return ordersService.getOpenOrders(marketName).stream()
                .filter(openOrder -> openOrder.getSide() == side)
                .collect(Collectors.toList());
    }

    private boolean anyClosed(Stream<Order> orderStatusStream) {
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
