package org.javabot.models;

public class ServerConfig {
    private String guildId;
    private String canalId;

    public ServerConfig(String guildId, String canalId) {
        this.guildId = guildId;
        this.canalId = canalId;
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public String getCanalId() {
        return canalId;
    }

    public void setCanalId(String canalId) {
        this.canalId = canalId;
    }
}