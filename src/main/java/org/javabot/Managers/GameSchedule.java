package org.javabot.Managers;

import java.util.HashSet;
import java.util.Set;

public class GameSchedule {

    private final String id;
    private final String guildId;
    private final long ownerId;

    private final String jogo;
    private final String horario;
    private final int maxParticipantes;

    private String categoryId;
    private String chatId;
    private String configId;
    private String messageId;

    private final Set<Long> participantes =
            new HashSet<>();


    public GameSchedule(
            String id,
            String guildId,
            long ownerId,
            String jogo,
            String horario,
            int maxParticipantes
    ) {

        this.id = id;
        this.guildId = guildId;
        this.ownerId = ownerId;
        this.jogo = jogo;
        this.horario = horario;
        this.maxParticipantes = maxParticipantes;
    }


    public String getId() {
        return id;
    }


    public String getGuildId() {
        return guildId;
    }


    public long getOwnerId() {
        return ownerId;
    }


    public String getJogo() {
        return jogo;
    }


    public String getHorario() {
        return horario;
    }


    public int getMaxParticipantes() {
        return maxParticipantes;
    }


    public String getCategoryId() {
        return categoryId;
    }


    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }


    public String getChatId() {
        return chatId;
    }


    public void setChatId(String chatId) {
        this.chatId = chatId;
    }


    public String getConfigId() {
        return configId;
    }


    public void setConfigId(String configId) {
        this.configId = configId;
    }


    public String getMessageId() {
        return messageId;
    }


    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }


    public Set<Long> getParticipantes() {
        return participantes;
    }


    public boolean adicionarParticipante(
            long userId
    ) {

        if (participantes.size()
                >= maxParticipantes) {

            return false;
        }

        return participantes.add(userId);
    }


    public boolean removerParticipante(
            long userId
    ) {

        return participantes.remove(
                userId
        );
    }


    public boolean possuiParticipante(
            long userId
    ) {

        return participantes.contains(
                userId
        );
    }


    public boolean estaCheio() {

        return participantes.size()
                >= maxParticipantes;
    }


    public int quantidadeParticipantes() {

        return participantes.size();
    }


    public boolean ehDono(
            long userId
    ) {

        return ownerId == userId;
    }
}