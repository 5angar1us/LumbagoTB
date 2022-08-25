package com.example.TradeBoot.api.domain.markets;

import java.math.BigDecimal;
import java.util.List;

public interface OrderBookLine {
    BigDecimal getPrice();

    BigDecimal getVolume();

    public abstract class Abstract implements OrderBookLine {
        @Override
        public String toString() {
            return "Abstract{" +
                    "position=" + position +
                    '}';
        }

        List<BigDecimal> position;

        public Abstract(List<BigDecimal> position) {
            this.position = position;
        }

        public Abstract() {}

        public BigDecimal getPrice() {
            return this.position.get(0);
        }

        public BigDecimal getVolume() {
            return this.position.get(1);
        }

        public void setPosition(List<BigDecimal> position) {
            this.position = position;
        }
    }
}