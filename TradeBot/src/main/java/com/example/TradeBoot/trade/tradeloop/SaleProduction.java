package com.example.TradeBoot.trade.tradeloop;

import com.example.TradeBoot.api.domain.markets.OrderBook;
import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.api.domain.orders.PlacedOrder;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.BadRequestByFtxException;
import com.example.TradeBoot.api.services.IMarketService;
import com.example.TradeBoot.api.utils.ESideChange;
import com.example.TradeBoot.trade.model.*;
import com.example.TradeBoot.trade.services.ClosePositionInformationService;
import com.example.TradeBoot.trade.services.FinancialInstrumentPositionsService;
import com.example.TradeBoot.trade.services.OrderPriceService;
import com.example.TradeBoot.trade.tradeloop.interfaces.IPlaceOrder;
import com.example.TradeBoot.trade.tradeloop.interfaces.IReplaceOrders;
import com.example.TradeBoot.trade.tradeloop.interfaces.ITradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class SaleProduction implements ITradeService {

    static final Logger log =
            LoggerFactory.getLogger(SaleProduction.class);


    public SaleProduction(FinancialInstrumentPositionsService financialInstrumentPositionsService, ClosePositionInformationService closePositionInformationService, MarketInformation marketInformation, OrderPriceService orderPriceService, IPlaceOrder placeOrder, IMarketService marketService, WorkStatus globalWorkStatus, IReplaceOrders replaceOrders) {
        this.financialInstrumentPositionsService = financialInstrumentPositionsService;
        this.closePositionInformationService = closePositionInformationService;
        this.marketInformation = marketInformation;
        this.orderPriceService = orderPriceService;
        this.placeOrder = placeOrder;
        this.marketService = marketService;
        this.globalWorkStatus = globalWorkStatus;
        this.replaceOrders = replaceOrders;
    }

    IMarketService marketService;

    OrderPriceService orderPriceService;

    FinancialInstrumentPositionsService financialInstrumentPositionsService;

    ClosePositionInformationService closePositionInformationService;

    IPlaceOrder placeOrder;

    IReplaceOrders replaceOrders;

    MarketInformation marketInformation;

    WorkStatus globalWorkStatus;

    long maxWorkTime = 6000;

    Persent maximumDefinition = new Persent(0);

    @Override
    public boolean trade() {
        var orderBook = getOrderBook();

        var positionSize = financialInstrumentPositionsService.getPositionNetSize(marketInformation.market());

        if (financialInstrumentPositionsService.isPositionOpen(positionSize) == false){
            return false;
        }

        sleep(2000);

        log.debug("Position opened");

        var closePositionTradeInformation = closePositionInformationService
                .createTradeInformation(positionSize);

        Map<OrderInformation, OrderToPlace> ordersToPlace = getOrdersToPlace(orderBook, closePositionTradeInformation.get().getOrderInformations());

        log.debug("Start place position order as " + ordersToPlace.values().stream().collect(Collectors.toList()));

        Map<OrderInformation, PlacedOrder> placedOrders = placeOrders(ordersToPlace);


        long startTradeTime = System.currentTimeMillis();
        long currentTradeTime = startTradeTime;
        long startIterationTime = startTradeTime;


        while (financialInstrumentPositionsService.isPositionOpen(positionSize) == true
                && globalWorkStatus.isNeedStop() == false) {

            Optional<Map<OrderInformation, OrderToPlace>> optionalOrderToPlaces;
            if(currentTradeTime <= maxWorkTime){
                optionalOrderToPlaces = createCorrectOrderToPlace(
                        placedOrders,
                        getOrderBook(),
                        marketInformation.market(),
                        maximumDefinition,
                        positionSize
                );
            }
            else {
                optionalOrderToPlaces = createWorstOrderToPlace(
                        placedOrders,
                        getOrderBook(),
                        marketInformation.market(),
                        maximumDefinition,
                        positionSize
                );
                log.debug("Close at unfavorable price");
            }


            if (optionalOrderToPlaces.isPresent()) {
                log.debug("Replacing orders");
                placedOrders = replaceOrders.replace(placedOrders, optionalOrderToPlaces.get());

            }
            long workTime = (System.currentTimeMillis() - startIterationTime);
            long currentSleepTime = marketInformation.tradingDelay() - workTime;

            if (currentSleepTime > 0) {
                sleep(currentSleepTime);
            }

            positionSize = financialInstrumentPositionsService.getPositionNetSize(marketInformation.market());
            startIterationTime = System.currentTimeMillis();
            currentTradeTime = startIterationTime - startTradeTime;
        }
        log.debug("Position closed");
        return true;
    }

    private Optional<Map<OrderInformation, OrderToPlace>> createCorrectOrderToPlace(
            Map<OrderInformation, PlacedOrder> orderInformationPlacedOrderMap,
            OrderBook orderBook,
            String market,
            Persent maximumDiviantion,
            BigDecimal positionSize) {

        var firstPair = orderInformationPlacedOrderMap.entrySet()
                .stream()
                .findFirst()
                .orElseThrow();

        var placedOrder = firstPair.getValue();
        var orderInformation = firstPair.getKey();

        var currentPrice = orderPriceService.calculateCorrectPriceWithIgnoreLower(
                orderBook,
                orderInformation.getDistanceInPercent(),
                placedOrder.getSide(),
                placedOrder.getSize());

        var isPriceInBoarding = orderPriceService.isPriceInBoarding(
                currentPrice,
                placedOrder.getPrice(),
                maximumDiviantion);

        var closePositionTradeInformation = closePositionInformationService
                .createTradeInformation(positionSize);


        if (isPriceInBoarding && closePositionTradeInformation.isEmpty()) return Optional.empty();


        return Optional.of(
                orderPriceService.createOrdersToPlaceMap(
                        orderBook,
                        closePositionTradeInformation.get().getOrderInformations(),
                        market)
        );
    }

    private Optional<Map<OrderInformation, OrderToPlace>> createWorstOrderToPlace(
        Map<OrderInformation, PlacedOrder> orderInformationPlacedOrderMap,
        OrderBook orderBook,
        String market,
        Persent maximumDiviantion,
        BigDecimal positionSize)
    {
        var firstPair = orderInformationPlacedOrderMap.entrySet()
                .stream()
                .findFirst()
                .orElseThrow();

        var placedOrder = firstPair.getValue();
        var orderInformation = firstPair.getKey();

        var currentPrice = orderPriceService.calculateCorrectPrice(
                orderBook,
                orderInformation.getDistanceInPercent(),
                ESideChange.change(closePositionInformationService.getSideBy(positionSize))
                );

        var isPriceInBoarding = orderPriceService.isPriceInBoarding(
                currentPrice,
                placedOrder.getPrice(),
                maximumDiviantion);

        var closePositionTradeInformation = closePositionInformationService
                .createTradeInformation(positionSize);


        if (isPriceInBoarding && closePositionTradeInformation.isEmpty()) return Optional.empty();

        return Optional.of(
                orderPriceService.createOrdersToPlaceMap(
                        orderBook,
                        closePositionTradeInformation.get().getOrderInformations(),
                        market)
        );

    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
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
            placedOrders.put(entryOrderToPlace.getKey(), placeOrder.place(entryOrderToPlace.getValue()));
        }

        return placedOrders;
    }

    private OrderBook getOrderBook() {
        return marketService.getOrderBook(marketInformation.market(), 5);
    }
}
