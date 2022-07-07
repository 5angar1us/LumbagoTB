package com.example.TradeBoot.ui.controller;

import com.example.TradeBoot.BigDecimalUtils;
import com.example.TradeBoot.api.services.FinancialInstrumentService;
import com.example.TradeBoot.ui.BaseTradeSettingsService;
import com.example.TradeBoot.ui.models.TradeSettings;
import com.example.TradeBoot.ui.models.TradeSettingsDetail;
import com.example.TradeBoot.ui.models.TradingStrategy;
import com.example.TradeBoot.ui.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.List;

@Controller
public class TradeSettingsController {

    @Autowired
    public TradeSettingsController(BaseTradeSettingsService baseTradeSettingsService) {
        this.baseTradeSettingsService = baseTradeSettingsService;
    }

    @Autowired
    private BaseTradeSettingsService baseTradeSettingsService;

    private Validator baseValidator =  Validation.buildDefaultValidatorFactory().getValidator();



    @GetMapping("trade_settings/signup")
    public String showSignUpForm(TradeSettings tradeSettings, Model model) {

        double epsilon = 0.000001d;
        var isTradeDelay = tradeSettings.getTradeDelay() == 0L;
//        var isMax = tradeSettings.getMaximumDefinition() - 0.0 > epsilon;
        var isMax = BigDecimalUtils.check(tradeSettings.getMaximumDefinition(), BigDecimalUtils.EOperator.EQUALS, BigDecimal.ZERO);
        if (isTradeDelay && isMax) {
            tradeSettings.setTradeDelay(1000);
            //tradeSettings.setMaximumDefinition(0.01);
            tradeSettings.setMaximumDefinition(new BigDecimal("0.01"));
        }
        model.addAttribute("tradingStrategyTypes", TradingStrategy.values());

        return "add-trade-settings";
    }

    @GetMapping("trade_settings/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {

        var tradeSettings = (TradeSettings) model.getAttribute("tradeSettings");
        if (tradeSettings == null) {
            tradeSettings = this.baseTradeSettingsService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
            model.addAttribute("tradeSettings", tradeSettings);
        }
        model.addAttribute("tradingStrategyTypes", TradingStrategy.values());

        return "update-trade-settings";
    }


    @GetMapping("trade_settings/add")
    public String addGet(@Valid TradeSettings tradeSettings, BindingResult result, Model model, HttpServletRequest request){
        TradeSettingsFiledParser.parse(request, tradeSettings);
        var tradeSettingsDetailsErrors = ValidorUtil.validateModels(baseValidator, tradeSettings.getTradeSettingsDetails());
//        var marketNameErrors =  ValidorUtil.validateMarketName(tradeSettings.getMarketName(), financialInstrumentsNames);

        ErrorsUtils.AddErrors(result, tradeSettingsDetailsErrors);
//        ErrorsUtils.AddErrors(result, marketNameErrors);

        model.addAttribute("tradingStrategyTypes", TradingStrategy.values());
        return "add-trade-settings";
    }

    @PostMapping("trade_settings/add")
    public String add(@Valid TradeSettings tradeSettings, BindingResult result, Model model, HttpServletRequest request) {

        TradeSettingsFiledParser.parse(request, tradeSettings);
        var tradeSettingsDetailsErrors = ValidorUtil.validateModels(baseValidator, tradeSettings.getTradeSettingsDetails());
//        var marketNameErrors =  ValidorUtil.validateMarketName(tradeSettings.getMarketName(), financialInstrumentsNames);

        ErrorsUtils.AddErrors(result, tradeSettingsDetailsErrors);
//        ErrorsUtils.AddErrors(result, marketNameErrors);


        if (result.hasErrors()) {
//            redirectAttributes.addFlashAttribute("tradeSettings", tradeSettings);
//            redirectAttributes.addFlashAttribute("tradingStrategyTypes", TradingStrategy.values());

            model.addAttribute("tradingStrategyTypes", TradingStrategy.values());
            //return "redirect:/trade_settings/signup";
            return "add-trade-settings";
        }

        this.baseTradeSettingsService.save(tradeSettings);
        return "redirect:/trade_settings/index";
    }


    // additional CRUD methods

    @PostMapping("trade_settings/update/{id}")
    public String update(@PathVariable("id") long id, @Valid TradeSettings tradeSettings, BindingResult result, Model model, HttpServletRequest request) {
        TradeSettingsFiledParser.parse(request, tradeSettings);
        var errors = ValidorUtil.validateModels(baseValidator, tradeSettings.getTradeSettingsDetails());
        ErrorsUtils.AddErrors(result, errors);

        if (result.hasErrors()) {
            tradeSettings.setId(id);
            return "update-trade-settings";
        }

        this.baseTradeSettingsService.update(tradeSettings);
        return "redirect:/trade_settings/index";
    }

    @GetMapping("trade_settings/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        TradeSettings tradeSettings = this.baseTradeSettingsService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        this.baseTradeSettingsService.delete(tradeSettings);

        return "redirect:/trade_settings/index";
    }



    @GetMapping("trade_settings/index")
    public String showUserList(Model model) {
        model.addAttribute("tradesSettings", baseTradeSettingsService.findAll());
        return "trade-settings-list";
    }


    ///
    @PostMapping("trade_settings/add_detail")
    public String addDetail(TradeSettings tradeSettings, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {
        TradeSettingsFiledParser.parse(request, tradeSettings);

        redirectAttrs.addFlashAttribute("tradeSettings", tradeSettings);
        return "redirect:/trade_settings/trade_settings_details/signup";
    }

    @PostMapping("trade_settings/add_row")
    public String addRow(TradeSettings tradeSettings, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) throws MalformedURLException {
        TradeSettingsFiledParser.parse(request, tradeSettings);

        tradeSettings.getTradeSettingsDetails().add(new TradeSettingsDetail(TradingStrategy.ALL,10, new BigDecimal("0.001")));

        //redirectUtils.addTradeSettingsCrudAttributes(redirectAttributes, tradeSettings);

        redirectAttributes.addFlashAttribute("tradingStrategyTypes", TradingStrategy.values());
        redirectAttributes.addFlashAttribute("tradeSettings", tradeSettings);
        var t = HttpServletRequestUitls.getPreventPath(request);
        return "redirect:" + HttpServletRequestUitls.getPreventPath(request);
        //return "add-trade-settings";
    }


}
