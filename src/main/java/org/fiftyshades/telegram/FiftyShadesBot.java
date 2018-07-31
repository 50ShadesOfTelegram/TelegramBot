package org.fiftyshades.telegram;

import com.google.gson.Gson;
import com.jtelegram.api.TelegramBot;
import com.jtelegram.api.TelegramBotRegistry;
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
        INSTANCE.start(args[0]);
    }

    public void start(String apiKey) throws Exception {
        loadConfig();

        TelegramBotRegistry registry = TelegramBotRegistry.builder()
                .updateProvider(new PollingUpdateProvider())
                .build();

        registry.registerBot(apiKey, (bot, error) -> {
            if (error != null) {
                System.out.println("Unable to login into telegram with provided api key! Printing error...");
                error.printStackTrace();
                return;
            }

            this.bot = bot;
            System.out.println("Successfully logged in as " + bot.getBotInfo().getUsername());

            bot.getCommandRegistry().registerCommand("whatwouldhappen", new WhatWouldHappen());
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Bot killed.")));
    }

    private void loadConfig() throws IOException {
        if (!CONFIG_FILE.exists()) {
            CONFIG_FILE.createNewFile();
            saveConfig();

            System.out.println("Default config saved.");
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
