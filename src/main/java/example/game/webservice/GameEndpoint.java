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

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

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
    public Response getTopGamesForMarketToday(@PathParam("rank") final Integer rank, @PathParam("market") final String market, @QueryParam("fields") final String fields) {
        if(rank == 0 || market == null || fields == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        JSONArray jsonArray = new DBService().fetchTopTodayMarket(rank, market, fields);
        return Response.status(Response.Status.OK).type(APPLICATION_JSON).entity(jsonArray.toString()).build();
    }

    @GET
    @Path("/top/yesterday/{market}/{rank}")
    public Response getTopGamesForMarketYesterday(@PathParam("rank") final Integer rank, @PathParam("market") final String market, @QueryParam("fields") final String fields) {
        if(rank == 0 || market == null || fields == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        JSONArray jsonArray = new DBService().fetchTopYesterdayMarket(rank, market, fields);
        return Response.status(Response.Status.OK).type(APPLICATION_JSON).entity(jsonArray.toString()).build();
    }

    @GET
    @Path("/top/week/{market}/{rank}")
    public Response getTopGamesForMarketWeek(@PathParam("rank") final Integer rank, @PathParam("market") final String market, @QueryParam("fields") final String fields) {
        if(rank == 0 || market == null || fields == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        JSONArray jsonArray = new DBService().fetchTopWeekMarket(rank, market, fields);
        return Response.status(Response.Status.OK).type(APPLICATION_JSON).entity(jsonArray.toString()).build();
    }

    @GET
    @Path("/top/month/{market}/{rank}")
    public Response getTopGamesForMarketMonth(@PathParam("rank") final Integer rank, @PathParam("market") final String market, @QueryParam("fields") final String fields) {
        if(rank == 0 || market == null || fields == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        JSONArray jsonArray = new DBService().fetchTopMonthMarket(rank, market, fields);
        return Response.status(Response.Status.OK).type(APPLICATION_JSON).entity(jsonArray.toString()).build();
    }

    // This should use id instead of name for parameter but for ease of reading we are using name in this example
    @GET
    @Path("/game/{market}/{name}")
    public Response getGameForMarket(@PathParam("market") final String market, @PathParam("name") final String name, @QueryParam("fields") final String fields) {
        if(name == null || market == null || fields == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        JSONArray jsonArray = new DBService().fetchGameFromMarket(name, market, fields);
        return Response.status(Response.Status.OK).type(APPLICATION_JSON).entity(jsonArray.toString()).build();
    }

    @GET
    @Path("/date/ranked/{market}/{rank}/{days}")
    public Response getGamesEnteredRank(@PathParam("market") final String market, @PathParam("rank") final int rank, @PathParam("days") final int days, @QueryParam("fields") final String fields) {
        if(rank == 0 || market == null || fields == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        JSONArray jsonArray = new DBService().fetchGamesEnteredRankFromMarket(rank, market, days, fields);
        return Response.status(Response.Status.OK).type(APPLICATION_JSON).entity(jsonArray.toString()).build();
    }

    @POST
    @Path("/game/{market}/{name}/favorite/add")
    public Response addToFavorites(@PathParam("market") final String market, @PathParam("name") final String name) {
        if(name == null || market == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        DBService dbService = new DBService();
        try {
            boolean success = dbService.changeFavorite(name, market, true);
            if(!success) {
                return Response.status(Response.Status.NOT_MODIFIED).build();
            }
        } catch (NotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).type(APPLICATION_JSON).entity(
                dbService.fetchGameFromMarket(name, market, "name,favorite").toString()
        ).build();
    }

    @POST
    @Path("/game/{market}/{name}/favorite/remove")
    public Response removeFromFavorites(@PathParam("market") final String market, @PathParam("name") final String name) {
        if(name == null || market == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        DBService dbService = new DBService();
        try {
            boolean success = dbService.changeFavorite(name, market, false);
            if(!success) {
                return Response.status(Response.Status.NOT_MODIFIED).build();
            }
        } catch (NotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).type(APPLICATION_JSON).entity(
                dbService.fetchGameFromMarket(name, market, "name,favorite").toString()
        ).build();
    }
}
