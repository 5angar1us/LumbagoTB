package com.example.TradeBoot.notification.telegram;

import com.example.TradeBoot.notification.telegram.commands.ECommandName;
import com.example.TradeBoot.notification.telegram.commands.ICommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayDeque;
import java.util.Arrays;


@Component
public class TelegramBot extends TelegramLongPollingBot {

    static final Logger log =
            LoggerFactory.getLogger(TelegramBot.class);
    Message requestMessage = new Message();

    private final String botUsername;
    private final String botToken;
    private final Long[] notificationChartsId;

    private final CommandContainer commandContainer;

    private final SendTelegramBotMessageService sendTelegramBotMessageService;

    private final static String COMMAND_PREFIX = "/";



    public TelegramBot(
            TelegramBotsApi telegramBotsApi,
            @Value("${telegram-bot.name}") String botUsername,
            @Value("${telegram-bot.token}") String botToken,
            Long[] notificationChatsId) throws TelegramApiException {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.notificationChartsId = notificationChatsId;

        commandContainer = new CommandContainer(notificationChatsId);

        this.sendTelegramBotMessageService = new SendTelegramBotMessageService(this);

        telegramBotsApi.registerBot(this);
    }

    /**
     * Этот метод вызывается при получении обновлений через метод GetUpdates.
     *
     * @param request Получено обновление
     */
    @Override
    public void onUpdateReceived(Update request) {
        requestMessage = request.getMessage();

        ArrayDeque<ICommand> deque = new ArrayDeque<ICommand>(2);
        deque.add(commandContainer.retrieveCommand(ECommandName.NO.getCommandName()));

        if (request.hasMessage() && request.getMessage().hasText()) {

            String message = request.getMessage().getText().trim();
            Long chatId = request.getMessage().getChatId();

            if (message.startsWith(COMMAND_PREFIX)) {
                String commandIdentifier = message.split(" ")[0].toLowerCase();

                deque.clear();
                if(Arrays.asList(notificationChartsId).contains(chatId) == false)
                {
                    deque.add(commandContainer.retrieveCommand(ECommandName.MEET.getCommandName()));
                }
                deque.add(commandContainer.retrieveCommand(commandIdentifier));
            }
        }

        deque.forEach(command -> command.execute(sendTelegramBotMessageService, request));

    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

}