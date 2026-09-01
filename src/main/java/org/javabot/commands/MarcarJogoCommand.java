package org.javabot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.javabot.Managers.Command;
import org.javabot.Managers.GameManager;
import org.javabot.repository.ServerRepository;

@Command(
        name = "marcajogo",
        description = "Cria um novo agendamento de jogo"
)
public class MarcarJogoCommand extends Commands {

    private final ServerRepository serverRepository = new ServerRepository();

    @Override
    public void execute(MessageReceivedEvent event) {

        if (!event.isFromGuild()) {
            event.getChannel()
                    .sendMessage("❌ Esse comando precisa ser usado dentro de um servidor.")
                    .queue();
            return;
        }

        String guildId = event.getGuild().getId();
        String scheduleChannelId = serverRepository.getScheduleChannelId(guildId);

        // A configuração do marcajogo é armazenada no MongoDB.
        if (scheduleChannelId == null || scheduleChannelId.isBlank()) {
            event.getChannel()
                    .sendMessage("❌ Este servidor ainda não possui um canal configurado para os agendamentos.")
                    .queue();
            return;
        }

        // Além de existir no banco, o canal precisa existir no Discord.
        if (event.getGuild().getTextChannelById(scheduleChannelId) == null) {
            event.getChannel()
                    .sendMessage("❌ O canal configurado para os agendamentos não existe mais. Use `!configschedule <ID>` para configurar outro.")
                    .queue();
            return;
        }

        GameManager.iniciarConfiguracao(
                event.getAuthor().getIdLong(),
                guildId
        );

        event.getAuthor()
                .openPrivateChannel()
                .queue(channel -> channel.sendMessage(
                                        "🎮 **Marcar jogo**\n\n" +
                                                "Clique no botão abaixo para preencher as informações do seu agendamento."
                                )
                                .setActionRow(
                                        Button.primary("game:marcar", "📝 Preencher informações")
                                )
                                .queue(),
                        error -> GameManager.removerConfiguracao(event.getAuthor().getIdLong())
                );

        event.getChannel()
                .sendMessage("📩 Te enviei uma mensagem privada para configurar o agendamento.")
                .queue();
    }
}
