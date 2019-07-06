package main.java.example.game.mongodb;

import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import main.java.example.game.Utils.EnvironmentVariables;

/**
 * Singleton class which handles connection to mongoDB instance
 */
public class DBSingleton {
    public MongoDatabase databaseInstance;

    private static DBSingleton singleton;

    private  DBSingleton() {
        MongoClientURI uri = new MongoClientURI("mongodb+srv://" +
                EnvironmentVariables.getMongoDBUsername() +":" +
                EnvironmentVariables.getMongoDBPasswprd() + "@" +
                EnvironmentVariables.getMongoDBUrl() + "/admin");

        MongoClient mongoClient = MongoClients.create(uri.toString());
        databaseInstance = mongoClient.getDatabase(EnvironmentVariables.getMongoDBUInstance());
    }

    /**
     * @return Singleton DB instance
     */
    public static DBSingleton getInstance() {
        if(singleton == null) {
            singleton = new DBSingleton();
        }
        return singleton;
    }
}
