package com.example.TradeBoot.notification.telegram.commands;

import com.example.TradeBoot.notification.telegram.SendTelegramBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Command interface for handling telegram-bot commands.
 */
public interface ICommand {

    /**
     * Main method, which is executing command logic.
     *
     * @param update provided {@link Update} object with all the needed data for command.
     */
    void execute(SendTelegramBotMessageService sendTelegramBotMessageService, Update update);
}
