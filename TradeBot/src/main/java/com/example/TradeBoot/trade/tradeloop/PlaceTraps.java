package com.example.TradeBoot.trade.tradeloop;

import com.example.TradeBoot.api.domain.markets.OrderBook;
import com.example.TradeBoot.api.domain.orders.EStatus;
import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.api.domain.orders.PlacedOrder;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.BadRequestByFtxException;
import com.example.TradeBoot.api.services.IMarketService;
import com.example.TradeBoot.api.services.IOrdersService;
import com.example.TradeBoot.trade.model.*;
import com.example.TradeBoot.trade.services.IPositionStatusService;
import com.example.TradeBoot.trade.services.OrderPriceService;
import com.example.TradeBoot.trade.tradeloop.interfaces.IPlaceOrder;
import com.example.TradeBoot.trade.tradeloop.interfaces.IReplaceOrderMap;
import com.example.TradeBoot.trade.tradeloop.interfaces.ITradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class PlaceTraps implements ITradeService {

    static final Logger log =
            LoggerFactory.getLogger(PlaceTraps.class);


    public PlaceTraps(IOrdersService ordersService, IMarketService marketService, IPositionStatusService positionStatus, OrderPriceService orderPriceService, IReplaceOrderMap replaceOrders, IPlaceOrder placeOrder, TradeInformation tradeInformation, MarketInformation marketInformation, WorkStatus globalWorkStatus) {
        this.ordersService = ordersService;
        this.marketService = marketService;
        this.positionStatus = positionStatus;
        this.orderPriceService = orderPriceService;
        this.replaceOrders = replaceOrders;
        this.tradeInformation = tradeInformation;
        this.marketInformation = marketInformation;
        this.globalWorkStatus = globalWorkStatus;
    }

    IOrdersService ordersService;

    IMarketService marketService;

    IPositionStatusService positionStatus;

    OrderPriceService orderPriceService;

    IReplaceOrderMap replaceOrders;

    TradeInformation tradeInformation;

    MarketInformation marketInformation;
    WorkStatus globalWorkStatus;




    public boolean trade() {
        var orderBook = getOrderBook();

        Map<OrderInformation, OrderToPlace> ordersToPlace = getOrdersToPlace(orderBook, tradeInformation.getOrderInformations());

        log.debug("Start place orders as " + ordersToPlace.values().stream().collect(Collectors.toList()));

        Map<OrderInformation, PlacedOrder> placedOrders = placeOrders(ordersToPlace);

        long startIterationTime = System.currentTimeMillis();

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
                placedOrders = replaceOrders.replace(placedOrders, optionalOrderToPlaces.get());

            }
            long workTime = (System.currentTimeMillis() - startIterationTime);
            long currentSleepTime = marketInformation.tradingDelay() - workTime;

            if (currentSleepTime > 0) {
                sleep(currentSleepTime);
            }

            startIterationTime = System.currentTimeMillis();
        }

        return true;
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

    private Map<OrderInformation, OrderToPlace> getOrdersToPlace(
            OrderBook orderBook,
            List<OrderInformation> orderInformations) {
        return orderPriceService.createOrdersToPlaceMap(orderBook, orderInformations, marketInformation.market());
    }

    private Map<OrderInformation, PlacedOrder> placeOrders(Map<OrderInformation, OrderToPlace> orderToPlaces)
            throws BadRequestByFtxException {
        Map<OrderInformation, PlacedOrder> placedOrders = new HashMap<>(orderToPlaces.size());

        for (Map.Entry<OrderInformation, OrderToPlace> entryOrderToPlace : orderToPlaces.entrySet()) {
            placedOrders.put(entryOrderToPlace.getKey(), ordersService.placeOrder(entryOrderToPlace.getValue()));

        }

        return placedOrders;
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
