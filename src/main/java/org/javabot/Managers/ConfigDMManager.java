package org.javabot.Managers;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class ConfigDMManager {

    public static void enviarPainel(
            Member member,
            TempVoice tempVoice
    ) {

        member.getUser().openPrivateChannel().queue(
                privateChannel -> {

                    EmbedBuilder embed =
                            new EmbedBuilder();

                    embed.setTitle(
                            "⚙️ Configuração da sua Call"
                    );

                    embed.setDescription(
                            "Sua call temporária foi criada!\n\n" +
                                    "Use os botões abaixo para " +
                                    "administrar sua call."
                    );

                    embed.addField(
                            "👑 Dono",
                            member.getEffectiveName(),
                            false
                    );

                    embed.addField(
                            "🔊 Call",
                            "<#" +
                                    tempVoice.getVoiceChannelId()
                                    + ">",
                            false
                    );

                    privateChannel
                            .sendMessageEmbeds(
                                    embed.build()
                            )
                            .setActionRow(
                                    Button.primary(
                                            "call:nome",
                                            "✏️ Nome"
                                    ),
                                    Button.primary(
                                            "call:limite",
                                            "👥 Limite"
                                    ),
                                    Button.secondary(
                                            "call:privacidade",
                                            "🔒 Privacidade"
                                    ),
                                    Button.danger(
                                            "call:excluir",
                                            "🗑️ Excluir"
                                    )
                            )
                            .queue();
                }
        );
    }
}