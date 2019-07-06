package main.java.example.game.Utils;

/**
 * Fetches saved environment variables
 */
public class EnvironmentVariables {

    /**
     * Get the mongoDB url
     *
     * @return MONGODB_ADDRESS variable
     */
    public static String getMongoDBUrl() {
        return System.getenv("MONGODB_ADDRESS");
    }

    /**
     * Get the mongoDB username
     *
     * @return MONGODB_USERNAME variable
     */
    public static String getMongoDBUsername() {
        return System.getenv("MONGODB_USERNAME");
    }

    /**
     * Get the mongoDB username's password
     *
     * @return MONGODB_PASSWORD variable
     */
    public static String getMongoDBPasswprd() {
        return System.getenv("MONGODB_PASSWORD");
    }

    /**
     * Get the target db instance
     *
     * @return MONGODB_ADDRESS variable
     */
    public static String getMongoDBUInstance() {
        return System.getenv("MONGODB_DB");
    }

    /**
     * Get the collection we deal with
     *
     * @return MONGODB_COLLECTION variable
     */
    public static String getMongoDBUCollection() {
        return System.getenv("MONGODB_COLLECTION");
    }
}
