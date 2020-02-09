package org.fiftyshades.telegram.commands;

import com.jtelegram.api.commands.Command;
import com.jtelegram.api.commands.filters.CommandFilter;
import com.jtelegram.api.events.message.TextMessageEvent;
import com.jtelegram.api.requests.message.DeleteMessage;
import com.jtelegram.api.requests.message.send.SendText;

public class Aesthetic implements CommandFilter {
    private static final String[] AESTHETIC_LETTERS = {
            "Ａ", "Ｂ", "Ｃ", "Ｄ", "Ｅ", "Ｆ", "Ｇ", "Ｈ", "Ｉ", "Ｊ", "Ｋ", "Ｌ",
            "Ｍ", "Ｎ", "Ｏ", "Ｐ", "Ｑ", "Ｒ", "Ｓ", "Ｔ", "Ｕ", "Ｖ", "Ｗ", "Ｘ",
            "Ｙ", "Ｚ"
    };

    @Override
    public boolean test(TextMessageEvent event, Command command) {
        StringBuilder sb = new StringBuilder();

        for (char c : command.getArgsAsText().toCharArray()) {
            int index = ((int) c) - 97; //Character code "a" starts at 97

            if (index >= 0 && index <= 25) {
                sb.append(AESTHETIC_LETTERS[index]).append(" ");
                continue;
            }

            sb.append(c).append(" ");
        }

        event.getBot().perform (
                DeleteMessage.builder()
                        .messageId(event.getMessage().getMessageId())
                        .chatId(event.getMessage().getChat().getChatId())
                        .build()
        );

        event.getBot().perform (
                SendText.builder()
                        .text(event.getMessage().getSender().getFirstName() + ": " + sb.toString())
                        .chatId(event.getMessage().getChat().getChatId())
                        .build()
        );
        return true;
    }
}
