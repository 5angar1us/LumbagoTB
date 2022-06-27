package com.example.TradeBoot.ui.utils;

import com.example.TradeBoot.ui.models.TradeSettings;

import javax.servlet.http.HttpServletRequest;

public class TradeSettingsFiledParser {

    public static void parse(HttpServletRequest request, TradeSettings tradeSettings){
        var tradeSettingsDetails = TradeSettingsDetailsParser.parse(request, tradeSettings);
        tradeSettings.addAllDetail(tradeSettingsDetails);
    }


}
