package org.javabot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.javabot.Managers.Command;
import org.javabot.Managers.ConfigManager;
import org.javabot.Managers.GameManager;

@Command(
        name = "marcajogo",
        description = "Cria um novo agendamento de jogo"
)
public class MarcarJogoCommand extends Commands {

    @Override
    public void execute(MessageReceivedEvent event) {

        if (!event.isFromGuild()) {
            event.getChannel()
                    .sendMessage(
                            "❌ Esse comando precisa ser usado dentro de um servidor."
                    )
                    .queue();

            return;
        }

        String guildId = event.getGuild().getId();

        /*
         * Verifica se o servidor possui um canal
         * configurado para receber os agendamentos.
         */
        String chatTextId =
                ConfigManager.getCanalDoServidor(
                        guildId,
                        "chattextid"
                );

        if (chatTextId == null) {

            event.getChannel()
                    .sendMessage(
                            "❌ Este servidor ainda não possui um canal configurado para os agendamentos."
                    )
                    .queue();

            return;
        }

        /*
         * Guarda temporariamente qual servidor
         * iniciou o processo.
         */
        GameManager.iniciarConfiguracao(
                event.getAuthor().getIdLong(),
                guildId
        );

        /*
         * Abre a DM do usuário.
         */
        event.getAuthor()
                .openPrivateChannel()
                .queue(channel -> {

                    channel.sendMessage(
                                    "🎮 **Marcar jogo**\n\n" +
                                            "Clique no botão abaixo para preencher " +
                                            "as informações do seu agendamento."
                            )
                            .setActionRow(
                                    Button.primary(
                                            "game:marcar",
                                            "📝 Preencher informações"
                                    )
                            )
                            .queue();
                });

        event.getChannel()
                .sendMessage(
                        "📩 Te enviei uma mensagem privada para configurar o agendamento."
                )
                .queue();
    }
}