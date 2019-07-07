/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package main.java.example.game.webservice;

import com.amazonaws.util.json.JSONArray;
import main.java.example.game.mongodb.DBService;

import javax.inject.Inject;
import javax.ws.rs.*;

/**
 * A simple REST service which is able to say hello to someone using HelloService Please take a look at the web.xml where JAX-RS
 * is enabled
 *
 * @author gbrey@redhat.com
 *
 */

@Path("/")
public class GameEndpoint {

    @GET
    @Path("/top/today/{market}/{rank}")
    @Produces({ "application/json" })
    public String getTopGamesForMarketToday(@PathParam("rank") final Integer rank, @PathParam("market") final String market, @QueryParam("fields") final String fields) {
        if(rank == 0 || market == null || fields == null) {
            return ""; // Should probably change return value type to response and return 400 status code instead
        }
        JSONArray jsonArray = new DBService().fetchTopTodayMarket(rank, market, fields);
        return jsonArray != null ? jsonArray.toString() : "";
    }

    @GET
    @Path("/top/yesterday/{market}/{rank}")
    @Produces({ "application/json" })
    public String getTopGamesForMarketYesterday(@PathParam("rank") final Integer rank, @PathParam("market") final String market, @QueryParam("fields") final String fields) {
        if(rank == 0 || market == null || fields == null) {
            return ""; // Should probably change return value type to response and return 400 status code instead
        }
        JSONArray jsonArray = new DBService().fetchTopYesterdayMarket(rank, market, fields);
        return jsonArray != null ? jsonArray.toString() : "";
    }

    // This should use id instead of name for parameter but for ease of reading we are using name in this example
    @GET
    @Path("/game/{market}/{name}")
    @Produces({ "application/json" })
    public String getGameForMarket(@PathParam("market") final String market, @PathParam("name") final String name, @QueryParam("fields") final String fields) {
        if(name == null || market == null || fields == null) {
            return ""; // Should probably change return value type to response and return 400 status code instead
        }
        JSONArray jsonArray = new DBService().fetchGameFromMarket(name, market, fields);
        return jsonArray != null ? jsonArray.toString() : "";
    }

    @GET
    @Path("/date/ranked/{market}/{rank}/{days}")
    @Produces({ "application/json" })
    public String getGamesEnteredRank(@PathParam("market") final String market, @PathParam("rank") final int rank, @PathParam("days") final int days, @QueryParam("fields") final String fields) {
        if(rank == 0 || market == null || fields == null) {
            return ""; // Should probably change return value type to response and return 400 status code instead
        }
        JSONArray jsonArray = new DBService().fetchGamesEnteredRankFromMarket(rank, market, days, fields);
        return jsonArray != null ? jsonArray.toString() : "";
    }

    @POST
    @Path("/game/{market}/{name}/favorite/add")
    public String addToFavorites(@PathParam("market") final String market, @PathParam("name") final String name) {
        if(name == null || market == null) {
            return ""; // Should probably change return value type to response and return 400 status code instead
        }
        DBService dbService = new DBService();
        dbService.changeFavorite(name, market, true);
        return dbService.fetchGameFromMarket(name, market, "name,favorite").toString();
    }

    @POST
    @Path("/game/{market}/{name}/favorite/remove")
    public String removeFromFavorites(@PathParam("market") final String market, @PathParam("name") final String name) {
        if(name == null || market == null) {
            return ""; // Should probably change return value type to response and return 400 status code instead
        }
        DBService dbService = new DBService();
        boolean success = dbService.changeFavorite(name, market, false);
        if(!success) {
            return "Something went wrong";
        }
        return dbService.fetchGameFromMarket(name, market, "name,favorite").toString();
    }
}
