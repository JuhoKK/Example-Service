package main.java.example.game.mongodb;

import com.amazonaws.util.json.JSONArray;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import main.java.example.game.Utils.EnvironmentVariables;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;

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

    public JSONArray fetchTopTodayMarket(int rank, String market, String fields) {
        return fetchTopMarket("rank", rank, market, fields);
    }

    public JSONArray fetchTopYesterdayMarket(int rank, String market, String fields) {
        return fetchTopMarket("rankYesterday", rank, market, fields);
    }

    public JSONArray fetchTopWeekMarket(int rank, String market, String fields) {
        return fetchTopMarket("rankWeek", rank, market, fields);
    }

    public JSONArray fetchTopMonthMarket(int rank, String market, String fields) {
        return fetchTopMarket("rankMonth", rank, market, fields);
    }

    public JSONArray fetchGameFromMarket(String gameName, String market, String fields) {
        BasicDBObject searchObject = new BasicDBObject();
        searchObject.put("market", market);
        searchObject.put("name", gameName);
        return fetchFiltered(searchObject, fields);
    }

    private JSONArray fetchTopMarket(String timeRank, int rank, String market, String fields) {
        BasicDBObject searchObject = new BasicDBObject();
        searchObject.put("market", market);
        searchObject.put(timeRank, new BasicDBObject("$lt", rank + 1));
        return fetchFiltered(searchObject, fields);
    }

    private JSONArray fetchFiltered(BasicDBObject searchObject, String fields) {
        List<String> fieldList = Arrays.asList(fields.split(","));
        if(fieldList.size() == 0) {
            return null;
        }

        JSONArray jsonArray = new JSONArray();
        MongoDatabase database = DBSingleton.getInstance().databaseInstance;
        MongoCollection<Document> mongoCollection = database.getCollection(EnvironmentVariables.getMongoDBUCollection());
        MongoCursor<Document> cursor = mongoCollection.find(searchObject)
                .projection(Projections.include(fieldList)).iterator();

        while (cursor.hasNext()) {
            Document obj = cursor.next();
            jsonArray.put(obj.toJson());
        }

        return jsonArray;
    }
}
