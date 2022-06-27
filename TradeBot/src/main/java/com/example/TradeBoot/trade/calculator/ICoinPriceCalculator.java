package com.example.TradeBoot.trade.calculator;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.domain.markets.OrderBook;
import com.example.TradeBoot.trade.model.Persent;

import java.math.BigDecimal;

public interface ICoinPriceCalculator {

    public BigDecimal getPrice(OrderBook orderBook, BigDecimal incrementSize, ESide side);

    public class Base implements ICoinPriceCalculator {
        @Override
        public BigDecimal getPrice(OrderBook orderBook, BigDecimal incrementSize, ESide side){
            return switch (side){
                case SELL -> getBestAskPrice(orderBook, incrementSize);
                case BUY -> getBestBidPrice(orderBook, incrementSize);
                default -> throw new IllegalArgumentException("side");
            };
        }
        private BigDecimal getBestAskPrice(OrderBook orderBook, BigDecimal incrementSize) {
            return orderBook
                    .getBestAsk()
                    .getPrice()
                    .subtract(incrementSize);
        }

        private BigDecimal getBestBidPrice(OrderBook orderBook, BigDecimal incrementSize) {
            return orderBook
                    .getBestBid()
                    .getPrice()
                    .add(incrementSize);
        }
    }

    public class Mock implements ICoinPriceCalculator{

        @Override
        public BigDecimal getPrice(OrderBook orderBook, BigDecimal incrementSize, ESide side) {
            Persent persent = new Persent(5);

            OrderPriceCalculator orderPriceCalculator = new OrderPriceCalculator();
            return switch (side){
                case SELL -> orderPriceCalculator.calculateCorrectPrice(orderBook,persent, ESide.SELL);
                case BUY -> orderPriceCalculator.calculateCorrectPrice(orderBook,persent, ESide.BUY);
                default -> throw new IllegalArgumentException("side");
            };
        }

    }

}
