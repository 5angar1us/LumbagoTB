package com.example.TradeBoot;

import com.example.TradeBoot.configuration.TestUtils;
import org.junit.jupiter.api.Test;

public class ShowAccountData {

    @Test
    public void print(){
        TestUtils.printAccountInfo();
        TestUtils.printBalances();
        TestUtils.printAllInfoBalances();
        TestUtils.printPositions();
        TestUtils.printAllInfoPositions();
    }
}
