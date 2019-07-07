# Example-Service
This is an example service which contains multiple REST endpoints. 
These endpoints connect to a MongoDB instance and either fetch data or input data

All endpoints have base path of: {host}/rest  
All endpoints support country codes: fi, es and it only.

## GET Endpoints
Here's documentation of endpoints which are GET type

### getTopGamesForMarketToday()
Fetches games from rank 1 to given rank from specified market
* Path: /top/today/{market}/{rank}

Params:
* market : String; Path parameter double digit country code of the market area we search
* rank : int; Path parameter which defines where where we stop the search
* fields : String; Query parameter comma delimited string of fields we display in results

### getTopGamesForMarketYesterday()
Fetches games from yesterday rank 1 to given rank from specified market
* Path: /top/yesterday/{market}/{rank}

Params:
* market : String; Path parameter double digit country code of the market area we search
* rank : int; Path parameter which defines where where we stop the search
* fields : String; Query parameter comma delimited string of fields we display in results
 
### getTopGamesForMarketWeek()
Fetches games from week rank 1 to given rank from specified market
* Path: /top/week/{market}/{rank}

Params:
* market : String; Path parameter double digit country code of the market area we search
* rank : int; Path parameter which defines where where we stop the search
* fields : String; Query parameter comma delimited string of fields we display in results

### getTopGamesForMarketMonth()
Fetches games from month rank 1 to given rank from specified market
* Path: /top/yesterday/{market}/{rank}

Params:
* market : String; Path parameter double digit country code of the market area we search
* rank : int; Path parameter which defines where where we stop the search
* fields : String; Query parameter comma delimited string of fields we display in results

### getGameForMarket()
Fetches specified game
* Path: /game/{market}/{name}

Params:
* market : String; Path parameter double digit country code of the market area we search
* name : String; Path parameter which contains name of the game we try to fetch
* fields : String; Query parameter comma delimited string of fields we display in results

### getGamesEnteredRank()
Fetches games which have entered specified rank in give past days
* Path: /date/ranked/{market}/{rank}/{days}

Params:
* market : String; Path parameter double digit country code of the market area we search
* rank : int; Path parameter which defines the rank which game should have entered. Currently supported values are: 10, 20, 50, 100 and 200
* days : int; Path parameter which defines the days since today 
* fields : String; Query parameter comma delimited string of fields we display in results

## POST Endpoints
Here's documentation of endpoints which are POST type

### addToFavorites()
Adds given game to favorites
* Path: /game/{market}/{name}/favorite/add

Params:
* market : String; Path parameter double digit country code of the market area we search
* name : String; Path parameter which contains name of the game we try add to favorites

Returns the modified game entry when successfully executed

### removeFromFavorites()
Removes given game from favorites
* Path: /game/{market}/{name}/favorite/add

Params:
* market : String; Path parameter double digit country code of the market area we search
* name : String; Path parameter which contains name of the game we try to remove from favorites

Returns the modified game entry when successfully executed