package com.example.TradeBoot.ui.controller;

import com.example.TradeBoot.notification.EMessageType;
import com.example.TradeBoot.notification.INotificationService;
import com.example.TradeBoot.notification.telegram.TelegramNotificationService;
import com.example.TradeBoot.trade.services.FinancialInstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@SuppressWarnings("ALL")
@Controller
public class TestController {

    @Autowired
    private FinancialInstrumentService financialInstrumentService;

    @Autowired
    private TelegramNotificationService telegramNotificationService;

    @Autowired
    private INotificationService notificationService;

    @Value("${app.security:true}")
    private Boolean isProductionMode;

    @GetMapping("test/test_names")
    public String showFinancialInstrumentNames(Model model) {

        model.addAttribute("names", financialInstrumentService.getAllNames());

        return "test-names";
    }

    @GetMapping("test/notification")
    public String showTestNotificationForm(Model model) {
        if (isProductionMode == false) {
            return "test-notification";
        }
        return "redirect:/index";
    }

    @PostMapping("test/send_telegram_notification")
    public String sendTelegramNotification(Model model) {
        if (isProductionMode == false) {
            telegramNotificationService.sendMessage(EMessageType.Empty,"Проверочка");
            return "redirect:/test/notification";
        }
        return "redirect:/index";
    }
    @PostMapping("test/send_default_notification")
    public String sendDefaultNotification(Model model) {
        if (isProductionMode == false) {
            notificationService.sendMessage(EMessageType.Empty,"Проверочка");
            return "redirect:/test/notification";
        }
        return "redirect:/index";
    }
}
