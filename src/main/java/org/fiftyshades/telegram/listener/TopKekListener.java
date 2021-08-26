package org.fiftyshades.telegram.listener;

import com.jtelegram.api.events.EventHandler;
import com.jtelegram.api.events.message.TextMessageEvent;
import com.jtelegram.api.message.input.file.ExternalInputFile;
import com.jtelegram.api.message.input.file.InputFile;
import com.jtelegram.api.requests.message.framework.ParseMode;
import com.jtelegram.api.requests.message.send.SendPhoto;
import com.jtelegram.api.requests.message.send.SendText;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// copied from https://github.com/bo0tzz/TopKekBot/blob/master/src/main/java/com/bo0tzz/topkekbot/engine/TopKekListener.java
// with modifications to use this API
// it's disgusting.
public class TopKekListener implements EventHandler<TextMessageEvent> {
    private final List<TextAction> textActions;
    private final String[] xd = {"x", "X", "d", "D"};

    private final Pattern jokesOnYouPattern = Pattern.compile("\\bjoke'?s?\\s+on\\s+you\\b", Pattern.CASE_INSENSITIVE);
    private final String jokesOnYouUrl = "http://i.imgur.com/4y6krel.png";
    private final Pattern jestOnTheePattern = Pattern.compile("\\bjest'?s?\\s+(?:on|(?:be\\s+)?with)\\s+th(?:ee|ou)\\b", Pattern.CASE_INSENSITIVE);
    private final String jestOnTheeUrl = "http://i.imgur.com/SzxKs5a.png";
    private final SecureRandom secureRandom = new SecureRandom();
    private InputFile jokesOnYouFile;
    private InputFile jestOnTheeFile;

    private final Pattern assPattern = Pattern.compile("(\\w+)-ass (\\w+)", Pattern.CASE_INSENSITIVE);

    private final Pattern subredditPattern = Pattern.compile("(?:^|\\s)/?r/(\\w+)[\\s$]?", Pattern.CASE_INSENSITIVE);

