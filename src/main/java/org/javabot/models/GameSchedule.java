package org.javabot.models;

import java.util.ArrayList;
import java.util.List;

public class GameSchedule {

    private String id;
    private String owner;
    private String jogo;
    private String horario;
    private int maxParticipantes;

    private String categoryId;
    private String chatId;
    private String configId;
    private String messageId;

    private List<String> jogadores;

    public GameSchedule(
            String id,
            String owner,
            String jogo,
            String horario,
            int maxParticipantes
    ) {
        this.id = id;
        this.owner = owner;
        this.jogo = jogo;
        this.horario = horario;
        this.maxParticipantes = maxParticipantes;

        this.jogadores = new ArrayList<>();
        this.jogadores.add(owner);
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
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

    public String getChatId() {
        return chatId;
    }

    public String getConfigId() {
        return configId;
    }

    public String getMessageId() {
        return messageId;
    }

    public List<String> getJogadores() {
        return jogadores;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}