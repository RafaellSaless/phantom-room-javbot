package org.javabot.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.javabot.database.MongoManager;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;

/** Repository responsável pela configuração e numeração dos tickets. */
public class TicketRepository {

    private final MongoCollection<Document> collection;

    public TicketRepository() {
        collection = MongoManager.getDatabase().getCollection("servers");
    }

    public void configureTicket(
            String guildId,
            String channelId,
            String messageId,
            String categoryId,
            String roleId
    ) {
        Bson update = Updates.combine(
                Updates.set("ticket.channelId", channelId),
                Updates.set("ticket.messageId", messageId),
                Updates.set("ticket.categoryId", categoryId),
                Updates.set("ticket.roleId", roleId),
                Updates.set("ticket.nextTicketNumber", 0L)
        );

        collection.updateOne(
                eq("_id", guildId),
                update,
                new com.mongodb.client.model.UpdateOptions().upsert(true)
        );
    }

    /** Retorna a configuração de tickets ou null se não estiver configurada. */
    public Document getTicketConfig(String guildId) {
        Document server = collection.find(eq("_id", guildId)).first();

        if (server == null) {
            return null;
        }

        return server.get("ticket", Document.class);
    }


    public Long reserveNextTicketNumber(String guildId) {
        Document result = collection.findOneAndUpdate(
                and(
                        eq("_id", guildId),
                        exists("ticket.categoryId", true)
                ),
                Updates.inc("ticket.nextTicketNumber", 1L),
                new FindOneAndUpdateOptions()
                        .returnDocument(ReturnDocument.AFTER)
        );

        if (result == null) {
            return null;
        }

        Document ticket = result.get("ticket", Document.class);
        if (ticket == null) {
            return null;
        }

        Number number = ticket.get("nextTicketNumber", Number.class);
        return number == null ? null : number.longValue();
    }
}
