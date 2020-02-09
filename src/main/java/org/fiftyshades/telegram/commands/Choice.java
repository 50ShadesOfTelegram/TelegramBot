package org.fiftyshades.telegram.commands;

import com.jtelegram.api.commands.Command;
import com.jtelegram.api.commands.filters.CommandFilter;
import com.jtelegram.api.events.message.TextMessageEvent;
import com.jtelegram.api.requests.message.send.SendText;

import java.util.Random;

public class Choice implements CommandFilter {
    private Random random = new Random();

    @Override
    public boolean test(TextMessageEvent event, Command command) {
        String[] args = event.getMessage().getText().split(",");
        SendText.SendTextBuilder builder = SendText.builder()
                .chatId(event.getMessage().getChat().getChatId())
                .replyToMessageID(event.getMessage().getMessageId());

        if (args.length <= 1) {
            builder.text("Give me choices!");
        } else {
            String choice = args[random.nextInt(args.length)].trim();
            builder.text("I say " + choice);
        }

        event.getBot().perform(builder.build());
        return true;
    }
}
