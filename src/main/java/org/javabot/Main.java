package org.javabot;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.javabot.commands.managers.CommandListener;
import org.javabot.commands.managers.CommandManager;

import java.util.EnumSet;

public class Main {

    static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();

        String token = dotenv.get("DISCORD_TOKEN");

        CommandManager.loadCommands();

        JDA jda = JDABuilder.create(
                        token,
                        EnumSet.allOf(GatewayIntent.class)

                )
                .addEventListeners(new CommandListener())
                .build();

    }
}

