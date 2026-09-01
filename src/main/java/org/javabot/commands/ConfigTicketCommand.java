package org.javabot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.javabot.Managers.Command;
import org.javabot.repository.TicketRepository;

import java.awt.Color;

/**
 * Configura o painel do sistema de tickets de um servidor.
 *
 * Uso:
 * !configticket <ID_DO_CANAL> <ID_DA_CATEGORIA> <mensagem>
 *
 * Exemplo:
 * !configticket 123456789012345678 987654321098765432 Precisa de ajuda? Clique no botão abaixo.
 */
@Command(
        name = "configticket",
        description = "Configura o canal, categoria e painel do sistema de tickets"
)
public class ConfigTicketCommand extends Commands {

    private final TicketRepository repository = new TicketRepository();

    @Override
    public void execute(MessageReceivedEvent event) {

        if (event.getGuild() == null) {
            return;
        }


        String[] parts = event.getMessage()
                .getContentRaw()
                .trim()
                .split("\\s+", 4);

        if (parts.length < 4) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(Color.RED)
                    .setTitle("⚡ !configticket")
                    .setDescription("Configure o painel de tickets.")
                    .addField(
                            "Uso",
                            "`!configticket <ID_DO_CANAL> <ID_DA_CATEGORIA> <mensagem>`",
                            false
                    )
                    .addField(
                            "Exemplo",
                            "`!configticket 123456789012345678 987654321098765432 Precisa de ajuda? Clique no botão abaixo.`",
                            false
                    );

            event.getChannel().sendMessageEmbeds(embed.build()).queue();
            return;
        }

        String channelId = limparIdDiscord(parts[1]);
        String categoryId = limparIdDiscord(parts[2]);
        String panelMessage = parts[3].trim();

        if (!channelId.matches("\\d+") || !categoryId.matches("\\d+")) {
            event.getChannel()
                    .sendMessage("❌ O ID do canal ou da categoria é inválido.")
                    .queue();
            return;
        }

        if (panelMessage.isBlank()) {
            event.getChannel()
                    .sendMessage("❌ A mensagem do painel não pode estar vazia.")
                    .queue();
            return;
        }

        TextChannel channel = event.getGuild().getTextChannelById(channelId);
        Category category = event.getGuild().getCategoryById(categoryId);

        if (channel == null) {
            event.getChannel()
                    .sendMessage("❌ O ID informado não corresponde a um canal de texto deste servidor.")
                    .queue();
            return;
        }

        if (category == null) {
            event.getChannel()
                    .sendMessage("❌ O ID informado não corresponde a uma categoria deste servidor.")
                    .queue();
            return;
        }

        // Se já existir um painel configurado, tenta apagar a mensagem antiga.
        String oldChannelId = repository.getChannelId(event.getGuild().getId());
        String oldMessageId = repository.getMessageId(event.getGuild().getId());

        if (oldChannelId != null && oldMessageId != null) {
            TextChannel oldChannel = event.getGuild().getTextChannelById(oldChannelId);

            if (oldChannel != null) {
                oldChannel.deleteMessageById(oldMessageId).queue(
                        ignored -> {},
                        ignored -> {}
                );
            }
        }

        channel.sendMessage(panelMessage)
                .setActionRow(
                        Button.primary(
                                "ticket:create",
                                "🎫 Criar Ticket"
                        )
                )
                .queue(message -> {

                    repository.saveConfiguration(
                            event.getGuild().getId(),
                            channel.getId(),
                            message.getId(),
                            category.getId()
                    );

                    EmbedBuilder success = new EmbedBuilder()
                            .setColor(Color.GREEN)
                            .setTitle("⚡ !configticket")
                            .setDescription("O sistema de tickets foi configurado com sucesso.")
                            .addField("Canal", channel.getAsMention(), true)
                            .addField("Categoria", category.getName(), true)
                            .addField("Painel", message.getJumpUrl(), false);

                    event.getChannel()
                            .sendMessageEmbeds(success.build())
                            .queue();
                }, error -> event.getChannel()
                        .sendMessage("❌ Não consegui enviar o painel para o canal informado. Verifique minhas permissões.")
                        .queue());
    }

    private String limparIdDiscord(String value) {
        return value
                .replace("<", "")
                .replace(">", "")
                .replace("#", "")
                .trim();
    }
}
