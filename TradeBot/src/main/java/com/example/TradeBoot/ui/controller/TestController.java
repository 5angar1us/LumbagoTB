package com.example.TradeBoot.ui.controller;

import com.example.TradeBoot.trade.services.FinancialInstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
