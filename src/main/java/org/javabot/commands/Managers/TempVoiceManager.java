package org.javabot.commands.Managers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;

import java.util.HashMap;
import java.util.Map;

public class TempVoiceManager {

    private static final Map<Long, TempVoice> calls = new HashMap<>();


    public static void criarCall(GuildVoiceUpdateEvent event) {

        Guild guild = event.getGuild();
        Member member = event.getMember();

        VoiceChannel canalCriacao =
                event.getChannelJoined().asVoiceChannel();

        Category categoria =
                canalCriacao.getParentCategory();

        if (categoria == null) {
            System.out.println(
                    "O canal de criação não possui uma categoria."
            );
            return;
        }

        // Cria a Voice Channel
        guild.createVoiceChannel(
                "🔊 " + member.getEffectiveName(),
                categoria
        ).queue(voiceChannel -> {

            // Move o usuário para a nova call
            guild.moveVoiceMember(
                    member,
                    voiceChannel
            ).queue();


                TempVoice tempVoice = new TempVoice(
                        voiceChannel.getIdLong(),

                        member.getIdLong(),
                        guild.getIdLong()
                );

                calls.put(
                        voiceChannel.getIdLong(),
                        tempVoice
                );

                ConfigDMManager.enviarPainel(member, tempVoice);
            });
    }


    public static void verificarCallVazia(
            Guild guild,
            long canalId
    ) {

        // Não é uma call temporária
        if (!calls.containsKey(canalId)) {
            return;
        }

        VoiceChannel voiceChannel =
                guild.getVoiceChannelById(canalId);

        if (voiceChannel == null) {
            calls.remove(canalId);
            return;
        }

        // Ainda existem pessoas na call
        if (!voiceChannel.getMembers().isEmpty()) {
            return;
        }

        TempVoice tempVoice =
                calls.get(canalId);

        // Remove a Voice Channel
        voiceChannel.delete().queue();

        // Remove do sistema
        calls.remove(canalId);

        System.out.println(
                "Call temporária excluída: "
                        + canalId
        );
    }


    public static boolean isDono(
            long canalId,
            long usuarioId
    ) {

        TempVoice tempVoice =
                calls.get(canalId);

        if (tempVoice == null) {
            return false;
        }

        return tempVoice.getOwnerId() == usuarioId;
    }


    public static TempVoice getCall(long canalId) {
        return calls.get(canalId);
    }


    public static TempVoice getCallDoUsuario(
            long usuarioId
    ) {

        for (TempVoice tempVoice : calls.values()) {

            if (tempVoice.getOwnerId() == usuarioId) {
                return tempVoice;
            }
        }

        return null;
    }

    public static void excluirCall(
            net.dv8tion.jda.api.JDA jda,
            TempVoice tempVoice
    ) {

        Guild guild =
                jda.getGuildById(
                        tempVoice.getGuildId()
                );

        if (guild == null) {
            return;
        }

        VoiceChannel voiceChannel =
                guild.getVoiceChannelById(
                        tempVoice.getVoiceChannelId()
                );

        if (voiceChannel != null) {
            voiceChannel.delete().queue();
        }

        calls.remove(
                tempVoice.getVoiceChannelId()
        );
    }

    public static void alterarNome(
            net.dv8tion.jda.api.JDA jda,
            TempVoice call,
            String novoNome
    ) {

        Guild guild =
                jda.getGuildById(
                        call.getGuildId()
                );

        if (guild == null) {
            return;
        }

        VoiceChannel voiceChannel =
                guild.getVoiceChannelById(
                        call.getVoiceChannelId()
                );

        if (voiceChannel == null) {
            return;
        }

        voiceChannel.getManager()
                .setName("🔊 " + novoNome)
                .queue();
    }

    public static void alterarLimite(
            net.dv8tion.jda.api.JDA jda,
            TempVoice call,
            int limite
    ) {

        Guild guild =
                jda.getGuildById(
                        call.getGuildId()
                );

        if (guild == null) {
            return;
        }

        VoiceChannel voiceChannel =
                guild.getVoiceChannelById(
                        call.getVoiceChannelId()
                );

        if (voiceChannel == null) {
            return;
        }

        voiceChannel.getManager()
                .setUserLimit(limite)
                .queue();
    }

    public static void alternarPrivacidade(
            JDA jda,
            TempVoice call
    ) {

        Guild guild =
                jda.getGuildById(
                        call.getGuildId()
                );

        if (guild == null) {
            return;
        }

        VoiceChannel voiceChannel =
                guild.getVoiceChannelById(
                        call.getVoiceChannelId()
                );

        if (voiceChannel == null) {
            return;
        }

        PermissionOverride everyone =
                voiceChannel
                        .getPermissionOverride(
                                guild.getPublicRole()
                        );

        boolean privada =
                everyone != null &&
                        everyone.getDenied()
                                .contains(Permission.VOICE_CONNECT);

        if (privada) {

            // Torna pública
            voiceChannel.upsertPermissionOverride(
                    guild.getPublicRole()
            ).setAllowed(
                    Permission.VOICE_CONNECT
            ).queue();

        } else {

            // Torna privada
            voiceChannel.upsertPermissionOverride(
                    guild.getPublicRole()
            ).setDenied(
                    Permission.VOICE_CONNECT
            ).queue();
        }
    }

}