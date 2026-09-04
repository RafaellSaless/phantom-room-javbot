package org.javabot.Managers;

import org.bson.Document;
import org.javabot.repository.ServerRepository;
import org.javabot.repository.TicketRepository;

public class ConfigManager {

    private final ServerRepository serverRepository;
    private final TicketRepository ticketRepository;

    public ConfigManager() {
        this.serverRepository = new ServerRepository();
        this.ticketRepository = new TicketRepository();
    }

    public Document getServerConfig(String guildId) {
        return serverRepository.getServer(guildId);
    }

    public String getCreateTempCallId(String guildId) {
        return serverRepository.getCreateTempCallId(guildId);
    }

    public String getScheduleChannelId(String guildId) {
        return serverRepository.getScheduleChannelId(guildId);
    }

    public Document getTicketConfig(String guildId) {
        return ticketRepository.getTicketConfig(guildId);
    }

    public boolean isCallConfigured(String guildId) {
        String channelId = getCreateTempCallId(guildId);

        return channelId != null && !channelId.isBlank();
    }

    public boolean isScheduleConfigured(String guildId) {
        String channelId = getScheduleChannelId(guildId);

        return channelId != null && !channelId.isBlank();
    }

    public boolean isTicketConfigured(String guildId) {
        return getTicketConfig(guildId) != null;
    }
}