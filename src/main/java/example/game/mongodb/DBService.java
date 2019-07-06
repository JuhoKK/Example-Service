package main.java.example.game.mongodb;

import com.amazonaws.util.json.JSONArray;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import main.java.example.game.Utils.EnvironmentVariables;
import org.bson.Document;

public class DBService {
    public JSONArray fetch() {
        JSONArray jsonArray = new JSONArray();
        MongoDatabase database = DBSingleton.getInstance().databaseInstance;
        MongoCollection<Document> mongoCollection = database.getCollection(EnvironmentVariables.getMongoDBUCollection());
        MongoCursor<Document> cursor = mongoCollection.find().limit(1).iterator();

        while (cursor.hasNext()) {
            Document obj = cursor.next();
            jsonArray.put(obj.toJson());
        }
        return jsonArray;
    }
}
