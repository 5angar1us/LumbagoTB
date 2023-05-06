package com.example.TradeBoot.notification.telegram.commands;

import com.example.TradeBoot.notification.telegram.SendTelegramBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Help {@link ICommand}.
 */
public class HelpICommand implements ICommand {

    public static final String HELP_MESSAGE = String.format("""
                    ✨<b>Дотупные команды</b>✨

                    %s - А мы знакомы?
                    %s - получить id чата

                    %s - получить помощь в работе со мной
                    """,
            ECommandName.MEET.getCommandName(),
            ECommandName.CHAT_ID.getCommandName(),
            ECommandName.HELP.getCommandName());

    @Override
    public void execute(SendTelegramBotMessageService sendTelegramBotMessageService, Update update) {
        sendTelegramBotMessageService.sendMessage(update.getMessage().getChatId(), HELP_MESSAGE);
    }
}
