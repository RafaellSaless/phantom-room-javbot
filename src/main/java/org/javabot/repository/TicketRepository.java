package org.javabot.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.Document;
import org.javabot.database.MongoManager;

/**
 * {
 *   _id: guildId,
 *   ticket: {
 *      channelId: "...",
 *      messageId: "...",
 *      categoryId: "...",
 *      nextTicketNumber: 1
 *   }
 * }
 */
public class TicketRepository {

    private final MongoCollection<Document> collection;

    public TicketRepository() {
        collection = MongoManager
                .getDatabase()
                .getCollection("ticket_configs");
    }

    public void saveConfiguration(
            String guildId,
            String channelId,
            String messageId,
            String categoryId
    ) {
        Document ticket = new Document()
                .append("channelId", channelId)
                .append("messageId", messageId)
                .append("categoryId", categoryId)
                .append("nextTicketNumber", 1);

        collection.updateOne(
                new Document("_id", guildId),
                new Document("$set", new Document("ticket", ticket)),
                new com.mongodb.client.model.UpdateOptions().upsert(true)
        );
    }

    public Document getConfiguration(String guildId) {
        Document server = collection.find(
                new Document("_id", guildId)
        ).first();

        if (server == null) {
            return null;
        }

        return server.get("ticket", Document.class);
    }

    public String getChannelId(String guildId) {
        Document ticket = getConfiguration(guildId);
        return ticket == null ? null : ticket.getString("channelId");
    }

    public String getMessageId(String guildId) {
        Document ticket = getConfiguration(guildId);
        return ticket == null ? null : ticket.getString("messageId");
    }

    public String getCategoryId(String guildId) {
        Document ticket = getConfiguration(guildId);
        return ticket == null ? null : ticket.getString("categoryId");
    }

    /**
     * Reserva o próximo número de ticket de forma atômica.
     * Preparado para a implementação da criação dos tickets.
     */
    public int getAndIncrementTicketNumber(String guildId) {
        Document result = collection.findOneAndUpdate(
                new Document("_id", guildId),
                new Document("$inc", new Document("ticket.nextTicketNumber", 1)),
                new FindOneAndUpdateOptions()
                        .upsert(false)
                        .returnDocument(ReturnDocument.BEFORE)
        );

        if (result == null) {
            throw new IllegalStateException(
                    "Configuração de ticket não encontrada para o servidor."
            );
        }

        Document ticket = result.get("ticket", Document.class);

        if (ticket == null) {
            throw new IllegalStateException(
                    "Configuração de ticket inválida para o servidor."
            );
        }

        Integer number = ticket.getInteger("nextTicketNumber");

        if (number == null) {
            throw new IllegalStateException(
                    "Contador de tickets não encontrado."
            );
        }

        return number;
    }
}
