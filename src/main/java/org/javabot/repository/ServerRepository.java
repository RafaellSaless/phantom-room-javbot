package org.javabot.repository;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.javabot.database.MongoManager;

public class ServerRepository {

    private final MongoCollection<Document> collection;

    public ServerRepository() {

        collection = MongoManager
                .getDatabase()
                .getCollection("servers");
    }

    public void createServer(String guildId) {

        if (exists(guildId)) {
            return;
        }

        Document document =
                new Document("_id", guildId)
                        .append("id_createtempcall", "")
                        .append("id_schedulechannel", "")
                        .append("schedules", new java.util.ArrayList<>());

        collection.insertOne(document);
    }

    public boolean exists(String guildId) {

        return collection.find(
                new Document("_id", guildId)
        ).first() != null;
    }

    public Document getServer(String guildId) {

        return collection.find(
                new Document("_id", guildId)
        ).first();
    }

    public void configureServer(
            String guildId,
            String createTempCallId,
            String scheduleChannelId
    ) {

        collection.updateOne(

                new Document("_id", guildId),

                new Document(
                        "$set",
                        new Document()
                                .append(
                                        "id_createtempcall",
                                        createTempCallId
                                )
                                .append(
                                        "id_schedulechannel",
                                        scheduleChannelId
                                )
                )
        );
    }

    public void createIfNotExists(String guildId) {

        if (collection.find(new Document("_id", guildId)).first() == null) {

            Document document = new Document("_id", guildId)
                    .append("id_createtempcall", "")
                    .append("id_schedulechannel", "")
                    .append("schedules", new java.util.ArrayList<>());

            collection.insertOne(document);
        }
    }

    public String getCreateTempCallId(String guildId) {

        Document server = getServer(guildId);

        if (server == null) {
            return null;
        }

        return server.getString("id_createtempcall");
    }

    public String getScheduleChannelId(String guildId) {

        Document server = getServer(guildId);

        if (server == null) {
            return null;
        }

        return server.getString("id_schedulechannel");
    }

    public void deleteServer(String guildId) {

        collection.deleteOne(
                new Document("_id", guildId)
        );
    }
}