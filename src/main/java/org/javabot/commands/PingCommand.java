package org.javabot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.javabot.commands.Managers.Command;

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

        event.getChannel()
                .sendMessage("Pong! 🏓")
                .queue();
    }
}