package com.example.TradeBoot;

import com.example.TradeBoot.configuration.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;

@SpringBootTest
@Lazy
public class ShowAccountData {
    @Autowired
    TestUtils testUtils;
    @Test
    public void print(){
        testUtils.printAccountInfo();
        testUtils.printBalances();
        testUtils.printAllInfoBalances();
        testUtils.printPositions();
        testUtils.printAllInfoPositions();
    }
}
