package com.example.TradeBoot.api.services;

import com.example.TradeBoot.configuration.TestUtils;
import org.junit.jupiter.api.Test;

public class AccountTests {

    @Test
    public void print(){
        var market= "SOL/USD";
        TestUtils.printAccountInfo();
        TestUtils.printBalances();
        TestUtils.printAllInfoBalances();
        TestUtils.printOpenOrders(market);
        TestUtils.printPositions();
        TestUtils.printAllInfoPositions();
    }
}
