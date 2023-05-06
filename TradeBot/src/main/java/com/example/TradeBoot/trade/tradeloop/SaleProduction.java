package com.example.TradeBoot.trade.tradeloop;

import com.example.TradeBoot.api.domain.markets.OrderBook;
import com.example.TradeBoot.api.domain.orders.EType;
import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.api.domain.orders.PlacedOrder;
import com.example.TradeBoot.api.services.IMarketService;
import com.example.TradeBoot.api.services.IOrdersService;
import com.example.TradeBoot.api.utils.BigDecimalUtils;
import com.example.TradeBoot.trade.model.*;
import com.example.TradeBoot.trade.services.ClosePositionInformationService;
import com.example.TradeBoot.trade.services.FinancialInstrumentPositionsService;
import com.example.TradeBoot.trade.services.OrderPriceService;
import com.example.TradeBoot.trade.tradeloop.interfaces.IReplaceOrder;
import com.example.TradeBoot.trade.tradeloop.interfaces.ITradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

@SuppressWarnings("ALL")
public class SaleProduction implements ITradeService {

    static final Logger log =
            LoggerFactory.getLogger(SaleProduction.class);


    public SaleProduction(IMarketService marketService, IOrdersService ordersService, OrderPriceService orderPriceService, FinancialInstrumentPositionsService financialInstrumentPositionsService, ClosePositionInformationService closePositionInformationService, IReplaceOrder replaceOrder, MarketInformation marketInformation, WorkStatus globalWorkStatus) {
        this.marketService = marketService;
        this.ordersService = ordersService;
        this.orderPriceService = orderPriceService;
        this.financialInstrumentPositionsService = financialInstrumentPositionsService;
        this.closePositionInformationService = closePositionInformationService;
        this.replaceOrder = replaceOrder;
        this.marketInformation = marketInformation;
        this.globalWorkStatus = globalWorkStatus;
    }

    IMarketService marketService;

    IOrdersService ordersService;

    OrderPriceService orderPriceService;

    FinancialInstrumentPositionsService financialInstrumentPositionsService;

    ClosePositionInformationService closePositionInformationService;

    IReplaceOrder replaceOrder;

    MarketInformation marketInformation;

    WorkStatus globalWorkStatus;

    long maxWorkTime = 6000;


    @Override
    public boolean trade() {
        var orderBook = getOrderBook();

        var positionSize = financialInstrumentPositionsService.getPositionNetSize(marketInformation.market());

        if (financialInstrumentPositionsService.isPositionOpen(positionSize) == false) {
            return false;
        }

        sleep(2000);

        log.debug("Position opened");

        var closePositionTradeInformation = closePositionInformationService
                .createTradeInformation(positionSize);

        var orderToPlace = createMostFavorableOrderToPlace(orderBook, closePositionTradeInformation.get());

        log.debug("Start place position order:" + orderToPlace);

        var placedOrder = ordersService.placeOrder(orderToPlace);


        long startTradeTime = System.currentTimeMillis();

        long startIterationTime = startTradeTime;
        boolean isNeedCloseByMostFavorablePrice = true;

        while (financialInstrumentPositionsService.isPositionOpen(positionSize) == true
                && globalWorkStatus.isNeedStop() == false) {

            Optional<OrderToPlace> optionalOrderToPlaces;
            if (isNeedCloseByMostFavorablePrice) {
                optionalOrderToPlaces = createMostFavorableOrderToPlace(
                        placedOrder,
                        getOrderBook(),
                        marketInformation.market(),
                        positionSize
                );
            } else {
                optionalOrderToPlaces = createMarketOrderToPlace(
                        placedOrder,
                        getOrderBook(),
                        marketInformation.market(),
                        positionSize
                );
            }


            if (optionalOrderToPlaces.isPresent()) {
                if(isNeedCloseByMostFavorablePrice){
                    log.debug("Replacing orders by most favorable price");
                }else {
                    log.debug("Replacing orders by market price");
                }
                placedOrder = replaceOrder.replace(placedOrder, optionalOrderToPlaces.get());

            }

            long workTime = (System.currentTimeMillis() - startIterationTime);
            long currentSleepTime = marketInformation.tradingDelay() - workTime;

            if (currentSleepTime > 0) {
                sleep(currentSleepTime);
            }


            startIterationTime = System.currentTimeMillis();

            var currentTradeTime = startIterationTime - startTradeTime;
            isNeedCloseByMostFavorablePrice = currentTradeTime <= maxWorkTime;
            positionSize = financialInstrumentPositionsService.getPositionNetSize(marketInformation.market());
        }
        log.debug("Position closed");
        return true;
    }

    private Optional<OrderToPlace> createMostFavorableOrderToPlace(
            PlacedOrder placedOrder,
            OrderBook orderBook,
            String market,
            BigDecimal positionSize) {

        var currentPrice = orderPriceService.calculateMostFavorablePrice(
                orderBook,
                new Persent(0),
                placedOrder.getSide(),
                placedOrder.getSize());

        return createOrderToPlace(currentPrice, placedOrder.getPrice(), positionSize, market);
    }

    private Optional<OrderToPlace> createMarketOrderToPlace(
            PlacedOrder placedOrder,
            OrderBook orderBook,
            String market,
            BigDecimal positionSize) {

        var currentPrice = orderPriceService.calculateMarketPrice(
                orderBook,
                closePositionInformationService.getSideBy(positionSize)
        );

       return createOrderToPlace(currentPrice, placedOrder.getPrice(), positionSize, market);
    }

    private Optional<OrderToPlace> createOrderToPlace(
            BigDecimal currentPrice,
            BigDecimal placedPrice,
            BigDecimal positionSize,
            String market) {

        var closePositionTradeInformation = closePositionInformationService
                .createTradeInformation(positionSize);

        if (BigDecimalUtils.check(currentPrice, BigDecimalUtils.EOperator.EQUALS, placedPrice) == false
                && closePositionTradeInformation.isEmpty()) {
            return Optional.empty();
        }

        var orderInformation = closePositionTradeInformation.get();

        var orderToPlace = new OrderToPlace(
                market,
                orderInformation.getSide(),
                currentPrice,
                EType.LIMIT,
                orderInformation.getVolume()
        );

        return Optional.of(orderToPlace);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    private OrderToPlace createMostFavorableOrderToPlace(
            OrderBook orderBook,
            OrderInformation orderInformation) {

        var price = orderPriceService.calculateCorrectPrice(orderBook, new Persent(0), orderInformation.getSide());

        var orderToPlace = new OrderToPlace(
                marketInformation.market(),
                orderInformation.getSide(),
                price,
                EType.LIMIT,
                orderInformation.getVolume()
        );

        return orderToPlace;
    }

    private OrderBook getOrderBook() {
        return marketService.getOrderBook(marketInformation.market(), 5);
    }
}
