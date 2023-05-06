package com.example.TradeBoot.configuration;


import com.example.TradeBoot.api.domain.account.Position;
import com.example.TradeBoot.api.domain.orders.Order;
import com.example.TradeBoot.api.domain.wallet.Balance;
import com.example.TradeBoot.api.services.AccountService;
import com.example.TradeBoot.api.services.IOrdersService;
import com.example.TradeBoot.api.services.IPositionsService;
import com.example.TradeBoot.api.services.IWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@Component
public class TestUtils {

    @Autowired
    private AccountService accountService;

    @Autowired
    private IPositionsService positionsService;
    @Autowired
    private IWalletService walletService;

    @Autowired
    private IOrdersService ordersService;


    public void printAccountInfo() {
        var accountInfo = accountService.getAccountInformation();
        System.out.println("AccountInfo");
        var joiner = new StringJoiner("\n");
        joiner.add(addParameter("Username",accountInfo.getUsername()));
        joiner.add(addParameter("Collateral",accountInfo.getCollateral().toString()));
        joiner.add(addParameter("FreeCollateral",accountInfo.getFreeCollateral().toString()));
        joiner.add(addParameter("TotalAccountValue",accountInfo.getTotalAccountValue().toString()));
        joiner.add(addParameter("TotalPositionSize",accountInfo.getTotalPositionSize().toString()));
        joiner.add(addParameter("InitialMarginRequirement",accountInfo.getInitialMarginRequirement().toString()));
        joiner.add(addParameter("MaintenanceMarginRequirement",accountInfo.getMaintenanceMarginRequirement().toString()));

        System.out.println(joiner);
    }


    private static final String TABULATION = "\t";

    private static String addParameter(String parameterName, String value){
       return addParameter(parameterName, value, 1);
    }
    private static String addParameter(String parameterName, String value, int tabulationCount){
        return TABULATION.repeat(tabulationCount) + parameterName + " : " + value;
    }

    public void printPosition(String market) {
        Optional<Position> position = positionsService.getPositionByMarket(market);
        if (position.isPresent()) {
            System.out.println("PositionInfo");
            System.out.println(TABULATION + position);
        } else {
            System.out.println("Position in market " + market + " not found");
        }
    }

    public void printPositions() {
        List<Position> positions = positionsService.getAllPositions();
        System.out.println("AllPositionInfo");
        for (Position position : positions) {
            var joiner = new StringJoiner("\n");
            joiner.add(addParameter("Future name", position.getFuture()));
            joiner.add(addParameter("Cost", position.getCost().toString(), 2));
            joiner.add(addParameter("OpenSize", position.getOpenSize().toString(), 2));
            joiner.add(addParameter("NetSize",position.getNetSize().toString(), 2));
            System.out.println(joiner);
        }
    }

    public void printAllInfoPositions() {
        List<Position> positions = positionsService.getAllPositions();
        System.out.println("AllPositionInfo");
        for (Position position : positions) {
            System.out.println(TABULATION + position);
        }
    }

    public void printBalance(String market) {
        var balance =walletService.getBalanceByMarket(market);
        if(balance.isPresent()){
            System.out.println("Balance of " + market);
            System.out.println(TABULATION + balance);
        }
        else {
            System.out.println("Balance of " + market + " not found");
        }
    }
    public void printAllInfoBalances() {

        var balances = walletService.getBalances();
        System.out.println("All Balances  info");
        for (Balance balance : balances) {
            System.out.println(balance);
        }
    }

    public void printBalances() {
        var balances = walletService.getBalances();
        System.out.println("Balances");
        for (Balance balance : balances) {
            System.out.println(addParameter(balance.getCoin(), balance.getTotal().toString()));
        }

    }

    public void printOpenOrders(String market) {
        var orders = ordersService.getOpenOrdersBy(market);
        System.out.println("OpenOrders");
        for (Order order : orders) {
            System.out.println(TABULATION + order);
        }
        System.out.println("OpenOrders");
    }

    public void printOpenOrdersId(List<Order> openOrders) {
        System.out.println("OpenOrdersId:");
        for (Order openOrder : openOrders) {

            System.out.println(TABULATION + openOrder.getId());
        }
    }


}
