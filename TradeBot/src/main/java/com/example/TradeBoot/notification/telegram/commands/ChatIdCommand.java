package com.example.TradeBoot.notification.telegram.commands;

import com.example.TradeBoot.notification.telegram.SendTelegramBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ChatIdCommand implements ICommand {

    @Override
    public void execute(SendTelegramBotMessageService sendTelegramBotMessageService, Update update) {
        Long chatId = update.getMessage().getChatId();
        String message = "ID Канала : " + chatId.toString();

        sendTelegramBotMessageService.sendMessage(chatId, message);
    }
}
