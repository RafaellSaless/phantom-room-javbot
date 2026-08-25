package org.javabot.commands.Listeners;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.javabot.commands.Managers.TempVoiceManager;
import org.jetbrains.annotations.NotNull;


public class VoiceListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
        if (event.getChannelJoined() != null) {

            String guildId = event.getGuild().getId();
            long canalEntradoId = event.getChannelJoined().getIdLong();

            // Busca direto o canal configurado para este servidor no JSON
            String canalConfiguradoId = org.javabot.commands.Managers.ConfigManager.getCanalDoServidor(guildId);

            if (canalConfiguradoId != null && canalConfiguradoId.equals(String.valueOf(canalEntradoId))) {
                String nomeUsuario = event.getMember().getUser().getName();
                System.out.println(nomeUsuario + " entrou no canal de criação de call do servidor " + guildId + "!");

                TempVoiceManager.criarCall(event);
            }
        }
    }
}