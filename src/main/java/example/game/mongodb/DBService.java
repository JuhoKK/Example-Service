package main.java.example.game.mongodb;

import com.amazonaws.util.json.JSONArray;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.UpdateResult;
import main.java.example.game.Utils.EnvironmentVariables;
import org.bson.Document;

import java.util.*;

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

    public JSONArray fetchGamesEnteredRankFromMarket(int rank, String market, int days, String fields) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -24 * days);
        Date date = calendar.getTime();
        BasicDBObject searchObject = new BasicDBObject();
        searchObject.put("market", market);
        switch (rank) {
            case 10:
                searchObject.put("top10Entry", new BasicDBObject("$gt", date.getTime()));
                break;
            case 20:
                searchObject.put("top20Entry", new BasicDBObject("$gt", date.getTime()));
                break;
            case 50:
                searchObject.put("top50Entry", new BasicDBObject("$gt", date.getTime()));
                break;
            case 100:
                searchObject.put("top100Entry", new BasicDBObject("$gt", date.getTime()));
                break;
            case 200:
            default:
                searchObject.put("top200Entry", new BasicDBObject("$gt", date.getTime()));
                break;
        }
        return fetchFiltered(searchObject, fields);
    }

    public boolean changeFavorite(String gameName, String market, boolean favorite) {
        BasicDBObject searchObject = new BasicDBObject();
        searchObject.put("market", market);
        searchObject.put("name", gameName);

        BasicDBObject updateObject = new BasicDBObject("favorite", favorite);

        return updateObject(updateObject, searchObject);
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

    private boolean updateObject(BasicDBObject updateObject, BasicDBObject searchObject) {
        MongoDatabase database = DBSingleton.getInstance().databaseInstance;
        MongoCollection<Document> mongoCollection = database.getCollection(EnvironmentVariables.getMongoDBUCollection());

        MongoCursor<Document> cursor = mongoCollection.find(searchObject)
                .projection(Projections.include("name")).iterator();

        if (!cursor.hasNext()) {
            return false; // Game(s) didn't exist in DB
        }

        UpdateResult result = mongoCollection.updateOne(searchObject, updateObject);

        return result.getModifiedCount() > 0;
    }
}
