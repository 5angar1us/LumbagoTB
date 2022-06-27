package com.example.TradeBoot.ui.controller;

import com.example.TradeBoot.BigDecimalUtils;
import com.example.TradeBoot.api.services.FinancialInstrumentService;
import com.example.TradeBoot.ui.models.TradeSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;

@Controller
public class TestController {

    @Autowired
    private FinancialInstrumentService financialInstrumentService;

    @GetMapping("test/")
    public String showSignUpForm(Model model) {

        model.addAttribute("names", financialInstrumentService.getAllNames());

        return "test-names";
    }
}
