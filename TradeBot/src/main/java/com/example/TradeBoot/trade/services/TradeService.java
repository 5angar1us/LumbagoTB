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

        Map<OrderInformation, OrderToPlace> ordersToPlace = getPlacedOrders(getOrderBook(), tradeInformation.getOrderInformations());

            log.debug("Start place orders as " + ordersToPlace.values().stream().collect(Collectors.toList()));

        try {

            Map<OrderInformation, PlacedOrder> placedOrders = placeOrders(ordersToPlace);

            long start = System.currentTimeMillis();

            while (positionStatus.getPositionStatus(marketInformation.getMarket()) == false
                    && tradeStatus.isNeedStop() == false) {

                Optional<Map<OrderInformation, OrderToPlace>> optionalOrderToPlaces = createCorrectOrderToPlace(
                        placedOrders,
                        getOrderBook(),
                        marketInformation.getMarket());

                if (optionalOrderToPlaces.isPresent()) {


                    closeOrdersOnMarket(marketInformation.getMarket());


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

            closeOrdersOnMarket(marketInformation.getMarket());

        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } catch (OrderAlreadyClosedException e) {
            CloseOrdersInCatch(e);

        } catch (BadRequestByFtxException e) {

            throw new RuntimeException(e);
        } catch (UnceckedIOException e) {
            CloseOrdersInCatch(e);
        }


    }

    private void CloseOrdersInCatch(Exception e) {
        try {
            Thread.sleep(200);
            log.error(e.getMessage());
            closeOrdersOnMarket(marketInformation.getMarket());
        } catch (InterruptedException interruptedException) {
            throw new RuntimeException(interruptedException);
        } catch (BadRequestByFtxException ex) {
            throw new RuntimeException(ex);
        }
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
            throws BadRequestByFtxException {
        Map<OrderInformation, PlacedOrder> placedOrders = new HashMap<>(orderToPlaces.size());

        for (Map.Entry<OrderInformation, OrderToPlace> entryOrderToPlace : orderToPlaces.entrySet()) {
            placedOrders.put(entryOrderToPlace.getKey(), ordersService.placeOrder(entryOrderToPlace.getValue()));
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

    private void closeOrdersOnMarket(String marketName) throws BadRequestByFtxException {
        ordersService.cancelAllOrderByMarket(marketName);
    }

    private Stream<Order> getOrderStatuses(Map<OrderInformation, PlacedOrder> placedOrders) {
        return placedOrders.values()
                .stream()
                .map(placedOrder -> ordersService.getOrderStatus(placedOrder.getId()));
    }

    private List<Order> getOrders(String marketName) {
        return ordersService.getOpenOrders(marketName).stream()
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

        var orderInformations = new ArrayList<>(orderInformationPlacedOrderMap.keySet());

        return Optional.of(
                orderPriceService.createOrdersToPlaceMap(orderBook, orderInformations, market)
        );
    }

}
