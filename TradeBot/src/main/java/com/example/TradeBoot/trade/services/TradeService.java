package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.api.domain.markets.OrderBook;
import com.example.TradeBoot.api.domain.orders.EStatus;
import com.example.TradeBoot.api.domain.orders.Order;
import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.api.domain.orders.PlacedOrder;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.*;
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

    public TradeService(OrdersService ordersService, IMarketService marketService, OrderPriceService orderPriceService, MarketInformation marketInformation, WorkStatus globalWorkStatus, Logger log) {
        this(ordersService, marketService, orderPriceService, marketInformation, globalWorkStatus);

        this.log = log;
    }

    public TradeService(OrdersService ordersService, IMarketService marketService, OrderPriceService orderPriceService, MarketInformation marketInformation, WorkStatus globalWorkStatus) {
        this(ordersService, marketService, orderPriceService, marketInformation);

        this.globalWorkStatus = globalWorkStatus;
    }

    public TradeService(OrdersService ordersService, IMarketService marketService, OrderPriceService orderPriceService, MarketInformation marketInformation) {
        this.ordersService = ordersService;
        this.marketService = marketService;
        this.orderPriceService = orderPriceService;
        this.marketInformation = marketInformation;

        this.log = defaultLog;
        this.globalWorkStatus = new WorkStatus(false);

        lastRequestTime = System.currentTimeMillis();
    }
    //generall

    private Logger log;
    private OrdersService ordersService;
    private IMarketService marketService;
    private OrderPriceService orderPriceService;

    private MarketInformation marketInformation;

    private WorkStatus globalWorkStatus;

    private long lastRequestTime;

    private final long MINIMUM_DELAY_MS = 105;

    public void runTrade(IPositionStatusService positionStatus, TradeInformation tradeInformation) {
        ETradeState state = ETradeState.TRADE;
        boolean localWorkStatus = true;
        boolean isNeedThrow = false;
        final int maxCloseAttemptsCount = 5;
        int closeAttemptsCount = 1;


        while (localWorkStatus) {
            try {
                switch (state) {
                    case TRADE -> {
                        trade(positionStatus, tradeInformation);
                        log.debug("Close trap orders");
                    }
                    case CLOSE_ORDERS -> {

                        if (closeAttemptsCount <= maxCloseAttemptsCount) {
                            closeAttemptsCount++;
                            log.debug(
                                    String.format("Close attempts %d", --closeAttemptsCount)
                            );

                            ordersService.cancelAllOrderByMarketByOne(marketInformation.market());

                        } else {
                            isNeedThrow = true;
                        }
                        localWorkStatus = false;
                    }
                }
            } catch (UnexpectedErrorException | RetryRequestException e) {
                //4 9
                sleep((long) (closeAttemptsCount * Math.pow(closeAttemptsCount, 2.45)) + 3);

            } catch (OrderAlreadyQueuedForCancellationException e) {
                sleep(closeAttemptsCount * 300);

            } catch (UnceckedIOException | BadRequestByFtxException e) {
                sleep(closeAttemptsCount * 150);

            } catch (UnknownErrorRequestByFtxException e) {
                globalWorkStatus.setNeedStop(true);
                sleep(1000);
            }
            finally {
                state = ETradeState.CLOSE_ORDERS;
            }
        }

        if (isNeedThrow) {
            throw new UnknownErrorRequestByFtxException(closeAttemptsCount + " attempts to close orders ended in failure");
        }
    }

    enum ETradeState {
        TRADE,
        CLOSE_ORDERS,
    }

    private void trade(IPositionStatusService positionStatus, TradeInformation tradeInformation) {
        var orderBook = getOrderBook();

        Map<OrderInformation, OrderToPlace> ordersToPlace = getPlacedOrders(orderBook, tradeInformation.getOrderInformations());

        log.debug("Start place orders as " + ordersToPlace.values().stream().collect(Collectors.toList()));


        Map<OrderInformation, PlacedOrder> placedOrders = placeOrders(ordersToPlace);

        long start = System.currentTimeMillis();

        while (positionStatus.getPositionStatus(marketInformation.market()) == false
                && isRandomOrderClosed(placedOrders) == false
                && globalWorkStatus.isNeedStop() == false) {

            Optional<Map<OrderInformation, OrderToPlace>> optionalOrderToPlaces = createCorrectOrderToPlace(
                    placedOrders,
                    getOrderBook(),
                    marketInformation.market(),
                    marketInformation.maximumDivination()
            );

            if (optionalOrderToPlaces.isPresent()) {
                log.debug("Replacing orders");
                placedOrders = replaceOrder(placedOrders, optionalOrderToPlaces.get());

            }
            long workTime = (System.currentTimeMillis() - start);
            long currentSleepTime = marketInformation.tradingDelay() - workTime;

            if (currentSleepTime > 0) {
                sleep(currentSleepTime);
            }

            start = System.currentTimeMillis();
        }
    }

    private boolean isRandomOrderClosed(Map<OrderInformation, PlacedOrder> placedOrders) {
        var checkedIndex = new Random().nextInt(placedOrders.values().size());
        var orderToCheck = placedOrders.values().stream().skip(checkedIndex).findFirst().orElseThrow();
        var order = ordersService.getOrderStatus(orderToCheck.getId());
        return order.getStatus() == EStatus.CLOSED;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    private OrderBook getOrderBook() {
        return marketService.getOrderBook(marketInformation.market(), 5);
    }

    private Map<OrderInformation, OrderToPlace> getPlacedOrders(
            OrderBook orderBook,
            List<OrderInformation> orderInformations) {
        return orderPriceService.createOrdersToPlaceMap(orderBook, orderInformations, marketInformation.market());
    }

    private Map<OrderInformation, PlacedOrder> replaceOrder(
            Map<OrderInformation, PlacedOrder> placedOrders,
            Map<OrderInformation, OrderToPlace> ordersToPlace) {

        Map<OrderInformation, PlacedOrder> newPlacedOrders = new HashMap<>();

        for (Map.Entry<OrderInformation, PlacedOrder> orderInformationPlacedOrderEntry : placedOrders.entrySet()) {
            var orderInformation = orderInformationPlacedOrderEntry.getKey();
            var placedOrder = orderInformationPlacedOrderEntry.getValue();

            ordersService.cancelOrder(placedOrder.getId());
            var orderToPlace = ordersToPlace.get(orderInformation);
            var newPlacedOrder = placeOrder(orderToPlace);

            newPlacedOrders.put(orderInformation, newPlacedOrder);
        }

        return newPlacedOrders;
    }

    private Map<OrderInformation, PlacedOrder> placeOrders(Map<OrderInformation, OrderToPlace> orderToPlaces)
            throws BadRequestByFtxException {
        Map<OrderInformation, PlacedOrder> placedOrders = new HashMap<>(orderToPlaces.size());

        for (Map.Entry<OrderInformation, OrderToPlace> entryOrderToPlace : orderToPlaces.entrySet()) {
            placedOrders.put(entryOrderToPlace.getKey(), placeOrder(entryOrderToPlace.getValue()));

        }

        return placedOrders;
    }

    private PlacedOrder placeOrder(OrderToPlace order) throws BadRequestByFtxException {

        var currentTime = System.currentTimeMillis();
        var delayBetweenLastRequest = currentTime - lastRequestTime;

        if (delayBetweenLastRequest < MINIMUM_DELAY_MS) {

            var sleepTime = MINIMUM_DELAY_MS - delayBetweenLastRequest;

            sleep(sleepTime);
        }

        var placedOrder = ordersService.placeOrder(order);

        lastRequestTime = System.currentTimeMillis();

        return placedOrder;
    }

    private Optional<Map<OrderInformation, OrderToPlace>> createCorrectOrderToPlace(
            Map<OrderInformation, PlacedOrder> orderInformationPlacedOrderMap,
            OrderBook orderBook,
            String market,
            Persent maximumDiviantion) {

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
