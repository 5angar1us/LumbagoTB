package com.example.TradeBoot.notification.telegram.commands;

/**
 * Enumeration for {@link ICommand}'s.
 */
public enum ECommandName {

    MEET("/meet"),
    HELP("/help"),
    CHAT_ID("/chatid"),
    NO("nocommand");

    private final String commandName;

    ECommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

}
