package org.javabot.repository;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.javabot.Managers.TempVoice;
import org.javabot.database.MongoManager;

import java.util.ArrayList;
import java.util.List;

public class TempVoiceRepository {

    private final MongoCollection<Document> collection;

    public TempVoiceRepository() {

        collection = MongoManager
                .getDatabase()
                .getCollection("tempvoices");
    }

    public void save(TempVoice tempVoice) {

        Document document = new Document(
                "_id",
                String.valueOf(
                        tempVoice.getVoiceChannelId()
                )
        )
                .append(
                        "guildid",
                        String.valueOf(
                                tempVoice.getGuildId()
                        )
                )
                .append(
                        "ownerid",
                        String.valueOf(
                                tempVoice.getOwnerId()
                        )
                );

        collection.insertOne(document);
    }

    public TempVoice getByChannelId(
            long channelId
    ) {

        Document document =
                collection.find(
                        new Document(
                                "_id",
                                String.valueOf(channelId)
                        )
                ).first();

        if (document == null) {
            return null;
        }

        return new TempVoice(
                Long.parseLong(
                        document.getString("_id")
                ),
                Long.parseLong(
                        document.getString("ownerid")
                ),
                Long.parseLong(
                        document.getString("guildid")
                )
        );
    }

    public List<TempVoice> getAll() {

        List<TempVoice> calls =
                new ArrayList<>();

        for (Document document : collection.find()) {

            calls.add(
                    new TempVoice(
                            Long.parseLong(
                                    document.getString("_id")
                            ),
                            Long.parseLong(
                                    document.getString("ownerid")
                            ),
                            Long.parseLong(
                                    document.getString("guildid")
                            )
                    )
            );
        }

        return calls;
    }

    public void delete(long channelId) {

        collection.deleteOne(
                new Document(
                        "_id",
                        String.valueOf(channelId)
                )
        );
    }
}