package com.example.TradeBoot.ui.controller;

import com.example.TradeBoot.trade.services.BaseTradingEngineService;
import com.example.TradeBoot.ui.TradeSettingsRepositoryWrapper;
import com.example.TradeBoot.ui.models.TradeSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@Controller
public class ControlPanelController {
    static final Logger log =
            LoggerFactory.getLogger(ControlPanelController.class);
    @Autowired
    private BaseTradingEngineService tradingEngineService;

    @Autowired
    private TradeSettingsRepositoryWrapper tradeSettingsRepositoryWrapper;

    @GetMapping("control_panel")
    public String index(Model model) {

        var tradeSettingsCount = StreamSupport.stream(tradeSettingsRepositoryWrapper.findAll().spliterator(), false).count();

        model.addAttribute("status", tradingEngineService.isStop() ? "stopped" : "works");
        model.addAttribute("tradeSettingsCount",  tradeSettingsCount);
        model.addAttribute("runnableEngineCount", tradingEngineService.runnableEngineCount());

        return "control-panel";
    }

    @PostMapping("control_panel/start")
    public String start(Model model) {

        List<TradeSettings> tradeSettings = new ArrayList<TradeSettings>();
        for (TradeSettings currentTradeSettings : tradeSettingsRepositoryWrapper.findAll()) {
            tradeSettings.add(currentTradeSettings);
        }
        log.info("Trading engine service launch");
        tradingEngineService.currectStart(tradeSettings);
        return "redirect:/control_panel";
    }

    @PostMapping("control_panel/stop")
    public String stop(Model model) {
        log.info("Trading engine service save stop");
        log.debug("Trading engine service save stop");
        tradingEngineService.saveStop();
        return "redirect:/control_panel";
    }
}
