package org.javabot;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.javabot.Listeners.ButtonListenerController;
import org.javabot.Listeners.CommandListener;
import org.javabot.Listeners.ModalListener;
import org.javabot.Listeners.VoiceListener;
import org.javabot.Managers.CommandManager;
import org.javabot.Managers.TempVoiceManager;

import java.util.EnumSet;

public class Main {

    static void main(String[] args) throws InterruptedException {
        Dotenv dotenv = Dotenv.configure().systemProperties().load();


        String token = dotenv.get("DISCORD_TOKEN");

        CommandManager.loadCommands();

        JDA jda = JDABuilder.create(
                        token,
                        EnumSet.allOf(GatewayIntent.class)

                )
                .addEventListeners(
                        new CommandListener(),
                        new VoiceListener(),
                        new ButtonListenerController(),
                        new ModalListener()
                )
                .build()
                .awaitReady();

        TempVoiceManager.carregarCalls(jda);

    }
}