package com.example.TradeBoot.ui.controller;

import com.example.TradeBoot.trade.services.TradingEngineService;
import com.example.TradeBoot.ui.service.BaseTradeSettingsService;
import com.example.TradeBoot.ui.service.TradeStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ControlPanelController {
    static final Logger log =
            LoggerFactory.getLogger(ControlPanelController.class);
    @Autowired
    private TradingEngineService tradingEngineService;

    @Autowired
    private BaseTradeSettingsService baseTradeSettingsService;

    @Autowired
    private TradeStatusService tradeStatusService;

    @GetMapping("control_panel")
    public String index(Model model) {
        model.addAttribute("status", tradingEngineService.isStop() ? "stopped" : "works");
        model.addAttribute("runnableEngineCount", tradingEngineService.runnableEnginesCount());

        model.addAttribute("tradeSettingsCount",  tradeStatusService.getTradeSettingsCount());
        model.addAttribute("openOrders", tradeStatusService.getOpenOrdersByConfiguration());
        model.addAttribute("openPositions", tradeStatusService.getOpenPositions());

        return "control-panel";
    }

    @PostMapping("control_panel/start")
    public String start(Model model) {
        log.info("Trading engine service launch");
        tradingEngineService.correctStart();
        return "redirect:/control_panel";
    }

    @PostMapping("control_panel/stop")
    public String stop(Model model) {
        log.info("Trading engine service save stop");
        tradingEngineService.saveStop();
        return "redirect:/control_panel";
    }
}
