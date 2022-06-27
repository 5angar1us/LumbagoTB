package com.example.TradeBoot;

import java.math.BigDecimal;

public class BigDecimalUtils {

    public static boolean check(BigDecimal firstNum, EOperator EOperator, BigDecimal secondNum) {
        switch (EOperator) {
            case EQUALS:
                return firstNum.compareTo(secondNum) == 0;
            case LESS_THAN:
                return firstNum.compareTo(secondNum) < 0;
            case LESS_THAN_OR_EQUALS:
                return firstNum.compareTo(secondNum) <= 0;
            case GREATER_THAN:
                return firstNum.compareTo(secondNum) > 0;
            case GREATER_THAN_OR_EQUALS:
                return firstNum.compareTo(secondNum) >= 0;
        }

        throw new IllegalArgumentException("Will never reach here");
    }

    public enum EOperator {
        LESS_THAN, LESS_THAN_OR_EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUALS, EQUALS
    }
}
