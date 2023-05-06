package com.example.TradeBoot.ui.utils;

import com.example.TradeBoot.ui.models.TradeSettings;
import com.example.TradeBoot.ui.models.TradeSettingsDetail;
import com.example.TradeBoot.ui.models.TradingStrategy;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TradeSettingsDetailsParser {
    private static final String DETAIL_VOLUME = "detailVolume";
    private static final String DETAIL_PRICE_OFFSET = "detailPriceOffset";
    private static final String DETAIL_ID = "detailID";
    private static final String DETAIL_TRADING_STRATEGY_TYPE = "detailTradingStrategyType";

    public static List<TradeSettingsDetail> parse(HttpServletRequest request, TradeSettings targetTradeSettings) {

        var detailId = request.getParameterValues(DETAIL_ID);
        var detailVolumes = request.getParameterValues(DETAIL_VOLUME);
        var detailPriceOffset = request.getParameterValues(DETAIL_PRICE_OFFSET);
        var detailTradingStrategyType = request.getParameterValues(DETAIL_TRADING_STRATEGY_TYPE);


        if (detailVolumes == null && detailPriceOffset == null && detailTradingStrategyType == null)
            return new ArrayList<TradeSettingsDetail>();

        var tradeSettingsDetails = new ArrayList<TradeSettingsDetail>();

        for (var i = 0; i < detailVolumes.length; i++) {
            tradeSettingsDetails.add(
                    createTradeSettingsDetail(targetTradeSettings, detailId, detailVolumes, detailPriceOffset, detailTradingStrategyType, i));
        }
        return tradeSettingsDetails;
    }

    private static TradeSettingsDetail createTradeSettingsDetail
            (TradeSettings targetTradeSettings,
             String[] detailId,
             String[] detailVolumes,
             String[] detailPriceOffset,
             String[] detailTradingStrategyType,
             int i) {
        int volume = Integer.parseInt(detailVolumes[i]);
        BigDecimal priceOffset = new BigDecimal(detailPriceOffset[i]);
        TradingStrategy tradingStrategyType = TradingStrategy.getByStringName(detailTradingStrategyType[i]);

        var isDetailIdNullOrEmpty = detailId == null && detailId.length == 0;
        if (isDetailIdNullOrEmpty) {
            return new TradeSettingsDetail(tradingStrategyType, volume, priceOffset, targetTradeSettings);
        }

        var id = Integer.valueOf(detailId[i]);

        var isTradeSettingsDetailInDatabase = Integer.parseInt(detailId[i]) == -1;
        if (isTradeSettingsDetailInDatabase) {
            return new TradeSettingsDetail(tradingStrategyType, volume, priceOffset, targetTradeSettings);
        }

        return new TradeSettingsDetail(id, tradingStrategyType, volume, priceOffset, targetTradeSettings);
    }
}
