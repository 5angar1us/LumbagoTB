package com.example.TradeBoot.notification.telegram.commands;

import com.example.TradeBoot.notification.telegram.SendTelegramBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NoCommand implements ICommand {

    public static final String NO_MESSAGE = "Я поддерживаю команды, начинающиеся со слеша(/).\n"
            + "Чтобы посмотреть список комманд введи /help";

    @Override
    public void execute(SendTelegramBotMessageService sendTelegramBotMessageService, Update update) {
        var message = update.getMessage();
        if(message == null) return;

        sendTelegramBotMessageService.sendMessage(message.getChatId(), NO_MESSAGE);
    }
}