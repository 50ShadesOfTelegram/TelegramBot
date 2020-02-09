package org.fiftyshades.telegram.commands;

import com.jtelegram.api.commands.Command;
import com.jtelegram.api.commands.filters.CommandFilter;
import com.jtelegram.api.events.message.TextMessageEvent;
import com.jtelegram.api.requests.message.send.SendText;

public class Lenny implements CommandFilter {
    @Override
    public boolean test(TextMessageEvent event, Command command) {
        event.getBot().perform (
                SendText.builder()
                        .chatId(event.getMessage().getChat().getChatId())
                        .text("( ͡° ͜ʖ ͡°)")
                        .build()
        );
        return true;
    }
}
