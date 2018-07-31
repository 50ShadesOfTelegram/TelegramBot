package org.fiftyshades.telegram;

import com.google.gson.Gson;
import com.jtelegram.api.TelegramBot;
import com.jtelegram.api.TelegramBotRegistry;
import com.jtelegram.api.chat.id.ChatId;
import com.jtelegram.api.requests.message.send.SendText;
import com.jtelegram.api.update.PollingUpdateProvider;
import lombok.Getter;
import org.fiftyshades.telegram.commands.WhatWouldHappen;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

@Getter
public class FiftyShadesBot {
    @Getter
    private static final FiftyShadesBot INSTANCE = new FiftyShadesBot();
    private static final File CONFIG_FILE = new File("config.json");
    private final Gson gson = new Gson();
    private BotConfig config = new BotConfig();
    private TelegramBot bot;

    public static void main(String[] args) throws Exception {
        INSTANCE.start();
    }

    public void start() throws Exception {
        loadConfig();

        TelegramBotRegistry registry = TelegramBotRegistry.builder()
                .updateProvider(new PollingUpdateProvider())
                .build();

        registry.registerBot(config.getApiKey(), (bot, error) -> {
            if (error != null) {
                System.out.println("Unable to login into telegram with provided api key! Printing error...");
                error.printStackTrace();
                return;
            }

            this.bot = bot;

            bot.getCommandRegistry().registerCommand("whatwouldhappen", new WhatWouldHappen());
        });
    }

    private void loadConfig() throws IOException {
        if (!CONFIG_FILE.exists()) {
            CONFIG_FILE.createNewFile();
            saveConfig();

            System.out.println("Default config saved. Please modify it before continuing");
            System.exit(-1);
            return;
        }

        config = gson.fromJson(new FileReader(CONFIG_FILE), BotConfig.class);
        // insert new default values
        saveConfig();
    }
    
    private void saveConfig() throws IOException {
        Files.write(CONFIG_FILE.toPath(), Collections.singleton(gson.toJson(config)));
    }
}
