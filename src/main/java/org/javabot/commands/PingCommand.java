package org.javabot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.javabot.Managers.Command;

import java.awt.*;

/**
 * Comando responsável por verificar a conectividade e o status do bot.
 */
@Command(
        name = "ping",
        description = "Verifica se o bot está online"
)
public class PingCommand extends Commands {

    @Override
    public void execute(MessageReceivedEvent event) {

        EmbedBuilder pingEmbed = new EmbedBuilder();
        pingEmbed.setColor(Color.red);
        pingEmbed.setTitle("⚡ !ping");
        pingEmbed.setDescription("Pong! 🏓");


        event.getChannel()
                .sendMessageEmbeds(pingEmbed.build())
                .queue();
    }
}