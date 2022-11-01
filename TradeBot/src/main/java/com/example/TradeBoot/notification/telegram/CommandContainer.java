package com.example.TradeBoot.notification.telegram;

import com.example.TradeBoot.notification.telegram.commands.*;
import com.google.common.collect.ImmutableMap;

/**
 * Container of the {@link ICommand}s, which are using for handling telegram commands.
 */
public class CommandContainer {

    private final ImmutableMap<String, ICommand> commandMap;
    private final ICommand unknownICommand;

    public CommandContainer(Long[] notificationChatsId) {

        commandMap = ImmutableMap.<String, ICommand>builder()
                .put(ECommandName.HELP.getCommandName(), new HelpICommand())
                .put(ECommandName.NO.getCommandName(), new NoCommand())
                .put(ECommandName.CHAT_ID.getCommandName(), new ChatIdCommand())
                .put(ECommandName.MEET.getCommandName(), new MeetCommand(notificationChatsId))
                .build();

        unknownICommand = new UnknownICommand();
    }

    public ICommand retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownICommand);
    }

}