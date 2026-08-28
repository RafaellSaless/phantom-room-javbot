package org.javabot.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoManager {

    private static final MongoClient CLIENT =
            MongoClients.create(System.getProperty("MONGODB_URI"));

    private static final MongoDatabase DATABASE =
            CLIENT.getDatabase(System.getProperty("MONGODB_DATABASE"));

    public static MongoDatabase getDatabase() {
        return DATABASE;
    }
}