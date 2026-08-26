package org.javabot.models;

public class ServerConfig {
    private String guildId;
    private String canalId;

    public ServerConfig(String guildId, String canalId) {
        this.guildId = guildId;
        this.canalId = canalId;
    }

    /**
     * @return retorna o id do servidor
     */
    public String getGuildId() {
        return guildId;
    }

    /**
     * @param guildId altera o id do servidor registrado
     */
    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    /**
     * @return retorna o id do canal
     */
    public String getCanalId() {
        return canalId;
    }

    /**
     * @param canalId altera o id do canal registrado
     */
    public void setCanalId(String canalId) {
        this.canalId = canalId;
    }
}