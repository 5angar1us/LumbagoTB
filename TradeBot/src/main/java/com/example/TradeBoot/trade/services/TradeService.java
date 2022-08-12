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

    public TradeService(OrdersService ordersService, IMarketService marketService, OrderPriceService orderPriceService, MarketInformation marketInformation, WorkStatus workStatus, Logger log) {
        this(ordersService, marketService, orderPriceService, marketInformation, workStatus);

        this.log = log;
    }

    public TradeService(OrdersService ordersService, IMarketService marketService, OrderPriceService orderPriceService, MarketInformation marketInformation, WorkStatus workStatus) {
        this(ordersService, marketService, orderPriceService, marketInformation);

        this.workStatus = workStatus;
    }

    public TradeService(OrdersService ordersService, IMarketService marketService, OrderPriceService orderPriceService, MarketInformation marketInformation) {
        this.ordersService = ordersService;
        this.marketService = marketService;
        this.orderPriceService = orderPriceService;
        this.marketInformation = marketInformation;

        this.log = defaultLog;
        this.workStatus = new WorkStatus(false);

        lastRequestTime = System.currentTimeMillis();
    }
    //generall

    private Logger log;
    private OrdersService ordersService;
    private IMarketService marketService;
    private OrderPriceService orderPriceService;

    private MarketInformation marketInformation;

    private WorkStatus workStatus;

    private long lastRequestTime;

    private final long MINIMUM_DELAY_MS = 105;

    public void trade(IPositionStatusService positionStatus, TradeInformation tradeInformation) {
        try {
            var orderBook = getOrderBook();

            Map<OrderInformation, OrderToPlace> ordersToPlace = getPlacedOrders(orderBook, tradeInformation.getOrderInformations());

            log.debug("Start place orders as " + ordersToPlace.values().stream().collect(Collectors.toList()));


            Map<OrderInformation, PlacedOrder> placedOrders = placeOrders(ordersToPlace);

            long start = System.currentTimeMillis();

            while (positionStatus.getPositionStatus(marketInformation.market()) == false
                    && isRandomOrderClosed(placedOrders) == false
                    && workStatus.isNeedStop() == false) {

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
                    Thread.sleep(currentSleepTime);
                }

                start = System.currentTimeMillis();
            }

            log.debug("Close trap orders");


        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);

        } catch (UnceckedIOException e) {
            sleep(200);

        } catch (OrderAlreadyQueuedForCancellationException e) {
            sleep(500);

        } catch (RetryRequestException e){
            sleep(2000);
        }
        catch (BadRequestByFtxException e) {

        } catch (UnknownErrorRequestByFtxException e) {
            workStatus.setNeedStop(true);
            sleep(1000);

        } finally {
            closeOrdersOrThrow();
        }
    }

    private void closeOrdersOrThrow() {
        final int maxCloseAttemptsCount = 4;

        var closeAttemptsCount = 0;

        boolean isSuccessCloseAllOrdersInMarket = false;

        do {
            closeAttemptsCount++;
            log.debug("Close attempts " + closeAttemptsCount);
            isSuccessCloseAllOrdersInMarket = tryCloseAllOrdersInMarket(closeAttemptsCount);

        } while (isSuccessCloseAllOrdersInMarket == false && closeAttemptsCount < maxCloseAttemptsCount);

        if (isSuccessCloseAllOrdersInMarket == false) {
            throw new UnknownErrorRequestByFtxException(maxCloseAttemptsCount + " attempts to close orders ended in failure");
        }
    }

    private boolean isRandomOrderClosed(Map<OrderInformation, PlacedOrder> placedOrders) {
        var checkedIndex = new Random().nextInt(placedOrders.values().size());
        var orderToCheck = placedOrders.values().stream().skip(checkedIndex).findFirst().orElseThrow();
        var order = ordersService.getOrderStatus(orderToCheck.getId());
        return order.getStatus() == EStatus.CLOSED;
    }

    private boolean tryCloseAllOrdersInMarket(int closeAttemptsCount) {
        boolean isSuccess = true;

        try {

            ordersService.cancelAllOrderByMarketByOne(marketInformation.market());

        }catch (UnexpectedErrorException e){
            sleep((long)(closeAttemptsCount * Math.pow(closeAttemptsCount, 2.45)) + 3);
        }
        catch (BadRequestByFtxException | UnceckedIOException e) {
            sleep(closeAttemptsCount * 150);
            isSuccess = false;
        } catch (UnknownErrorRequestByFtxException e) {
            workStatus.setNeedStop(true);
            sleep(1000);
            isSuccess = false;
        }
        return isSuccess;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
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
            Map<OrderInformation, OrderToPlace> ordersToPlace) throws InterruptedException {

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
