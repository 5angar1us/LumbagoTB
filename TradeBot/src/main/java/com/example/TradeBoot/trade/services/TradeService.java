package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.api.domain.markets.OrderBook;
import com.example.TradeBoot.api.domain.orders.EStatus;
import com.example.TradeBoot.api.domain.orders.Order;
import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.api.domain.orders.PlacedOrder;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.BadRequestByFtxException;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.UnceckedIOException;
import com.example.TradeBoot.api.services.IMarketService;
import com.example.TradeBoot.api.services.OrdersService;
import com.example.TradeBoot.trade.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TradeService {

    static final Logger defaultLog =
            LoggerFactory.getLogger(TradeService.class);

    public TradeService(OrdersService ordersService, IMarketService marketService, OrderPriceService orderPriceService, MarketInformation marketInformation, Persent maximumDiviantion, TradeStatus tradeStatus, Logger log, FinancialInstrumentPositionsService financialInstrumentPositionsService) {
        this(ordersService, marketService, orderPriceService, marketInformation, maximumDiviantion, financialInstrumentPositionsService, tradeStatus);

        this.log = log;
    }

    public TradeService(OrdersService ordersService, IMarketService marketService, OrderPriceService orderPriceService, MarketInformation marketInformation, Persent maximumDiviantion, FinancialInstrumentPositionsService financialInstrumentPositionsService, TradeStatus tradeStatus) {
        this(ordersService, marketService, orderPriceService, marketInformation, maximumDiviantion, financialInstrumentPositionsService);

        this.tradeStatus = tradeStatus;
    }

    public TradeService(OrdersService ordersService, IMarketService marketService, OrderPriceService orderPriceService, MarketInformation marketInformation, Persent maximumDiviantion, FinancialInstrumentPositionsService financialInstrumentPositionsService) {
        this.ordersService = ordersService;
        this.marketService = marketService;
        this.orderPriceService = orderPriceService;
        this.marketInformation = marketInformation;
        this.maximumDiviantion = maximumDiviantion;
        this.financialInstrumentPositionsService = financialInstrumentPositionsService;

        this.log = defaultLog;
        this.tradeStatus = new TradeStatus(false);

        lastRequestTime = System.currentTimeMillis();
    }
    //generall

    private Logger log;
    private OrdersService ordersService;
    private IMarketService marketService;
    private OrderPriceService orderPriceService;

    private MarketInformation marketInformation;

    private Persent maximumDiviantion;

    private FinancialInstrumentPositionsService financialInstrumentPositionsService;
    private TradeStatus tradeStatus;

    private long lastRequestTime;

    private final long MINIMUM_DELAY_MS = 105;

    public void trade(IPositionStatusService positionStatus, TradeInformation tradeInformation) {
        try {
            var orderBook = getOrderBook();

            Map<OrderInformation, OrderToPlace> ordersToPlace = getPlacedOrders(orderBook, tradeInformation.getOrderInformations());

            log.debug("Start place orders as " + ordersToPlace.values().stream().collect(Collectors.toList()));


            Map<OrderInformation, PlacedOrder> placedOrders = placeOrders(ordersToPlace);

            long start = System.currentTimeMillis();

            while (positionStatus.getPositionStatus(marketInformation.getMarket()) == false
                    && isFirstOrderClosed(placedOrders) == false
                    && tradeStatus.isNeedStop() == false) {

                Optional<Map<OrderInformation, OrderToPlace>> optionalOrderToPlaces = createCorrectOrderToPlace(
                        placedOrders,
                        getOrderBook(),
                        marketInformation.getMarket());

                if (optionalOrderToPlaces.isPresent()) {


                    closeAllOrdersOnMarket(marketInformation.getMarket());

                    Thread.sleep(160);

                    placedOrders = placeOrders(optionalOrderToPlaces.get());

                }

                long workTime = (System.currentTimeMillis() - start);
                long currentSleepTime = marketInformation.getTradingDelay() - workTime;

                if (currentSleepTime > 0) {
                    Thread.sleep(currentSleepTime);
                }

                start = System.currentTimeMillis();
            }

            log.debug("Close trap orders");

            closeAllOrdersOnMarket(marketInformation.getMarket());

        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } catch (UnceckedIOException e) {

        } catch (BadRequestByFtxException e) {
            log.error(e.getMessage());

        } finally {
            for (var i = 0; tryCloseAllOrdersInMarket() == false || i > 5; i++) { }
        }
    }

    private boolean isFirstOrderClosed(Map<OrderInformation, PlacedOrder> placedOrders) {
        var firstOrder = placedOrders.values().stream().findFirst().orElseThrow();
        var order = ordersService.getOrderStatus(firstOrder.getId());
        return order.getStatus() == EStatus.CLOSED;
    }

    private boolean tryCloseAllOrdersInMarket() {
        boolean isSuccess = true;

        try {
            Thread.sleep(150);
            closeAllOrdersOnMarket(marketInformation.getMarket());
            Thread.sleep(150);

        } catch (InterruptedException | BadRequestByFtxException e) {
            log.error(e.getMessage());
            isSuccess = false;
        }
        return isSuccess;
    }

    private OrderBook getOrderBook() {
        return marketService.getOrderBook(marketInformation.getMarket(), 5);
    }

    private Map<OrderInformation, OrderToPlace> getPlacedOrders(
            OrderBook orderBook,
            List<OrderInformation> orderInformations) {
        return orderPriceService.createOrdersToPlaceMap(orderBook, orderInformations, marketInformation.getMarket());
    }

    private Map<OrderInformation, PlacedOrder> placeOrders(Map<OrderInformation, OrderToPlace> orderToPlaces)
            throws BadRequestByFtxException, InterruptedException {
        Map<OrderInformation, PlacedOrder> placedOrders = new HashMap<>(orderToPlaces.size());

        for (Map.Entry<OrderInformation, OrderToPlace> entryOrderToPlace : orderToPlaces.entrySet()) {
            placedOrders.put(entryOrderToPlace.getKey(), placeOrder(entryOrderToPlace.getValue()));

        }

        return placedOrders;
    }

    private PlacedOrder placeOrder(OrderToPlace order) throws BadRequestByFtxException, InterruptedException {

        var currentTime = System.currentTimeMillis();
        var delayBetweenLastRequest = currentTime - lastRequestTime;

        if (delayBetweenLastRequest < MINIMUM_DELAY_MS) {

            var sleepTime = MINIMUM_DELAY_MS - delayBetweenLastRequest;

            Thread.sleep(sleepTime);
        }

        var placedOrder = ordersService.placeOrder(order);

        lastRequestTime = System.currentTimeMillis();

        return placedOrder;
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

    private void closeAllOrdersOnMarket(String marketName) throws BadRequestByFtxException {
        ordersService.cancelAllOrderByMarket(marketName);
    }

    private Stream<Order> getOrderStatuses(Map<OrderInformation, PlacedOrder> placedOrders) {
        return placedOrders.values()
                .stream()
                .map(placedOrder -> ordersService.getOrderStatus(placedOrder.getId()));
    }

    private List<Order> getOrders(String marketName) {
        return ordersService.getOpenOrdersBy(marketName).stream()
                .filter(openOrder -> openOrder.getStatus() != EStatus.CLOSED)
                .collect(Collectors.toList());
    }

    private boolean anyClosed(Stream<Order> orderStatusStream) {
        return orderStatusStream.anyMatch(orderStatus -> orderStatus.getStatus() == EStatus.CLOSED);
    }

    private Optional<Map<OrderInformation, OrderToPlace>> createCorrectOrderToPlace(
            Map<OrderInformation, PlacedOrder> orderInformationPlacedOrderMap,
            OrderBook orderBook,
            String market) {

        var firstPair = orderInformationPlacedOrderMap.entrySet()
                .stream()
                .findFirst()
                .orElseThrow();

        var placedOrder = firstPair.getValue();
        var orderInformation = firstPair.getKey();

        var currentPrice = orderPriceService.calculateCorrectPrice(
                orderBook,
                orderInformation.getDistanceInPercent(),
                placedOrder.getSide());

        var isPriceInBoarding = orderPriceService.isPriceInBoarding(
                currentPrice,
                placedOrder.getPrice(),
                maximumDiviantion);

        if (isPriceInBoarding) return Optional.empty();

        var orderInformations = new ArrayList<>(orderInformationPlacedOrderMap.keySet());

        return Optional.of(
                orderPriceService.createOrdersToPlaceMap(orderBook, orderInformations, market)
        );
    }

}
