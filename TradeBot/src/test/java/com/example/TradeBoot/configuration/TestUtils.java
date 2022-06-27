package com.example.TradeBoot.configuration;


import com.example.TradeBoot.api.domain.account.Position;
import com.example.TradeBoot.api.domain.orders.OpenOrder;
import com.example.TradeBoot.api.domain.wallet.Balance;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class TestUtils {

    public static  void printAccountInfo() {
        var accountInfo = TestServiceInstances.getAccountService().getAccountInformation();
        System.out.println("AccountInfo");
        var joiner = new StringJoiner("\n");
        joiner.add(addParameter("Username",accountInfo.getUsername()));
        joiner.add(addParameter("Collateral",accountInfo.getCollateral().toString()));
        joiner.add(addParameter("FreeCollateral",accountInfo.getFreeCollateral().toString()));
        joiner.add(addParameter("TotalAccountValue",accountInfo.getTotalAccountValue().toString()));
        joiner.add(addParameter("TotalPositionSize",accountInfo.getTotalPositionSize().toString()));
        joiner.add(addParameter("InitialMarginRequirement",accountInfo.getInitialMarginRequirement().toString()));
        joiner.add(addParameter("MaintenanceMarginRequirement",accountInfo.getMaintenanceMarginRequirement().toString()));

        System.out.println(joiner.toString());
    }


    private static String TABULATION = "\t";
    private static String addParameter(String parameterName, String value){
        return TABULATION + parameterName + " : " + value;
    }

    public static  void printPosition(String market) {
        Optional<Position> position = TestServiceInstances.getPositionsService().getPositionByMarket(market);
        if (position.isPresent()) {
            System.out.println("PositionInfo");
            System.out.println(TABULATION + position);
        } else {
            System.out.println("Position in market " + market + " not found");
        }
    }

    public static  void printPositions() {
        List<Position> positions = TestServiceInstances.getPositionsService().getAllPositions();
        System.out.println("AllPositionInfo");
        for (Position position : positions) {
            System.out.println(TABULATION + position);
        }
    }

    public static  void printBalance(String market) {
        var balance = TestServiceInstances.getWalletService().getBalanceByMarket(market);
        if(balance.isPresent()){
            System.out.println("Balance of " + market);
            System.out.println(TABULATION + balance);
        }
        else {
            System.out.println("Balance of " + market + " not found");
        }
    }

    public static  void printBalances() {
        var balances = TestServiceInstances.getWalletService().getBalances();
        System.out.println("Balances");
        for (Balance balance : balances) {
            System.out.println(addParameter(balance.getCoin(), balance.getTotal().toString()));
        }

    }

    public static void printOpenOrders(String market) {
        var orders = TestServiceInstances.getOrdersService().getOpenOrders(market);
        System.out.println("OpenOrders");
        for (OpenOrder order : orders) {
            System.out.println(TABULATION + order);
        }

    }

    public static void printOpenOrdersId(List<OpenOrder> openOrders) {
        System.out.println("OpenOrdersId:");
        for (OpenOrder openOrder : openOrders) {
            System.out.println(TABULATION + openOrder.getId());
        }
    }
}
