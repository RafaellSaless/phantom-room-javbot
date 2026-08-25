package org.javabot.commands.Listeners;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.javabot.commands.Managers.ConfigManager;
import org.javabot.commands.Managers.TempVoiceManager;

public class VoiceListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {

        if (event.getChannelJoined() != null) {

            String guildId = event.getGuild().getId();
            long canalEntradoId = event.getChannelJoined().getIdLong();

            String canalConfiguradoId =
                    ConfigManager.getCanalDoServidor(guildId);

            if (canalConfiguradoId != null &&
                    canalConfiguradoId.equals(String.valueOf(canalEntradoId))) {

                System.out.println(
                        event.getMember().getEffectiveName()
                                + " entrou no canal de criação de call."
                );

                TempVoiceManager.criarCall(event);
            }
        }

        if (event.getChannelLeft() != null) {

            long canalSaidoId =
                    event.getChannelLeft().getIdLong();

            TempVoiceManager.verificarCallVazia(
                    event.getGuild(),
                    canalSaidoId
            );
        }
    }

}