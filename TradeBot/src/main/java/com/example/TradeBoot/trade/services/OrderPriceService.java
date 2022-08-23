package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.domain.markets.OrderBook;
import com.example.TradeBoot.api.domain.markets.OrderBookLine;
import com.example.TradeBoot.api.domain.orders.EType;
import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.api.utils.BigDecimalUtils;
import com.example.TradeBoot.api.utils.ESideChange;
import com.example.TradeBoot.trade.model.OrderInformation;
import com.example.TradeBoot.trade.model.Persent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderPriceService {


    static final Logger log =
            LoggerFactory.getLogger(OrderPriceService.class);

    public OrderPriceService() {
    }


    public Map<OrderInformation, OrderToPlace> createWorstOrdersToPlaceMap(OrderBook orderBook, List<OrderInformation> orderInformations, String market) {

        Map<OrderInformation, OrderToPlace> orderToPlaces = new HashMap<>();
        for (OrderInformation orderInformation : orderInformations) {
            BigDecimal price = calculateMarketPrice(
                    orderBook,
                    orderInformation.getSide()
            );

            orderToPlaces.put(
                    orderInformation,
                    new OrderToPlace(
                            market,
                            orderInformation.getSide(),
                            price,
                            EType.LIMIT,
                            orderInformation.getVolume()
                    ));
        }
        return orderToPlaces;

    }

    public Map<OrderInformation, OrderToPlace> createOrdersToPlaceMap(
            OrderBook orderBook,
            List<OrderInformation> orderInformations,
            String market) {

        Map<OrderInformation, OrderToPlace> orderToPlaces = new HashMap<>();
        for (OrderInformation orderInformation : orderInformations) {
            BigDecimal price = calculateCorrectPrice(
                    orderBook,
                    orderInformation.getDistanceInPercent(),
                    orderInformation.getSide()
            );

            orderToPlaces.put(
                    orderInformation,
                    new OrderToPlace(
                            market,
                            orderInformation.getSide(),
                            price,
                            EType.LIMIT,
                            orderInformation.getVolume()
                    ));
        }
        return orderToPlaces;
    }

    public boolean isPriceInBoarding(BigDecimal currentPrice, BigDecimal placedPrice, Persent maximumDeviation) {
        var topBoarding = targetPriceHigher(currentPrice, maximumDeviation);
        var bottomBoarding = targetPriceLower(currentPrice, maximumDeviation);

        var isLessOrEqualTopBoarding = BigDecimalUtils.check(
                topBoarding,
                BigDecimalUtils.EOperator.GREATER_THAN_OR_EQUALS,
                placedPrice
        );

        var isMoreOrEqualsBottomBoarding = BigDecimalUtils.check(
                bottomBoarding,
                BigDecimalUtils.EOperator.LESS_THAN_OR_EQUALS,
                placedPrice
        );

        log.debug("bottomBording " + bottomBoarding + " currentPrice " + currentPrice + " topBording " + topBoarding + " placedPrice " + placedPrice);
        log.debug("isLessOrEqualTopBoarding " + isLessOrEqualTopBoarding + " " + " isMoreOrEqualsBottomBoarding " + isMoreOrEqualsBottomBoarding);
        return isLessOrEqualTopBoarding && isMoreOrEqualsBottomBoarding;
    }

    public BigDecimal calculateMarketPrice(OrderBook orderBook, ESide side) {
        log.debug("Using market");
        return createCorrectPrice(new Persent(0), side, orderBook.getBestBySide(ESideChange.change(side)).getPrice());
    }

    public BigDecimal calculateCorrectPrice(OrderBook orderBook, Persent distance, ESide side) {
        log.debug("Using best target");
        return createCorrectPrice(distance, side, orderBook.getBestBySide(side).getPrice());
    }

    public BigDecimal calculateMostFavorablePrice(OrderBook orderBook, Persent distanceInPercent, ESide side, BigDecimal placedVolume) {
        log.debug("Using a most favorable");

        BigDecimal price = getMostFavorablePrice(orderBook.getAllBySide(side), placedVolume);

        return createCorrectPrice(distanceInPercent, side, price);
    }

    private BigDecimal getMostFavorablePrice(List<OrderBookLine> OrderBookLines, BigDecimal placedVolume) {
        var index = 0;
        BigDecimal accamulatedVolume = BigDecimal.ZERO;
        var maxIgnoredVolume = placedVolume.divide(new BigDecimal(2));
        log.debug("maxIgnoredVolume: " + maxIgnoredVolume );

        for (OrderBookLine orderBookLine : OrderBookLines) {
            accamulatedVolume = accamulatedVolume.add(orderBookLine.getVolume());
            index++;

            log.debug("accamulatedVolume: " + accamulatedVolume);
            log.debug("index: "+ index);
            if (BigDecimalUtils.check(accamulatedVolume, BigDecimalUtils.EOperator.GREATER_THAN_OR_EQUALS, maxIgnoredVolume)) {
                log.debug("break");
                break;
            }

        }
        return OrderBookLines.get(--index).getPrice();
    }

    private BigDecimal createCorrectPrice(Persent distance, ESide side, BigDecimal price) {
        return switch (side) {
            case BUY -> {

                log.debug("targetBid:" + price);

                yield targetPriceLower(
                        price,
                        distance);

            }

            case SELL -> {
                log.debug("targetAsk:" + price);

                yield targetPriceHigher(
                        price,
                        distance);

            }

            default -> throw new IllegalArgumentException("side");
        };
    }

    private BigDecimal targetPriceHigher(BigDecimal askPrice, Persent persent) {
        return HUNDRED
                .add(persent.getValue())
                .multiply(askPrice)
                .multiply(ONE_HUNDREDTH);
    }

    private BigDecimal targetPriceLower(BigDecimal bidPrice, Persent persent) {

        return HUNDRED
                .subtract(persent.getValue())
                .multiply(bidPrice)
                .multiply(ONE_HUNDREDTH);
    }

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100L);
    private static final BigDecimal ONE_HUNDREDTH = new BigDecimal("0.01");


}
