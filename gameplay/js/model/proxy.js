var catan = catan || {};
catan.models = catan.models || {};



catan.models.Proxy = (function() {

	/**
		Interface to make calls to the server.

		@class Proxy
		@constructor
	*/
	function Proxy(){
      

	};

	/**
		Login a player 
		<pre>
		PRE: The player must be registered
		PRE: Provided information (username and password) must be correct
		POST: The player will be logged in
		</pre>
		@method login
	*/
	
	 Proxy.prototype.login = function(LoginUser) {


      jQuery.post("/user/login", {username: LoginUser.username, password: LoginUser.password}, function(data){

      	console.log("User Logged in" + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("User failed to login" + JSON.stringify(data,null,"\t"));
      	return data;
      })


	 };

	 /**
		Register a player 
		<pre>
		PRE: The username chosen cannot already exist
		POST: The player will be registered
		</pre>
		@method register
	*/
	 Proxy.prototype.register = function(RegisterUser) {
	 

	 	 jQuery.post("/user/register", {username: RegisterUser.username, password: RegisterUser.password}, function(data){

      	console.log("User Registered" + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("User failed to Register" + JSON.stringify(data,null,"\t"));
      	return data;
      })


	 };
	 	 /**
		Get a list of all the available games
		<pre>
		PRE: At least 1 game should be created
		POST: A list of all the games will be retrieved
		</pre>
		@method listGames
		@return (JSON) all the games
	*/
	 Proxy.prototype.listGames = function() {

	 	jQuery.get("/games/list", function(data){

      	console.log("Game List: " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Could not get Game List: " + JSON.stringify(data,null,"\t"));
      	return data;
      })
	 };

	  	 /**
		Get a list of all the available games
		<pre>
		PRE: none
		POST: The game will be assigned a unique id
		</pre>
		@method createGame
	*/
	 Proxy.prototype.createGame = function(CreateGame) {

	 	jQuery.post("/games/create", {randomTiles: CreateGame.randomTiles, randomNumbers: CreateGame.randomNumbers,randomPorts: CreateGame.randomPorts, name: CreateGame.name}, function(data){

      	console.log("Game Created: " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Failed to create game" + JSON.stringify(data,null,"\t"));
      	return data;
      })
	 };

	   	 /**
		Join an availale game
		<pre>
		PRE: Must be logged in
		PRE: Game can't be full
		POST: Player will join the game
		</pre>
		@method joinGame
	*/
	 Proxy.prototype.joinGame = function(JoinGame) {
	 	


	 	 jQuery.post("/games/join", {color: JoinGame.color, id: JoinGame.id}, function(data){

      	console.log("You Joined the game" + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Failed to join the game" + JSON.stringify(data,null,"\t"));
      	return data;
      })


	 };


	   	 /**
		Returns the game model that you are currently playing in
		<pre>
		PRE: Cookies must be set
		POST: The game model is retrieved
		</pre>
		@method getGameModel
		@return (JSON) the model of the game
	*/
	 Proxy.prototype.getGameModel = function() {

	 		jQuery.get("/game/model", function(data){

      	console.log("Game Model: " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Could not get Game Model: " + JSON.stringify(data,null,"\t"));
      	return data;
      })
	 };

	   	 /**
		Resets the current game 
		<pre>
		PRE: Cookies must be set
		POST: game will return to original state
		</pre>
		@method resetGame
	*/
	 Proxy.prototype.resetGame = function() {

	 		jQuery.post("/game/reset", function(data){

      	console.log("Game Reset " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Failed to reset game " + JSON.stringify(data,null,"\t"));
      	return data;
      })

	 };

	   	 /**
		Gets a set of commands for the current game 
		<pre>
		PRE: Cookies must be set
		POST: Returns a list of the actions taken through the /moves/* methods. This will return an empty list if no
moves have been taken
		</pre>
		@method getCommands
	*/
	 Proxy.prototype.getCommands = function() {


	 		jQuery.get("/game/commands", function(data){

      	console.log("Got game commands: " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Could not get game commands: " + JSON.stringify(data,null,"\t"));
      	return data;
      })
	 };
	  	 /**
		Posts a list of commands to the server. A command is any valid JSON for any valid /moves/*
method. 
		<pre>
		PRE: Cookies must be set
		PRE: The list has to be in JSON form
		POST: command will be executed
		</pre>
		@method sendCommand
	*/
	 Proxy.prototype.sendCommand = function(SendCommand) {

	 		jQuery.post("/game/commands", SendCommand,  function(data){

      	console.log("Game commands updated: " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Could not update game commands: " + JSON.stringify(data,null,"\t"));
      	return data;
      })
	 };
	 	 /**
		Adds an AI player to the game
		<pre>
		PRE: Cookies must be set
		PRE: The AI type is a string -it will be one of the strings returned by /game/listAI
		PRE: Game cannot be full
		POST: AI will be added to the game
		</pre>
		@method addAI
		@param name of the AI player
	*/
	 Proxy.prototype.addAI = function(AddAI) {
	jQuery.post("/game/addAI", {name:AddAI.AiType},  function(data){

      	console.log("Added AI: " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Could not add AI: " + JSON.stringify(data,null,"\t"));
      	return data;
      })

	 };
	 	 /**
		Lists all the AI players of the current game
		<pre>
		PRE: Cookies must be set
		POST: list of AI players retrieved
		</pre>
		@method listAI
	*/
	 Proxy.prototype.listAI = function() {
	jQuery.get("/game/listAI", function(data){

      	console.log("AI List: " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Could not get AI List: " + JSON.stringify(data,null,"\t"));
      	return data;
      })

	 };
	 	 /**
		Changes the log level of the program
		<pre>
		PRE: Cookies must be set
		PRE: ALL, SEVERE, WARNING,INFO, CONFIG, FINE, FINER, FINEST, OFF are the only accepted inputs
		POST: Setting it changes how verbose the server is.
		</pre>
		@method changeLogLevel
	*/
     Proxy.prototype.changeLogLevel = function(ChangeLogLevel) {


     	jQuery.post("/util/changeLogLevel", {logLevel:ChangeLogLevel.logLevel},  function(data){

      	console.log("Changed Log level: " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Could not change log level: " + JSON.stringify(data,null,"\t"));
      	return data;
      })

     };
     /**
		Sends a chat message
		<pre>
		PRE: Cookies must be set
		PRE: 
		POST: sends a chat to player by given id
		</pre>
		@method sendChat
		@return Return's the 'Client Model' JSON (identical to game/model)
	*/
      Proxy.prototype.sendChat = function(SendChat) {

      	jQuery.post("/moves/sendChat", SendChat,  function(data){

      	console.log("Chat Sent: " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Could not send chat: " + JSON.stringify(data,null,"\t"));
      	return data;
      })
      };
      /**
		Used to roll a number at the beginning of your turn
		<pre>
		PRE: Cookies must be set
		PRE: must be the players turn
		POST: gives the number rolled
		</pre>
		@method rollNumber
	*/
       Proxy.prototype.rollNumber = function(RollNumber) {

      
       	jQuery.post("/moves/rollNumber", RollNumber,  function(data){

      	console.log("Roll Number: " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Could not roll number: " + JSON.stringify(data,null,"\t"));
      	return data;
      })

       };
         /**
		Used to finish a players turn
		<pre>
		PRE: Cookies must be set
		PRE: must be the players turn
		POST: the players turn is over
		</pre>
		@method finishTurn
		@return Return's the 'Client Model' JSON (identical to game/model)
	*/
        Proxy.prototype.finishTurn = function(FinishTurn) {
        	 	jQuery.post("/moves/finishTurn", FinishTurn,  function(data){

      	console.log("Finish Turn: " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Could not finish turn: " + JSON.stringify(data,null,"\t"));
      	return data;
      })
        };
          /**
		Buys a development card for yourself.
		<pre>
		PRE: Cookies must be set
		PRE: must be the players turn
		PRE: must have the correct resource cards
		POST: player gets a devCard
		</pre>
		@method buyDevCard
	*/
         Proxy.prototype.buyDevCard = function(BuyDevCard) {

          	jQuery.post("/moves/buyDevCard", BuyDevCard,  function(data){

      	console.log("Bought Dev Card: " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Could not buy Dev card: " + JSON.stringify(data,null,"\t"));
      	return data;
      })
         };
              /**
		Plays a 'Year of Plenty' card from your hand to gain the two specified resources
		<pre>
		PRE: Cookies must be set
		PRE: must be the players turn
		PRE: player must have the year of plenty card
		POST: year of plenty card is played
		</pre>
		@method playYearPlenty
	*/
          Proxy.prototype.playYearPlenty = function(PlayYearOfPlenty) {
          	 	jQuery.post("/moves/Year_of_Plenty", PlayYearOfPlenty,  function(data){

      	console.log("Played year of plenty: " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Could not play year of plenty: " + JSON.stringify(data,null,"\t"));
      	return data;
      })
          };
                  /**
	Plays a 'Road Building' card from your hand to build at the two spots specified
		<pre>
		PRE: Cookies must be set
		PRE: must be the players turn
		PRE: player must have the road building card
		POST: road building card is played
		</pre>
		@method playRoadBuilding
	*/
           Proxy.prototype.playRoadBuilding = function(PlayRoadBuilding) {
            	jQuery.post("/moves/Road_Building", PlayRoadBuilding,  function(data){

      	console.log("Played road building: " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Could not play road building: " + JSON.stringify(data,null,"\t"));
      	return data;
      })
           };
                  /**
	Plays a 'Soldier' from your hand, selecting the new robber position and player to rob
		<pre>
		PRE: Cookies must be set
		PRE: must be the players turn
		PRE: player must have the soldier card
		POST: soldier card is played
		</pre>
		@method playSoldier
	*/
	 Proxy.prototype.playSoldier = function(PlaySoldier) {

	  	jQuery.post("/moves/Soldier", PlaySoldier,  function(data){

      	console.log("Soldier Played: " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Could not play soldier: " + JSON.stringify(data,null,"\t"));
      	return data;
      })
	 };
	  /**
	Plays a 'Monopoly' card from you hand to monopolize the requested resource.
		<pre>
		PRE: Cookies must be set
		PRE: must be the players turn
		PRE: player must have the monopoly card
		POST: monopoly card played
		</pre>
		@method playMonopoly
	*/
            Proxy.prototype.playMonopoly = function(PlayMonopoly) {
             	jQuery.post("/moves/Monopoly", PlayMonopoly,  function(data){

      	console.log("Monopoly played: " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Could not play monopoly: " + JSON.stringify(data,null,"\t"));
      	return data;
      })

            };
             /**
	Plays a 'Monument' card from your hand to give you a point
		<pre>
		PRE: Cookies must be set
		PRE: must be the players turn
		PRE: player must have the monument card
		POST: player given a point
		</pre>
		@method playMonument
	*/
             Proxy.prototype.playMonument = function(PlayMonument) {

              	jQuery.post("/moves/Monument", PlayMonument,  function(data){

      	console.log("Played Monument: " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Could not play Monument: " + JSON.stringify(data,null,"\t"));
      	return data;
      })

             };
                  /**
	Builds a road for the specified player at the specified spot. Set true to free if it's during the setup
		<pre>
		PRE: Cookies must be set
		PRE: must be the players turn
		PRE: player must have the resources available
		POST: road built
		</pre>
		@method buildRoad
	*/
              Proxy.prototype.buildRoad = function(BuildRoad) {

               	jQuery.post("/moves/buildRoad", BuildRoad,  function(data){

      	console.log("Road Built: " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Could not build road: " + JSON.stringify(data,null,"\t"));
      	return data;
      })
              };
                  /**
	Builds a settlement for the specified player at the specified spot. Set true to free if it's during the setup
		<pre>
		PRE: Cookies must be set
		PRE: must be the players turn
		PRE: player must have the resources available
		POST: settlement built
		</pre>
		@method buildSettlement
	*/
               Proxy.prototype.buildSettlement = function(BuildSettlement) {
             	jQuery.post("/moves/buildSettlement", BuildSettlement,  function(data){

      	console.log("Settlement built: " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Could not build settlement: " + JSON.stringify(data,null,"\t"));
      	return data;
      })
               };
                      /**
	Builds a city for the specified player at the specified spot. Set true to free if it's during the setup.
		<pre>
		PRE: Cookies must be set
		PRE: must be the players turn
		PRE: player must have the resources available
		POST: city built
		</pre>
		@method buildCity
	*/
                Proxy.prototype.buildCity = function(BuildCity) {

           	jQuery.post("/moves/buildCity", BuildCity,  function(data){

      	console.log("City BUilt: " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Could not build city: " + JSON.stringify(data,null,"\t"));
      	return data;
      })
                };
                            /**
		Offers a trade to another player
		<pre>
		PRE: Cookies must be set
		PRE: player must have the resources
		POST: trade extended to another player
		</pre>
		@method offerTrade
	*/
                 Proxy.prototype.offerTrade = function(OfferTrade) {

          	jQuery.post("/moves/offerTrade", OfferTrade,  function(data){

      	console.log("Trade Offered: " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Could not offer trade " + JSON.stringify(data,null,"\t"));
      	return data;
      })
                 };
                           /**
		Accept or reject a trade offered to you
		<pre>
		PRE: Cookies must be set
		PRE: player must have the resources
		POST: trade offer is closed
		</pre>
		@method acceptTrade
	*/
                  Proxy.prototype.acceptTrade = function(AcceptTrade) {

          	jQuery.post("/moves/acceptTrade", AcceptTrade,  function(data){

      	console.log("Trade Accepted: " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("could not accept trade: " + JSON.stringify(data,null,"\t"));
      	return data;
      })
                  };
                               /**
		Discards selected cards because of a 7 rolled
		<pre>
		PRE: Cookies must be set
		PRE: player must have > 7 cards
		POST: player has 7 cards left
		</pre>
		@method discardCards
	*/
                   Proxy.prototype.discardCards = function(DiscardCards) {
              	jQuery.post("/moves/discardCards", DiscardCards,  function(data){

      	console.log("Cards discarded " + JSON.stringify(data,null,"\t"));
      	return data;

      }).fail(function(data){
      	console.log("Could not discardCards: " + JSON.stringify(data,null,"\t"));
      	return data;
      })
                   };


     return Proxy;
})();
