package org.javabot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.javabot.commands.managers.Command;
import org.javabot.commands.managers.CommandManager;

import java.awt.*;

@Command(
        name = "help",
        description = "comando de ajuda do bot;"
)
public class HelpCommand extends Commands{

    @Override
    public void execute(MessageReceivedEvent event) {

        EmbedBuilder embed = new EmbedBuilder();

        embed.setColor(new Color(88, 101, 242));

        embed.setTitle("⚡ Central de Comandos");
        embed.setDescription(
                "╰・**Olá!** Aqui estão os comandos disponíveis.\n" +
                        "╰・Use `!` antes do comando para executá-lo."
        );

        embed.addBlankField(false);

        for (Commands command : CommandManager.getCommands().values()) {

            embed.addField(
                    "╭─「 `" + command.getCommandName() + "` 」",
                    "╰─➤ " + command.getDescription(),
                    false
            );

        }


        event.getChannel()
                .sendMessageEmbeds(embed.build())
                .queue();


    }
    }
