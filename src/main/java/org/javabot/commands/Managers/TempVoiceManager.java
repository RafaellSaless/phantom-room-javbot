package org.javabot.commands.Managers;


import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import org.intellij.lang.annotations.Identifier;
import org.jetbrains.annotations.NotNull;
import org.javabot.commands.Managers.ConfigManager;


import java.util.HashMap;
import java.util.Map;

public class TempVoiceManager {

    private static final Map<Long, Long> donos = new HashMap<>();

    public static void criarCall(GuildVoiceUpdateEvent event) {

        Guild guild = event.getGuild();
        Member member = event.getMember();

        Category categoria = event.getChannelJoined()
                .asVoiceChannel()
                .getParentCategory();

        guild.createVoiceChannel(
                "🔊 " + member.getEffectiveName(),
                categoria
        ).queue(canal -> {


            donos.put(canal.getIdLong(), member.getIdLong());


            guild.moveVoiceMember(member, canal).queue();

            System.out.println(
                    "Call criada: " + canal.getName()
            );
        });
    }

    public static void verificarCallVazia(Guild guild, long canalId) {

        IO.println("verificoy");
        if (!donos.containsKey(canalId))
            return;

        VoiceChannel canal = guild.getVoiceChannelById(canalId);

        if (canal == null) {
            IO.println("Null");
            return;
        }


        if (canal.getMembers().isEmpty()) {

            IO.println("saiu");
            donos.remove(canalId);

            canal.delete().queue();

            System.out.println(
                    "Call temporária excluída: " + canalId
            );
        }
    }


    public static boolean isDono(VoiceChannel canal, Member member) {
        return donos.getOrDefault(canal.getIdLong(), -1L)
                == member.getIdLong();
    }

}