package com.example.TradeBoot.notification.telegram.commands;

import com.example.TradeBoot.notification.telegram.SendTelegramBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Unknown {@link ICommand}.
 */
public class UnknownICommand implements ICommand {

    public static final String UNKNOWN_MESSAGE = "Не понимаю вас \uD83D\uDE1F, напишите /help чтобы узнать что я понимаю.";

    @Override
    public void execute(SendTelegramBotMessageService sendTelegramBotMessageService, Update update) {
        sendTelegramBotMessageService.sendMessage(update.getMessage().getChatId(), UNKNOWN_MESSAGE);
    }
}
