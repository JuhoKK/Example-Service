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

import javax.ws.rs.NotFoundException;
import java.util.*;

public class DBService {

    /**
     * Fetches games from rank 1 to given rank from specified market
     *
     * @param rank Rank where we stop the search
     * @param market Market area we search
     * @param fields Fields we display in results
     * @return JSON array
     */
    public JSONArray fetchTopTodayMarket(int rank, String market, String fields) {
        return fetchTopMarket("rank", rank, market, fields);
    }

    /**
     * Fetches games from yesterday rank 1 to given rank from specified market
     *
     * @param rank Rank where we stop the search
     * @param market Market area we search
     * @param fields Fields we display in results
     * @return JSON array
     */
    public JSONArray fetchTopYesterdayMarket(int rank, String market, String fields) {
        return fetchTopMarket("rankYesterday", rank, market, fields);
    }

    /**
     * Fetches games from week rank 1 to given rank from specified market
     *
     * @param rank Rank where we stop the search
     * @param market Market area we search
     * @param fields Fields we display in results
     * @return JSON array
     */
    public JSONArray fetchTopWeekMarket(int rank, String market, String fields) {
        return fetchTopMarket("rankWeek", rank, market, fields);
    }

    /**
     * Fetches games from month rank 1 to given rank from specified market
     *
     * @param rank Rank where we stop the search
     * @param market Market area we search
     * @param fields Fields we display in results
     * @return JSON array
     */
    public JSONArray fetchTopMonthMarket(int rank, String market, String fields) {
        return fetchTopMarket("rankMonth", rank, market, fields);
    }

    /**
     * Fetches specified game
     *
     * @param gameName Name of the game we try to fetch
     * @param market Market area we search
     * @param fields Fields we display in results
     * @return JSON array
     */
    public JSONArray fetchGameFromMarket(String gameName, String market, String fields) {
        BasicDBObject searchObject = new BasicDBObject();
        searchObject.put("market", market);
        searchObject.put("name", gameName);
        return fetchFiltered(searchObject, fields);
    }

    /**
     * Fetches games which have entered specified rank in give past days
     *
     * @param rank Rank which game should have entered
     * @param market Market area we search
     * @param days Days since today
     * @param fields Fields we display in results
     * @return JSON array
     */
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

    /**
     * Update favorite status of a given game
     *
     * @param gameName Name of the game we try to update
     * @param market Market area we search
     * @param favorite Status we should update to DB
     * @return True if update succeeds
     * @throws NotFoundException thrown if no game with specified name was found
     */
    public boolean changeFavorite(String gameName, String market, boolean favorite) throws NotFoundException {
        BasicDBObject searchObject = new BasicDBObject();
        searchObject.put("market", market);
        searchObject.put("name", gameName);

        BasicDBObject updateObject = new BasicDBObject("$set", new BasicDBObject("favorite", favorite));

        return updateObject(searchObject, updateObject);
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

    private boolean updateObject(BasicDBObject searchObject, BasicDBObject updateObject) throws NotFoundException {
        MongoDatabase database = DBSingleton.getInstance().databaseInstance;
        MongoCollection<Document> mongoCollection = database.getCollection(EnvironmentVariables.getMongoDBUCollection());

        UpdateResult result = mongoCollection.updateOne(searchObject, updateObject);

        if(result.getMatchedCount() == 0) {
            throw new NotFoundException("Did not find the specified game");
        }
        return result.getModifiedCount() > 0;
    }
}
