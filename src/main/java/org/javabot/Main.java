package org.javabot;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.javabot.commands.Listeners.ButtonListener;
import org.javabot.commands.Listeners.CommandListener;
import org.javabot.commands.Listeners.ModalListener;
import org.javabot.commands.Listeners.VoiceListener;
import org.javabot.commands.Managers.CommandManager;

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
                .addEventListeners(
                        new CommandListener(),
                        new VoiceListener(),
                        new ButtonListener(),
                        new ModalListener()
                )
                .build();

    }
}