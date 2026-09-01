package org.javabot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.javabot.Managers.Command;
import org.javabot.repository.TicketRepository;

import java.awt.Color;

/** Configura o painel e a categoria/cargo usados pelo sistema de tickets. */
@Command(
        name = "configticket",
        description = "Configura o painel de tickets, categoria e cargo de atendimento"
)
public class ConfigTicketCommand extends Commands {

    private final TicketRepository repository = new TicketRepository();

    @Override
    public void execute(MessageReceivedEvent event) {

        if (event.getGuild() == null) {
            return;
        }


        String[] parts = event.getMessage().getContentRaw().split("\\s+");

        if (parts.length < 5) {
            event.getChannel().sendMessage(
                    "❌ Uso correto: `!configticket <canalId> <categoriaId> <cargoId> <mensagem>`"
            ).queue();
            return;
        }

        String serverName = event.getGuild().getName();
        String channelId = limparIdDiscord( parts[1] );
        String categoryId = limparIdDiscord( parts[2] );
        String roleId = limparIdDiscord( parts[3] );

        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 4; i < parts.length; i++) {
            if (i > 4) {
                messageBuilder.append(" ");
            }
            messageBuilder.append(parts[i]);
        }

        String panelMessage = messageBuilder.toString().trim();

        TextChannel channel = event.getGuild().getTextChannelById(channelId);
        Category category = event.getGuild().getCategoryById(categoryId);
        Role role = event.getGuild().getRoleById(roleId);



        if (channel == null) {
            event.getChannel().sendMessage("❌ O ID informado não corresponde a um canal de texto deste servidor.").queue();
            return;
        }

        if (category == null) {
            event.getChannel().sendMessage("❌ O ID informado não corresponde a uma categoria deste servidor.").queue();
            return;
        }

        if (role == null) {
            event.getChannel().sendMessage("❌ O ID informado não corresponde a um cargo deste servidor.").queue();
            return;
        }

        if (panelMessage.isBlank()) {
            event.getChannel().sendMessage("❌ A mensagem do painel não pode ficar vazia.").queue();
            return;
        }


        EmbedBuilder createTicket = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("🎫 Sistema de Tickets - " + serverName)
                .setDescription(panelMessage);

        String bannerUrl = event.getGuild().getBannerUrl();

        if (bannerUrl != null) {
            createTicket.setImage(bannerUrl);
        } else {
            String botAvatarUrl = event.getJDA().getSelfUser().getEffectiveAvatarUrl();
            createTicket.setImage(botAvatarUrl);
        }



                channel.sendMessageEmbeds(createTicket.build())
                .setActionRow(Button.primary("ticket:create", "🎫 Criar Ticket"))
                .queue(message -> {

                            repository.configureTicket(
                                    event.getGuild().getId(),
                                    channel.getId(),
                                    message.getId(),
                                    category.getId(),
                                    role.getId()
                            );

                            EmbedBuilder success = new EmbedBuilder()
                                    .setColor(Color.GREEN)
                                    .setTitle("🎫 Sistema de Tickets")
                                    .setDescription("O sistema de tickets foi configurado com sucesso.")
                                    .addField("Canal", channel.getAsMention(), true)
                                    .addField("Categoria", category.getName(), true)
                                    .addField("Cargo", role.getAsMention(), true);

                            event.getChannel().sendMessageEmbeds(success.build()).queue();
                        }, failure ->
                                event.getChannel().sendMessage("❌ Não consegui enviar a mensagem no canal informado. Verifique as permissões do bot.").queue()
                );
    }

    private String limparIdDiscord(String value) {
        return value
                .replace("<", "")
                .replace(">", "")
                .replace("#", "")
                .replace("@", "")
                .replace("&", "")
                .replace("(", "")
                .replace(")", "")
                .trim();
    }

}
