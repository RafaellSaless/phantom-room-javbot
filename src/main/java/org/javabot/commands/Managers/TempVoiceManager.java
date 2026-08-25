package org.javabot.commands.Managers;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;

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

    

    public static boolean isDono(VoiceChannel canal, Member member) {
        return donos.getOrDefault(canal.getIdLong(), -1L)
                == member.getIdLong();
    }
}