package com.example.TradeBoot.notification.telegram.commands;

import com.example.TradeBoot.notification.telegram.SendTelegramBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class MeetCommand implements ICommand {

    private final Long[] notificationChartsId;

    public MeetCommand(Long[] notificationChartsId) {
        this.notificationChartsId = notificationChartsId;
    }

    @Override
    public void execute(SendTelegramBotMessageService sendTelegramBotMessageService, Update update) {

        Long chatId = update.getMessage().getChatId();

        String message;

        if(List.of(notificationChartsId).contains(chatId)){
            var name = update.getMessage().getChat().getFirstName();
            message = "Привет, " + name;
        }else {
            message = "Мы не знакомы. А я незнакомым не всё рассказываю.";
        }
        sendTelegramBotMessageService.sendMessage(chatId, message);
    }
}