    public TopKekListener() {
        try {
            this.jokesOnYouFile = new ExternalInputFile(new URL(jokesOnYouUrl));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            this.jestOnTheeFile = new ExternalInputFile(new URL(jestOnTheeUrl));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        textActions = new LinkedList<TextAction>() {{
            //add(TextAction.from((t, ev) -> t.toLowerCase().startsWith("@topkek_bot"), (e) -> e.getMessage().getContent().substring(11)));

            //it's literally called "topkekbot" so this responds "kek" when someone says "top"
            add(TextAction.from((t, ev) -> t.equalsIgnoreCase("top"), (e) -> "kek"));
            add(TextAction.from((t, ev) -> t.toLowerCase().contains("good night"), (e) -> "goodnight bb, sweet dreams \uD83D\uDE18\uD83D\uDE18\uD83D\uDE18❤️❤️❤️\uD83D\uDC8B\uD83D\uDC8B\uD83D\uDC8B"));
            add(TextAction.from((t, ev) -> t.toLowerCase().contains("pleb"), (e) -> "Pleb, yes"));
            add(TextAction.from((t, ev) -> t.toLowerCase().contains("humble brag"), (e) -> "It's not their fault that they're better than you."));
            add(TextAction.from((t, ev) -> t.equalsIgnoreCase("not"), (e) -> "You - Not, Me - Bot"));
            add(TextAction.from((t, ev) -> t.toLowerCase().contains("blend"), (e) -> "But will it blend?"));
            add(TextAction.from((t, ev) -> t.equalsIgnoreCase("thank mr bot"), (e) -> "may good cpus and dedotated wams come to you"));
            add(TextAction.from((t, ev) -> t.equalsIgnoreCase("nice meme"), (e) -> "http://niceme.me/nicememe.mp3"));
            add(TextAction.from((t, ev) -> t.equalsIgnoreCase("true love"), (e) -> "http://i.imgur.com/nRAZBRs.png"));
            add(TextAction.from((t, ev) -> t.equalsIgnoreCase("hi"), (e) -> "sup"));
            add(TextAction.from((t, ev) -> t.equalsIgnoreCase("sup"), (e) -> "hi"));
            add(TextAction.from((t, ev) -> t.equalsIgnoreCase("oh canada"), (e) -> "http://i.imgur.com/bULAfzE.jpg"));
            add(TextAction.from((t, ev) -> t.equalsIgnoreCase("fish go moo"), (e) -> "@TopKek_Bot notices that " + e.getMessage().getSender().getFullname() + " is truly enlightened."));
            add(TextAction.from((t, ev) -> t.equalsIgnoreCase("moo"), (e) -> "woof"));
            add(TextAction.from((t, ev) -> t.contains("@topkek_bot"), (e) -> "topkek"));
            add(TextAction.from((t, ev) -> t.contains("@shibesquad"), (e) -> "+1"));
            add(TextAction.from((t, ev) -> t.contains("UUID"), (e) -> "Be careful with that UUID, you might get a collision!"));
            add(TextAction.from((t, ev) -> t.toLowerCase().contains("trident"), (e) -> secureRandom.nextInt(100) <= 15 ? "TridnetSDK is dead." : null));
            add(new TextAction((t, ev) -> t.toLowerCase().contains("topkek"), (e) ->
                    SendText.builder().text("[Gotta be safe while keking!](http://v.bo0tzz.me/topkek)").parseMode(ParseMode.MARKDOWN)
            ));
            add(TextAction.from((t, ev) -> t.toLowerCase().contains("rawr"), (e) -> "xd"));
            add(TextAction.from((t, ev) -> t.toLowerCase().contains("girl") && ev.getMessage().getSender().getUsername().equalsIgnoreCase("MazenK"), (e) -> "April is watching..."));
            add(TextAction.from((t, ev) ->
                                        ev.getMessage().getSender().getUsername().equalsIgnoreCase("DarkSeraphim") &&
                                                (t.toLowerCase().contains(" gf ") || t.toLowerCase().contains("girlfriend")),
                                e -> "ahem"
            ));
            add(TextAction.from((t, ev) -> t.equalsIgnoreCase("xD"), (e) -> {
                String s = e.getMessage().getContent().toLowerCase();
                int index = -1;
                StringBuilder m = new StringBuilder();
                Random r = new SecureRandom(); // we don't want people guessing!!!
                while ((index = s.indexOf("xd", index + 1)) != -1) {
                    m.append("x");
                    for (int i = 0; i < r.nextInt(10); i++) {
                        m.append(xd[r.nextInt(4)]);
                    }
                    m.append("D");
                }
                return m.toString();
            }));
            add(TextAction.from((t, ev) -> t.equals("tfw"), (e) -> "no gf"));
            add(TextAction.from((t, ev) -> assPattern.matcher(t).find(), (e) -> {
                String message = e.getMessage().getContent();
                Matcher m = assPattern.matcher(message);

                if (m.find()) {
                    return m.group(1) + " ass-" + m.group(2);
                }

                return null;
            }));
            add(new TextAction((t, ev) -> subredditPattern.matcher(t).find(), (e) -> {
                String message = e.getMessage().getContent();
                Matcher m = subredditPattern.matcher(message);

                if (m.find()) {
                    String subreddit = m.group(1);
                    String markdown = "[/r/";
                    markdown += subreddit;
                    markdown += "](https://old.reddit.com/r/";
                    markdown += subreddit;
                    markdown += ")";

                    return SendText.builder().text(markdown).parseMode(ParseMode.MARKDOWN);
                }

                return null;
            }));

            if (jokesOnYouFile != null) {
                add(new TextAction((t, ev) -> jokesOnYouPattern.matcher(t).find(), e -> {
                    e.getBot().perform (
                            SendPhoto.builder()
                                    .chatId(e.getMessage().getChat().getChatId())
                                    .photo(jokesOnYouFile)
                                    .build()
                    );
                    return null;
                }));
            }

            if (jestOnTheeFile != null) {
                add(new TextAction((t, ev) -> jestOnTheePattern.matcher(t).find(), e -> {
                    e.getBot().perform (
                            SendPhoto.builder()
                                    .chatId(e.getMessage().getChat().getChatId())
                                    .photo(jestOnTheeFile)
                                    .build()
                    );
                    return null;
                }));
            }
        }};


    }

    static class TextAction {
        private final BiPredicate<String, TextMessageEvent> predicate;
        private final Function<TextMessageEvent, SendText.SendTextBuilder> function;

        public TextAction(BiPredicate<String, TextMessageEvent> predicate,
                          Function<TextMessageEvent, SendText.SendTextBuilder> function) {
            this.predicate = predicate;
            this.function = function;
        }

        static TextAction from(BiPredicate<String, TextMessageEvent> predicate, Function<TextMessageEvent, String> function) {
            return new TextAction (
                    predicate,
                    textMessageReceivedEvent -> SendText.builder().text(function.apply(textMessageReceivedEvent)).parseMode(ParseMode.NONE)
            );
        }

        public SendText.SendTextBuilder apply(TextMessageEvent event) {
            return function.apply(event);
        }

        public boolean test(String s, TextMessageEvent c) {
            return predicate.test(s, c);
        }
    }

    @Override
    public void onEvent(TextMessageEvent event) {
        String message = event.getMessage().getContent();
        textActions.stream()
                .map(t -> t.test(message, event) ? t.apply(event) : null)
                .filter(Objects::nonNull)
                .filter((response) -> !response.build().getText().trim().isEmpty())
                .findFirst()
                .map(response -> {
                    event.getBot().perform(
                            response.chatId(event.getMessage().getChat().getChatId())
                                    .build()
                    );
                    return null;
                });
    }
}
