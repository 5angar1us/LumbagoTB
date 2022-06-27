package com.example.TradeBoot.trade.model;

import com.example.TradeBoot.BigDecimalUtils;

import java.math.BigDecimal;

public class Persent {

    public Persent(int value) {
        setValue(BigDecimal.valueOf(value));
    }
    public Persent(double value) {
        setValue(BigDecimal.valueOf(value));
    }
    public Persent(BigDecimal value) {
        setValue(value);
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {

        var isLessOrEqualTopBoarding = BigDecimalUtils.check(
                MAX_VALUE,
                BigDecimalUtils.EOperator.GREATER_THAN_OR_EQUALS,
                value
        );

        var isMoreOrEqualsBottomBoarding = BigDecimalUtils.check(
                MIN_VALUE,
                BigDecimalUtils.EOperator.LESS_THAN_OR_EQUALS,
                value
        );


        if(isLessOrEqualTopBoarding || isMoreOrEqualsBottomBoarding)
            this.value = value;
        else
            throw new IllegalArgumentException("value");
    }



    private BigDecimal value;

    public static BigDecimal MAX_VALUE = BigDecimal.valueOf(100L);
    public static BigDecimal MIN_VALUE = BigDecimal.valueOf(-100L);

    @Override
    public String toString() {
        return "Persent{" +
                "value=" + value +
                '}';
    }
}
