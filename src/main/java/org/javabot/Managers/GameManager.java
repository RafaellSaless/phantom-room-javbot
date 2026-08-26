/**
 *
 */
package org.javabot.Managers;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class GameManager {

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
            int maxParticipantes
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
                "0/" + maxParticipantes,
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
                                "game:participar",
                                "🎮 Participar"
                        )
                )
                .queue();
    }
}