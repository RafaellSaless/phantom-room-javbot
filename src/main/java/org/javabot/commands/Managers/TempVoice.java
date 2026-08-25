package org.javabot.commands.Managers;

public class TempVoice {

    private final long voiceChannelId;
    private final long ownerId;
    private final long guildId;

    public TempVoice(
            long voiceChannelId,
            long ownerId,
            long guildId
    ) {
        this.voiceChannelId = voiceChannelId;
        this.ownerId = ownerId;
        this.guildId = guildId;
    }

    public long getVoiceChannelId() {
        return voiceChannelId;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public long getGuildId() {
        return guildId;
    }
}