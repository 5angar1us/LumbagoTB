package com.example.TradeBoot.ui.controller;

import com.example.TradeBoot.ui.models.TradeSettings;
import com.example.TradeBoot.ui.models.TradeSettingsDetail;
import com.example.TradeBoot.ui.utils.HttpServletRequestUitls;
import com.example.TradeBoot.ui.utils.TradeSettingsFiledParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.MalformedURLException;
import java.util.List;

@Controller
public class TradeSettingsDetailsController {

//    @GetMapping("trade_settings/trade_settings_details/signup")
//    public String showSignUpForm(TradeSettingsDetail tradeSettingsDetail, Model model) {
//        tradeSettingsDetail.setPriceOffset(0.001);
//        tradeSettingsDetail.setVolume(50D);
//        tradeSettingsDetail.setTradeSettings((TradeSettings) model.getAttribute("tradeSettings"));
//        model.addAttribute("tradeSettingsDetail", tradeSettingsDetail);
//        return "add-trade-settings-detail";
//    }

    @PostMapping("/add_trade_settings_details")
    public String add(@Valid TradeSettingsDetail tradeSettingsDetail, BindingResult result, Model model, RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            return "add-trade-settings-detail";
        }
        var tradeSettings = (TradeSettings) model.getAttribute("tradeSettings");
        List<TradeSettingsDetail> tradesSettingsDetails;

        tradeSettings.getTradeSettingsDetails().add(tradeSettingsDetail);
        redirectAttrs.addFlashAttribute("tradeSettings", tradeSettings);


        return "redirect:trade_settings/signup";
    }

    // additional CRUD methods

    @PostMapping("trade_settings_details/update/{id}")
    public String update(@PathVariable("id") long id, @Valid TradeSettings tradeSettings,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            tradeSettings.setId(id);
            return "update-trade-settings";
        }

        return "redirect:/add-trade-settings";
    }

    @PostMapping("trade_settings/trade_settings_details/delete/{index}")
    public String delete(@PathVariable("index") int index, TradeSettings tradeSettings, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) throws MalformedURLException {
        TradeSettingsFiledParser.parse(request, tradeSettings);

        var tradeSettingsDetails = tradeSettings.getTradeSettingsDetails();

        if (tradeSettingsDetails == null || tradeSettingsDetails.isEmpty() || tradeSettingsDetails.size() < index)
            return "redirect:/trade_settings/signup";

        tradeSettings.getTradeSettingsDetails().remove(index - 1);

        redirectAttributes.addFlashAttribute("tradeSettings", tradeSettings);
        return "redirect:" + HttpServletRequestUitls.getPreventPath(request);
    }



//    @GetMapping("trade_settings_details/edit/{id}")
//    public String showUpdateForm(@PathVariable("id") long id, Model model) {
//        TradeSettings tradeSettings = this.tradeSettingsRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
//
//        model.addAttribute("tradeSettings", tradeSettings);
//        return "update-trade-settings";
//    }

//    @GetMapping("trade_settings_details/index")
//    public String showUserList(Model model) {
//        model.addAttribute("tradesSettings", tradeSettingsRepository.findAll());
//        return "index";
//    }
}
