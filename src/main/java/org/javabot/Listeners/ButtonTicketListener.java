package org.javabot.Listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bson.Document;
import org.javabot.repository.TicketRepository;

import java.awt.*;

public class ButtonTicketListener  {
    private final TicketRepository Ticketrepository;

    public ButtonTicketListener() {
        this.Ticketrepository = new TicketRepository();
    }

    public void handle(ButtonInteractionEvent event, String id) {
        if(id.equalsIgnoreCase("ticket:create")) {

            String guildId = event.getGuild().getId();
            Document config = Ticketrepository.getTicketConfig(guildId);

            if (config == null) {
                event.reply("❌ O sistema de tickets ainda não foi configurado neste servidor.")
                        .setEphemeral(true)
                        .queue();
                return;
            }

            String categoryId = config.getString("categoryId");
            String roleId = config.getString("roleId");

            Category category = event.getGuild().getCategoryById(categoryId);
            Role supportRole = event.getGuild().getRoleById(roleId);

            if (category == null) {
                event.reply("❌ A categoria configurada para os tickets não existe mais.")
                        .setEphemeral(true)
                        .queue();
                return;
            }

            if (supportRole == null) {
                event.reply("❌ O cargo configurado para atendimento não existe mais.")
                        .setEphemeral(true)
                        .queue();
                return;
            }

            Long ticketNumber = Ticketrepository.reserveNextTicketNumber(guildId);

            if (ticketNumber == null) {
                event.reply("❌ Não foi possível reservar um número para o ticket.")
                        .setEphemeral(true)
                        .queue();
                return;
            }

            String ticketName = String.format("ticket-%03d", ticketNumber);

            event.getGuild()
                    .createTextChannel(ticketName, category)
                    .queue(channel -> TicketConfigurarPermissoes(
                                    event,
                                    channel,
                                    supportRole
                            ), failure ->
                                    event.reply("❌ Não consegui criar o ticket. Verifique as permissões do bot.")
                                            .setEphemeral(true)
                                            .queue()
                    );

        }

        if(id.equalsIgnoreCase("ticket:fechar")) {
            TextChannel channel = event.getChannel().asTextChannel();
            Member member = event.getMember();
            event.deferEdit();


            channel.upsertPermissionOverride(member)
                    .deny(Permission.MESSAGE_SEND)
                    .queue();

            channel.sendMessage("Ticket fechado!").queue();

        }

        if(id.equalsIgnoreCase("ticket:deletar")) {
            TextChannel channel = event.getChannel().asTextChannel();
            Member member = event.getMember();

            event.deferEdit();
            String roleId = TicketRepository.getRoleId(event.getMessage().getGuildId());

            boolean hasRole = member.getRoles().stream().anyMatch(role -> role.getId().equals(roleId));
            boolean isAdmin = member.hasPermission(Permission.ADMINISTRATOR);

            if(!hasRole && !isAdmin) {
                return;
            }

            channel.delete().queue();

        }
    }

    private void TicketConfigurarPermissoes(
            ButtonInteractionEvent event,
            TextChannel channel,
            Role supportRole
    ) {

        String serverName = event.getGuild().getName();

        Member member = event.getMember();

        // Ninguém vê o ticket por padrão.
        channel.upsertPermissionOverride(event.getGuild().getPublicRole())
                .deny(Permission.VIEW_CHANNEL)
                .queue();

        // O usuário que abriu o ticket pode visualizar e escrever.
        channel.upsertPermissionOverride(member)
                .grant(
                        Permission.VIEW_CHANNEL,
                        Permission.MESSAGE_SEND,
                        Permission.MESSAGE_HISTORY,
                        Permission.MESSAGE_ATTACH_FILES
                )
                .queue();

        // O cargo de atendimento pode visualizar e responder.
        channel.upsertPermissionOverride(supportRole)
                .grant(
                        Permission.VIEW_CHANNEL,
                        Permission.MESSAGE_SEND,
                        Permission.MESSAGE_HISTORY,
                        Permission.MESSAGE_ATTACH_FILES
                )
                .queue();

        event.reply("✅ Seu ticket foi criado: " + channel.getAsMention())
                .setEphemeral(true)
                .queue();

        EmbedBuilder ticketsucess = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("🎫 Sistema de Tickets - " + serverName)
                .setDescription("Bem vindo ao sistema de ticket!" + channel.getName())
                .addField(" - ",  "um dos nossos " + supportRole.getAsMention() + " poderá responder aqui!", false)
                .addField(" - ", "Seja bem vindo " + member.getAsMention(), false);

        String bannerUrlPlayer = member.getAvatarUrl();
        String bannerUrlGuild = event.getGuild().getBannerUrl();


        if (bannerUrlPlayer != null) {
            ticketsucess.setImage(bannerUrlPlayer);
        } else if(bannerUrlGuild != null) {
            ticketsucess.setImage(bannerUrlGuild);
        }else {
            String botAvatarUrl = event.getJDA().getSelfUser().getEffectiveAvatarUrl();
            ticketsucess.setImage(botAvatarUrl);
        }

        channel.sendMessageEmbeds(ticketsucess.build())
                .setActionRow(
                        net.dv8tion.jda.api.interactions.components.buttons.Button.primary("ticket:fechar", "🔒FECHAR TICKET "),
                        Button.secondary("ticket:deletar", "🗑️DELETAR TICKET")
                )
                .queue();
        channel.sendMessage(supportRole.getAsMention()).queue();
    }
}