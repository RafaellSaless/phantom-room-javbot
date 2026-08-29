package org.javabot.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.javabot.Managers.GameSchedule;
import org.javabot.database.MongoManager;

import java.util.ArrayList;
import java.util.List;

public class ScheduleRepository {

    private final MongoCollection<Document> collection;

    public ScheduleRepository() {

        collection = MongoManager
                .getDatabase()
                .getCollection("servers");
    }

    public void addSchedule(
            String guildId,
            GameSchedule schedule
    ) {

        Document scheduleDocument = new Document()
                .append("id", schedule.getId())
                .append("owner", schedule.getOwner())
                .append("jogo", schedule.getJogo())
                .append("horario", schedule.getHorario())
                .append(
                        "maxparticipantes",
                        schedule.getMaxParticipantes()
                )
                .append("categoryid", schedule.getCategoryId())
                .append("chatid", schedule.getChatId())
                .append("configid", schedule.getConfigId())
                .append("messageid", schedule.getMessageId())
                .append(
                        "jogadores",
                        new ArrayList<>(schedule.getJogadores())
                );

        collection.updateOne(
                Filters.eq("_id", guildId),
                Updates.push("schedules", scheduleDocument)
        );
    }

    public Document getSchedule(
            String guildId,
            String scheduleId
    ) {

        Document server = collection.find(
                Filters.eq("_id", guildId)
        ).first();

        if (server == null) {
            return null;
        }

        List<Document> schedules =
                server.getList(
                        "schedules",
                        Document.class
                );

        if (schedules == null) {
            return null;
        }

        for (Document schedule : schedules) {

            if (scheduleId.equals(
                    schedule.getString("id")
            )) {

                return schedule;
            }
        }

        return null;
    }

    public void deleteSchedule(
            String guildId,
            String scheduleId
    ) {

        collection.updateOne(
                Filters.eq("_id", guildId),
                Updates.pull(
                        "schedules",
                        new Document("id", scheduleId)
                )
        );
    }

    public boolean addPlayer(
            String guildId,
            String scheduleId,
            String playerId
    ) {

        Document schedule =
                getSchedule(guildId, scheduleId);

        if (schedule == null) {
            return false;
        }

        List<String> jogadores =
                schedule.getList(
                        "jogadores",
                        String.class
                );

        if (jogadores == null) {
            jogadores = new ArrayList<>();
        }

        if (jogadores.contains(playerId)) {
            return false;
        }

        int maxParticipantes =
                schedule.getInteger(
                        "maxparticipantes"
                );

        if (jogadores.size() >= maxParticipantes) {
            return false;
        }

        collection.updateOne(
                Filters.and(
                        Filters.eq("_id", guildId),
                        Filters.eq(
                                "schedules.id",
                                scheduleId
                        )
                ),
                Updates.addToSet(
                        "schedules.$.jogadores",
                        playerId
                )
        );

        return true;
    }

    public boolean removePlayer(
            String guildId,
            String scheduleId,
            String playerId
    ) {

        Document schedule =
                getSchedule(guildId, scheduleId);

        if (schedule == null) {
            return false;
        }

        List<String> jogadores =
                schedule.getList(
                        "jogadores",
                        String.class
                );

        if (jogadores == null ||
                !jogadores.contains(playerId)) {

            return false;
        }

        collection.updateOne(
                Filters.and(
                        Filters.eq("_id", guildId),
                        Filters.eq(
                                "schedules.id",
                                scheduleId
                        )
                ),
                Updates.pull(
                        "schedules.$.jogadores",
                        playerId
                )
        );

        return true;
    }

    public List<String> getPlayers(
            String guildId,
            String scheduleId
    ) {

        Document schedule =
                getSchedule(
                        guildId,
                        scheduleId
                );

        if (schedule == null) {
            return new ArrayList<>();
        }

        List<String> jogadores =
                schedule.getList(
                        "jogadores",
                        String.class
                );

        if (jogadores == null) {
            return new ArrayList<>();
        }

        return jogadores;
    }

    public boolean isPlayer(
            String guildId,
            String scheduleId,
            String playerId
    ) {

        return getPlayers(
                guildId,
                scheduleId
        ).contains(playerId);
    }

    public boolean isFull(
            String guildId,
            String scheduleId
    ) {

        Document schedule =
                getSchedule(
                        guildId,
                        scheduleId
                );

        if (schedule == null) {
            return false;
        }

        List<String> jogadores =
                schedule.getList(
                        "jogadores",
                        String.class
                );

        int max =
                schedule.getInteger(
                        "maxparticipantes"
                );

        return jogadores.size() >= max;
    }

    public void updateChannelIds(
            String guildId,
            String scheduleId,
            String categoryId,
            String chatId,
            String configId
    ) {

        collection.updateOne(
                Filters.and(
                        Filters.eq("_id", guildId),
                        Filters.eq(
                                "schedules.id",
                                scheduleId
                        )
                ),
                Updates.combine(
                        Updates.set(
                                "schedules.$.categoryid",
                                categoryId
                        ),
                        Updates.set(
                                "schedules.$.chatid",
                                chatId
                        ),
                        Updates.set(
                                "schedules.$.configid",
                                configId
                        )
                )
        );
    }

    public void updateMessageId(
            String guildId,
            String scheduleId,
            String messageId
    ) {

        collection.updateOne(
                Filters.and(
                        Filters.eq("_id", guildId),
                        Filters.eq(
                                "schedules.id",
                                scheduleId
                        )
                ),
                Updates.set(
                        "schedules.$.messageid",
                        messageId
                )
        );
    }
}