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
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.javabot.Main;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameManager {

    private static final Map<String, GameSchedule> agendamentos = new HashMap<>();

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
                ConfigManager.getCanalDoServidor(
                        guildId,
                        "chattextid"
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
                .queue();
    }

    public static void criarAgendamento(
            Guild guild,
            long ownerId,
            String jogo,
            String horario,
            int maxParticipantes
    ) {

        String scheduleId = UUID.randomUUID().toString();

        GameSchedule schedule =
                new GameSchedule(
                        scheduleId,
                        guild.getId(),
                        ownerId,
                        jogo,
                        horario,
                        maxParticipantes
                );

        // O criador já é participante
        schedule.adicionarParticipante(ownerId);

        agendamentos.put(
                scheduleId,
                schedule
        );

        Member owner =
                guild.getMemberById(ownerId);

        if (owner == null) {
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

                    // @everyone não pode visualizar
                    category
                            .upsertPermissionOverride(
                                    guild.getPublicRole()
                            )
                            .deny(
                                    Permission.VIEW_CHANNEL
                            )
                            .queue();

                    // Criador recebe acesso
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

                    publicarAgendamento(
                            guild.getJDA(),
                            schedule.getOwnerId(),
                            schedule.getGuildId(),
                            schedule.getJogo(),
                            schedule.getHorario(),
                            schedule.getMaxParticipantes(),
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

                    enviarConfiguracoes(
                            channel,
                            schedule
                    );
                });
    }

    public static void deletarCanais(
            ButtonInteractionEvent event,
            GameSchedule schedule
    ) {

        Guild guild = event.getJDA().getGuildById(schedule.getGuildId());

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

    public static void adicionarPlayerAgendamento(String scheduleId, long userId, ButtonInteractionEvent event) {
        GameSchedule schedule = getAgendamento(scheduleId);

        if (schedule == null) {
            event.reply("❌ Este agendamento não existe mais ou foi fechado.").setEphemeral(true).queue();
            return;
        }

        if (schedule.possuiParticipante(userId)) {
            event.reply("⚠️ Você já está participando deste agendamento!").setEphemeral(true).queue();
            return;
        }

        boolean adicionado = schedule.adicionarParticipante(userId);

        if (!adicionado) {
            // Se retornar false, significa que atingiu o limite máximo (está cheio)
            event.reply("❌ Este agendamento já está lotado!").setEphemeral(true).queue();
            return;
        }

        event.reply("✅ Você entrou no agendamento com sucesso!").setEphemeral(true).queue();
    }

    /**
     * Busca um agendamento pelo seu ID único (UUID).
     * @param scheduleId O ID do agendamento.
     * @return O objeto GameSchedule correspondente, ou null se não encontrar.
     */
    public static GameSchedule getAgendamento(String scheduleId) {
        return agendamentos.get(scheduleId);
    }

    /**
     * Remove um agendamento do mapa global.
     * @param scheduleId O ID do agendamento que será deletado.
     */
    public static void removerAgendamento(String scheduleId) {
        agendamentos.remove(scheduleId);
    }

}

