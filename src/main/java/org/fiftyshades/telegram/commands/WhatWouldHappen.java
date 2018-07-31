package org.fiftyshades.telegram.commands;

import com.jtelegram.api.chat.id.ChatId;
import com.jtelegram.api.commands.Command;
import com.jtelegram.api.commands.filters.CommandFilter;
import com.jtelegram.api.events.message.TextMessageEvent;
import com.jtelegram.api.requests.message.send.SendText;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class WhatWouldHappen implements CommandFilter {
    private static final String[] OPTIONS = new String[] {
            "[member] would join the [movement] movement",
            "[member] would come out as [sexuality]",
            "[member] would come out as [gender]",
            "[member] and [member] would move in together",
            "[member] would buy a [sexy toy]",
            "[member] would be featured on [show]",
            "[member] would leak [company]'s codebase",
            "[member] would let [member] use their [sex toy]",
            "[member] would publicly sexually abuse [member]",
            "[member] would become [member]'s slave for the day",
            "[member], [member], and [member] would join [extreme group]",
            "[member] would convert to [religion]",
            "[member] would become atheist",
            "[member] would have 99 problems, but [act] [member] ain't one",
            "[member] would sell their [body part]",
            "there would be a terrorist attack in [member]'s city",
            "[member], [member], and [member] would start an anti [movement] cult",
            "[member] would cry during sex with [member]",
            "[member] would have a threesome with their parents",
            "[member] would be Elon Musk's [job]",
            "[member] would become the most hated person alive",
            "[company] would hire [member] as a [job]",
            "[member] would assault [member] with their [body part]",
            "[member] would slap [member] with their [body part]",
            "[member] would slap [member] with their [sexy toy] on their [body part]",
            "[member] would sell themselves as a sex worker for [money]",
            "[member] would slap themselves with [object]",
            "[member] would come out as HowToBasic",
            "[member] is exposed as the creator of jOOQ",
            "[member] would let [member] lick their [body part]",
            "[member] would ejaculate [object]",
            "[member] would get a piercing on their [body part]",
            "[company] would release their latest and greatest product -- a [sex toy]",
            "[member] would have sex with [member] for [money]"
    };

    private static final Map<String, List<String>> ITEMS = new HashMap<String, List<String>>() {{
        put("member", Arrays.asList("Mazen", "Mattrick", "Vilsol", "Amir", "Zack", "Ryan", "Davo", "Eddy", "Julian", "Mark", "James", "Boet", "Nick"));
        put("movement", Arrays.asList("#MeToo", "Black Lives Matter", "Impeach Trump", "9/11 Truth Movement", "Feminist Movement", "Pro-Islamophobia Movement",
                "Fascism Movement", "Ku Klux Klan", "Men's rights", "Veganism", "Occupy Wall Street", "Pro-life", "Sex Workers' Rights"));
        put("sexuality", Arrays.asList("straight", "gay", "pansexual", "asexual", "furry", "objectumsexual"));
        put("gender", Arrays.asList("masculine female", "feminine female", "androgine female", "legendary cheese pizza", "masculine male", "feminine male",
                "androgine female"));
        put("sexy toy", Arrays.asList("dildo", "vibrator", "chastity belt", "GSpot Vibrator", "Fleshlight", "strap on", "vibrating dildo", "penis ring",
                "masturbation sleeve", "penis pump", "cock cage", "vaginal dilator", "ball gag", "ball stretcher", "butt plug", "clit clamp", "clit pump",
                "double ended vibrator", "penis extension", "sex doll", "sex swing"));
        put("show", Arrays.asList("CNN", "Last Week Tonight", "Is This America?", "Sex and the City", "Love Island", "Black Mirror", "Westworld"));
        put("company", Arrays.asList("Google", "Facebook", "Microsoft", "Uber", "Boeing", "Lyft", "Samsung", "Apple", "IBM", "Intel", "Oracle", "Amazon", "Twitter"));
        put("extreme group", Arrays.asList("ISIS", "Antifa", "The Alt-Right", "the Illuminati", "the KKK", "The New World Order", "the NSA", "Scientology", "the CIA",
                "Comcast", "Time Warner Cable", "Verizon"));
        put("religion", Arrays.asList("Christianity", "Mormonism", "Moronism", "Islam", "Judaism", "Last Thursdayism", "Pastafarism", "Anti-theism", "Atheism"));
        put("act", Arrays.asList("fucking", "killing"));
        put("body part", Arrays.asList("nose", "arm", "liver", "lung", "finger", "cock", "vagina", "ass", "asshole", "hair", "leg", "ear", "eye"));
        put("job", Arrays.asList("Machine Learning Engineer™", "sex slave", "Chief Sexual Officer", "Chief Blockchain Officer", "janitor", "intern",
                "spokesperson", "Vice President of Social Media", "Bi Engineer", "company prostitute", "puppy"));
        put("money", Arrays.asList("$2.5 billion", "10 cents", "$19.5 Billion", "some loose change and a bus pass", "an incomprehensible amount of money", "12 rupees", "50 bucks", "$0.02", "pretty much nothing", "1 MILLION DOLLARS"));
    }};

    @Override
    public boolean test(TextMessageEvent event, Command command) {
        String option = OPTIONS[ThreadLocalRandom.current().nextInt(OPTIONS.length)];

        for (Map.Entry<String, List<String>> token : ITEMS.entrySet()) {
            Random random = new Random();
            int tokenOptions = token.getValue().size();

            while(option.contains("[" + token.getKey() + "]")) {
                String replace;

                // don't allow repeats
                while (option.contains(replace = token.getValue().get(random.nextInt(tokenOptions)))) {
                }

                option = option.replaceFirst("\\[" + token.getKey() + "]", replace);
            }
        }

        String name = command.getSender().getFirstName();
        String possessiveReplacement = (name.endsWith("s") ? name + "'" : name + "'s");
        String message = command.getArgsAsText()
                .replaceAll("\\sI\\s/i", name)
                .replaceAll("\\s(my|me)\\s/i", possessiveReplacement);

        event.getBot().perform(
                SendText.builder()
                        .chatId(ChatId.of(command.getChat()))
                        .text(message + ", " + option)
                        .build()
        );
        return true;
    }
}
