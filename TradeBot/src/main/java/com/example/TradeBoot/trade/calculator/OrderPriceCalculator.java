package com.example.TradeBoot.trade.calculator;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.domain.markets.OrderBook;
import com.example.TradeBoot.api.domain.orders.EType;
import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.api.utils.BigDecimalUtils;
import com.example.TradeBoot.trade.model.OrderInformation;
import com.example.TradeBoot.trade.model.Persent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderPriceCalculator {


    static final Logger log =
            LoggerFactory.getLogger(OrderPriceCalculator.class);
    public OrderPriceCalculator() {}

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

        log.debug("bottomBording " + bottomBoarding + " currentPrice "+ currentPrice + " topBording " + topBoarding + " placedPrice " + placedPrice);
        log.debug("isLessOrEqualTopBoarding " + isLessOrEqualTopBoarding + " " + " isMoreOrEqualsBottomBoarding " + isMoreOrEqualsBottomBoarding);
        return isLessOrEqualTopBoarding && isMoreOrEqualsBottomBoarding;
    }

    public BigDecimal calculateCorrectPrice(OrderBook orderBook, Persent distance, ESide side) {
        BigDecimal price;
        switch (side) {
            case BUY -> {
                price = targetPriceLower(
                        orderBook.getBestBid().getPrice(),
                        distance);

                log.debug("bestBid " + orderBook.getBestBid().getPrice());
            }

            case SELL -> {
                price = targetPriceHigher(
                        orderBook.getBestAsk().getPrice(),
                        distance);

                log.debug("bestAsk " + orderBook.getBestAsk().getPrice());
            }

            default -> throw new IllegalArgumentException("side");
        }
        return price;
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
