package org.fiftyshades.telegram.commands;

import com.jtelegram.api.commands.Command;
import com.jtelegram.api.commands.filters.CommandFilter;
import com.jtelegram.api.events.message.TextMessageEvent;
import com.jtelegram.api.requests.message.send.SendText;

import java.util.Random;

public class EightBall implements CommandFilter {
    private static final String[] OPTIONS_8BALL = {
            "It is certain",
            "It is decidedly so",
            "Without a doubt",
            "Yes definitely",
            "You may rely on it",
            "As I see it, yes",
            "Most likely",
            "Outlook good",
            "Yes",
            "Signs point to yes",
            "Don't count on it",
            "My reply is no",
            "My sources say no",
            "Outlook not so good",
            "Very doubtful"
    };
    private Random random = new Random();

    @Override
    public boolean test(TextMessageEvent event, Command command) {
        int chosen = random.nextInt(OPTIONS_8BALL.length);

        event.getBot().perform (
                SendText.builder().text(OPTIONS_8BALL[chosen])
                        .replyToMessageID(event.getMessage().getMessageId())
                        .chatId(event.getMessage().getChat().getChatId())
                        .build()
        );
        return true;
    }
}
