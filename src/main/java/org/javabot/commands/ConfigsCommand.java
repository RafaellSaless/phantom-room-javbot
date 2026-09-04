package org.javabot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bson.Document;
import org.javabot.Managers.Command;

import java.awt.Color;

@Command(
        name = "configs",
        description = "Exibe as configurações do servidor"
)
public class ConfigsCommand extends Commands {

    private final org.javabot.Managers.ConfigManager configManager;

    public ConfigsCommand() {
        this.configManager = new org.javabot.Managers.ConfigManager();
    }

    @Override
    public void execute(MessageReceivedEvent event) {


        if (event.getGuild() == null) {
            return;
        }

        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.getChannel()
                    .sendMessage("❌ Apenas administradores podem configurar o sistema de tickets.")
                    .queue();
            return;
        }

        String guildId = event.getGuild().getId();

        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("⚙️ Configurações do Servidor");
        embed.setDescription(
                "Confira abaixo as configurações atuais dos sistemas."
        );
        embed.setColor(Color.decode("#5865F2"));

        // =========================
        // CANAIS DE VOZ
        // =========================

        String callStatus;

        if (configManager.isCallConfigured(guildId)) {

            String channelId =
                    configManager.getCreateTempCallId(guildId);

            var channel =
                    event.getGuild().getVoiceChannelById(channelId);

            if (channel != null) {

                callStatus =
                        "✅ Configurado\n" +
                                "**Canal gatilho:** " +
                                channel.getAsMention();

            } else {

                callStatus =
                        "⚠️ Configurado, mas o canal não foi encontrado.";
            }

        } else {

            callStatus = "❌ Não configurado";
        }

        embed.addField(
                "🎙️ Canais de Voz",
                callStatus,
                false
        );

        // =========================
        // SCHEDULE
        // =========================

        String scheduleStatus;

        if (configManager.isScheduleConfigured(guildId)) {

            String channelId =
                    configManager.getScheduleChannelId(guildId);

            var channel =
                    event.getGuild().getTextChannelById(channelId);

            if (channel != null) {

                scheduleStatus =
                        "✅ Configurado\n" +
                                "**Canal:** " +
                                channel.getAsMention();

            } else {

                scheduleStatus =
                        "⚠️ Configurado, mas o canal não foi encontrado.";
            }

        } else {

            scheduleStatus = "❌ Não configurado";
        }

        embed.addField(
                "🎮 Gameplays",
                scheduleStatus,
                false
        );

        // =========================
        // TICKET
        // =========================

        String ticketStatus;

        Document ticketConfig =
                configManager.getTicketConfig(guildId);

        if (ticketConfig != null) {

            String channelId =
                    ticketConfig.getString("channelId");

            String categoryId =
                    ticketConfig.getString("categoryId");

            String roleId =
                    ticketConfig.getString("roleId");

            StringBuilder ticketInfo =
                    new StringBuilder("✅ Configurado\n");

            var channel =
                    event.getGuild().getTextChannelById(channelId);

            var category =
                    event.getGuild().getCategoryById(categoryId);

            var role =
                    event.getGuild().getRoleById(roleId);

            ticketInfo.append("**Canal:** ")
                    .append(
                            channel != null
                                    ? channel.getAsMention()
                                    : "⚠️ Não encontrado"
                    )
                    .append("\n");

            ticketInfo.append("**Categoria:** ")
                    .append(
                            category != null
                                    ? category.getAsMention()
                                    : "⚠️ Não encontrada"
                    )
                    .append("\n");

            ticketInfo.append("**Cargo:** ")
                    .append(
                            role != null
                                    ? role.getAsMention()
                                    : "⚠️ Não encontrado"
                    );

            ticketStatus = ticketInfo.toString();

        } else {

            ticketStatus = "❌ Não configurado";
        }

        embed.addField(
                "🎫 Tickets",
                ticketStatus,
                false
        );

        // =========================
        // FOOTER
        // =========================

        embed.setFooter(
                "PhantomGM"
        );

        event.getChannel()
                .sendMessageEmbeds(embed.build())
                .queue();
    }
}