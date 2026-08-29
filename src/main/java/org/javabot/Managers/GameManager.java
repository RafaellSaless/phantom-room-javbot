/**
 *
 */
package org.javabot.Managers;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bson.Document;
import org.javabot.repository.ScheduleRepository;
import org.javabot.repository.ServerRepository;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameManager {

    private static final ServerRepository serverRepository =
            new ServerRepository();

    private static final ScheduleRepository scheduleRepository =
            new ScheduleRepository();
    /*
     * Guarda temporariamente:
     *
     * usuário → servidor
     *
     * Isso é necessário porque o Modal será
     * respondido na DM e uma DM não possui Guild.
     */
    private static final Map<Long, String> configuracoes =
            new HashMap<>();


    /**
     * Inicia o processo de configuração.
     */
    public static void iniciarConfiguracao(
            long userId,
            String guildId
    ) {

        configuracoes.put(
                userId,
                guildId
        );
    }


    /**
     * Retorna o servidor associado ao usuário.
     */
    public static String getGuildId(
            long userId
    ) {

        return configuracoes.get(userId);
    }


    /**
     * Remove a configuração temporária.
     */
    public static void removerConfiguracao(
            long userId
    ) {

        configuracoes.remove(userId);
    }


    /**
     * Publica o agendamento no canal configurado.
     */
    public static void publicarAgendamento(
            JDA jda,
            long userId,
            String guildId,
            String jogo,
            String horario,
            int maxParticipantes,
            GameSchedule schedule
    ) {

        String chatTextId =
                serverRepository.getScheduleChannelId(
                        guildId
                );

        if (chatTextId == null) {
            return;
        }

        TextChannel channel =
                jda.getTextChannelById(
                        chatTextId
                );

        if (channel == null) {
            return;
        }


        EmbedBuilder embed =
                new EmbedBuilder();

        embed.setTitle(
                "🎮 Novo agendamento de jogo"
        );

        embed.setDescription(
                "Uma nova partida foi marcada!"
        );

        embed.setColor(
                new Color(88, 101, 242)
        );

        embed.addField(
                "🎮 Jogo",
                jogo,
                true
        );

        embed.addField(
                "🕐 Horário",
                horario,
                true
        );

        embed.addField(
                "👥 Participantes",
                String.valueOf(maxParticipantes),
                true
        );

        embed.addField(
                "👑 Criado por",
                "<@" + userId + ">",
                false
        );

        embed.setFooter(
                "Clique em participar para entrar no agendamento."
        );


        channel.sendMessageEmbeds(
                        embed.build()
                )
                .setActionRow(
                        Button.success(
                                "game:participar:" + schedule.getId(),
                                "🎮 Participar"
                        )
                )
                .queue(message -> {

                    schedule.setMessageId(
                            message.getId()
                    );

                    scheduleRepository.updateMessageId(
                            guildId,
                            schedule.getId(),
                            message.getId()
                    );
                });
    }

    public static void criarAgendamento(
            Guild guild,
            long ownerId,
            String jogo,
            String horario,
            int maxParticipantes
    ) {

        String scheduleId =
                UUID.randomUUID().toString();

        GameSchedule schedule =
                new GameSchedule(
                        scheduleId,
                        String.valueOf(ownerId),
                        jogo,
                        horario,
                        maxParticipantes
                );

        scheduleRepository.addSchedule(
                guild.getId(),
                schedule
        );

        Member owner =
                guild.getMemberById(ownerId);

        if (owner == null) {

            scheduleRepository.deleteSchedule(
                    guild.getId(),
                    scheduleId
            );

            return;
        }

        String nomeCategoria =
                owner.getEffectiveName()
                        + " - "
                        + jogo;

        guild.createCategory(nomeCategoria)
                .queue(category -> {

                    schedule.setCategoryId(
                            category.getId()
                    );

                    category
                            .upsertPermissionOverride(
                                    guild.getPublicRole()
                            )
                            .deny(
                                    Permission.VIEW_CHANNEL
                            )
                            .queue();

                    category
                            .upsertPermissionOverride(owner)
                            .grant(
                                    Permission.VIEW_CHANNEL,
                                    Permission.MESSAGE_SEND,
                                    Permission.MESSAGE_HISTORY
                            )
                            .queue();

                    criarCanais(
                            category,
                            schedule
                    );

                });
    }

    private static void criarCanais(
            Category category,
            GameSchedule schedule
    ) {

        category.createTextChannel("chat-geral")
                .queue(channel -> {

                    schedule.setChatId(
                            channel.getId()
                    );

                    atualizarIdsNoBanco(
                            category.getGuild().getId(),
                            schedule
                    );

                    channel.sendMessage(
                            "🎮 **"
                                    + schedule.getJogo()
                                    + "**\n\n"
                                    + "Bem-vindo ao grupo da partida!\n"
                                    + "🕐 Horário: `"
                                    + schedule.getHorario()
                                    + "`"
                    ).queue();
                });

        category.createTextChannel("configuracoes")
                .queue(channel -> {

                    schedule.setConfigId(
                            channel.getId()
                    );

                    atualizarIdsNoBanco(
                            category.getGuild().getId(),
                            schedule
                    );

                    enviarConfiguracoes(
                            channel,
                            schedule
                    );
                    atualizarIdsNoBanco(
                            category.getGuild().getId(),
                            schedule
                    );

                    publicarAgendamento(
                            category.getGuild().getJDA(),
                            Long.parseLong(schedule.getOwner()),
                            category.getGuild().getId(),
                            schedule.getJogo(),
                            schedule.getHorario(),
                            schedule.getMaxParticipantes(),
                            schedule
                    );
                });
    }

    private static void atualizarIdsNoBanco(
            String guildId,
            GameSchedule schedule
    ) {

        scheduleRepository.updateChannelIds(
                guildId,
                schedule.getId(),
                schedule.getCategoryId(),
                schedule.getChatId(),
                schedule.getConfigId()
        );
    }

    public static void deletarCanais(
            ButtonInteractionEvent event,
            GameSchedule schedule
    ) {
        Guild guild = event.getGuild();

        if (guild != null) {
            // 1. Deleta o chat (usando a String chatId)
            if (schedule.getChatId() != null) {
                var chatChannel = guild.getTextChannelById(schedule.getChatId());
                if (chatChannel != null) chatChannel.delete().queue();
            }

            // 2. Deleta o canal de configuração (usando a String configId)
            if (schedule.getConfigId() != null) {
                var configChannel = guild.getTextChannelById(schedule.getConfigId());
                if (configChannel != null) configChannel.delete().queue();
            }

            // 3. Deleta a categoria (usando a String categoryId)
            if (schedule.getCategoryId() != null) {
                var category = guild.getCategoryById(schedule.getCategoryId());
                if (category != null) category.delete().queue();
            }
        }

        scheduleRepository.deleteSchedule(
                guild.getId(),
                schedule.getId()
        );

    }

    private static void enviarConfiguracoes(
            TextChannel channel,
            GameSchedule schedule
    ) {

        EmbedBuilder embed =
                new EmbedBuilder();

        embed.setTitle(
                "⚙️ Configurações do agendamento"
        );

        embed.setDescription(
                "Utilize os botões abaixo para gerenciar sua participação."
        );

        embed.addField(
                "🎮 Jogo",
                schedule.getJogo(),
                true
        );

        embed.addField(
                "🕐 Horário",
                schedule.getHorario(),
                true
        );

        embed.addField(
                "👥 Participantes",
                schedule.quantidadeParticipantes()
                        + "/"
                        + schedule.getMaxParticipantes(),
                true
        );

        embed.setColor(
                new Color(88, 101, 242)
        );

        channel.sendMessageEmbeds(
                        embed.build()
                )
                .setActionRow(
                        Button.secondary(
                                "game:sair:" + schedule.getId(),
                                "🚪 Sair do grupo"
                        ),
                        Button.danger(
                                "game:fechar:" + schedule.getId(),
                                "🔒 Fechar agendamento"
                        )
                )
                .queue();
    }

    public static void adicionarPlayerAgendamento(
            String guildId,
            String scheduleId,
            long userId,
            ButtonInteractionEvent event
    ) {

        GameSchedule schedule =
                getAgendamento(
                        guildId,
                        scheduleId
                );

        if (schedule == null) {

            event.reply(
                            "❌ Este agendamento não existe mais ou foi fechado."
                    )
                    .setEphemeral(true)
                    .queue();

            return;
        }

        if (schedule.ehDono(userId)) {

            event.reply(
                            "⚠️ Você já é o criador deste agendamento!"
                    )
                    .setEphemeral(true)
                    .queue();

            return;
        }

        if (schedule.possuiParticipante(userId)) {

            event.reply(
                            "⚠️ Você já está participando deste agendamento!"
                    )
                    .setEphemeral(true)
                    .queue();

            return;
        }

        boolean adicionado =
                scheduleRepository.addPlayer(
                        guildId,
                        scheduleId,
                        String.valueOf(userId)
                );

        if (!adicionado) {

            event.reply(
                            "❌ Este agendamento já está lotado!"
                    )
                    .setEphemeral(true)
                    .queue();

            return;
        }

        event.reply(
                        "✅ Você entrou no agendamento com sucesso!"
                )
                .setEphemeral(true)
                .queue();
    }

    public static void removerPlayerAgendamento(
            String guildId,
            String scheduleId,
            long userId,
            ButtonInteractionEvent event
    ) {

        GameSchedule schedule =
                getAgendamento(
                        guildId,
                        scheduleId
                );

        if (schedule == null) {

            event.reply(
                            "❌ Este agendamento não existe mais ou foi fechado."
                    )
                    .setEphemeral(true)
                    .queue();

            return;
        }

        if (schedule.ehDono(userId)) {

            event.reply(
                            "❌ O criador não pode sair do próprio agendamento."
                    )
                    .setEphemeral(true)
                    .queue();

            return;
        }

        boolean removido =
                scheduleRepository.removePlayer(
                        guildId,
                        scheduleId,
                        String.valueOf(userId)
                );

        if (!removido) {

            event.reply(
                            "⚠️ Você não está participando deste agendamento."
                    )
                    .setEphemeral(true)
                    .queue();

            return;
        }

        event.reply(
                        "✅ Você saiu do agendamento."
                )
                .setEphemeral(true)
                .queue();
    }

    public static GameSchedule getAgendamento(
            String guildId,
            String scheduleId
    ) {

        Document document =
                scheduleRepository.getSchedule(
                        guildId,
                        scheduleId
                );

        if (document == null) {
            return null;
        }

        GameSchedule schedule =
                new GameSchedule(
                        document.getString("id"),
                        document.getString("owner"),
                        document.getString("jogo"),
                        document.getString("horario"),
                        document.getInteger(
                                "maxparticipantes"
                        )
                );

        schedule.setCategoryId(
                document.getString("categoryid")
        );

        schedule.setChatId(
                document.getString("chatid")
        );

        schedule.setConfigId(
                document.getString("configid")
        );

        schedule.setMessageId(
                document.getString("messageid")
        );

        List<String> jogadores =
                document.getList(
                        "jogadores",
                        String.class
                );

        if (jogadores != null) {

            schedule.getJogadores().clear();

            schedule.getJogadores().addAll(
                    jogadores
            );
        }

        return schedule;
    }
}

