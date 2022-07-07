package com.example.TradeBoot.ui.utils;

import com.example.TradeBoot.ui.models.TradeSettings;
import com.example.TradeBoot.ui.models.TradingStrategy;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class redirectUtils {

    public static void addTradeSettingsCrudAttributes(RedirectAttributes redirectAttributes, TradeSettings tradeSettings){

        redirectAttributes.addFlashAttribute("tradeSettings", tradeSettings);
        redirectAttributes.addFlashAttribute("tradingStrategyTypes", TradingStrategy.values());
    }

}
