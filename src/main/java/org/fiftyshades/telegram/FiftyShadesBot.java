package org.fiftyshades.telegram;

import com.google.gson.Gson;
import com.jtelegram.api.TelegramBot;
import com.jtelegram.api.TelegramBotRegistry;
import com.jtelegram.api.chat.id.ChatId;
import com.jtelegram.api.events.message.TextMessageEvent;
import com.jtelegram.api.requests.message.send.SendText;
import com.jtelegram.api.update.PollingUpdateProvider;
import com.sun.net.httpserver.HttpServer;
import lombok.Getter;
import org.fiftyshades.telegram.commands.*;
import org.fiftyshades.telegram.listener.TopKekListener;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
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
    private HttpServer healthServer;

    public static void main(String[] args) throws Exception {
        INSTANCE.start(System.getenv("BOT_API_KEY"));
    }

    public void start(String apiKey) throws Exception {
        loadConfig();

        TelegramBotRegistry registry = TelegramBotRegistry.builder()
                .updateProvider(PollingUpdateProvider.builder()
                        .updateErrorHandler((e) -> {})
                        .build())
                .build();

        registry.registerBot(apiKey, (bot, error) -> {
            if (error != null) {
                System.out.println("Unable to login into telegram with provided api key! Printing error...");
                error.printStackTrace();
                return;
            }

            this.bot = bot;
            System.out.println("Successfully logged in as " + bot.getBotInfo().getUsername());
            setupHealthServer();

            bot.getCommandRegistry().registerCommand("whatwouldhappen", new WhatWouldHappen());
            bot.getCommandRegistry().registerCommand("aesthetic", new Aesthetic());
            bot.getCommandRegistry().registerCommand("choice", new Choice());
            bot.getCommandRegistry().registerCommand("eightball", new EightBall());
            bot.getCommandRegistry().registerCommand("idk", new Idk());
            bot.getCommandRegistry().registerCommand("lenny", new Lenny());

            bot.getEventRegistry().registerEvent(TextMessageEvent.class, new TopKekListener());

            bot.perform(SendText.builder()
                    .chatId(ChatId.of(config.getChatId()))
                    .text("Successfully logged in!")
                    .errorHandler((ex) -> {
                        System.out.println("Could not send login message to configured chat id, are you sure its correct?");
                        ex.printStackTrace();
                    })
                    .build());
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                bot.perform(SendText.builder()
                        .text("Bot shutting down...")
                        .chatId(ChatId.of(config.getChatId()))
                        .build())
        ));
    }

    private void setupHealthServer() {
        try {
            healthServer = HttpServer.create(new InetSocketAddress(8080), 0);
        } catch (IOException e) {
            System.out.println("Unable to start health server! Shutting down...");
            e.printStackTrace();
            System.exit(-127);
            return;
        }

        healthServer.createContext("/health").setHandler((he) -> {
            byte[] response = "OK".getBytes();
            he.sendResponseHeaders(200, response.length);

            OutputStream output = he.getResponseBody();

            output.write(response);
            output.flush();
            he.close();
        });

        healthServer.start();
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
