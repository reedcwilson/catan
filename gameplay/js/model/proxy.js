var catan = catan || {};
catan.models = catan.models || {};



catan.models.Proxy = (function() {
	/**
		Interface to make calls to the server.

		@class Proxy
		@constructor
	*/
	function Proxy(){};

	/**
		Login a player 
		<pre>
		PRE: The player must be registered
		PRE: Provided information (username and password) must be correct
		POST: The player will be logged in
		</pre>
		@method login
	*/
	
	 Proxy.prototype.login = function() {};

	 /**
		Register a player 
		<pre>
		PRE: The username chosen cannot already exist
		POST: The player will be registered
		</pre>
		@method register
	*/
	 Proxy.prototype.register = function() {};
	 	 /**
		Get a list of all the available games
		<pre>
		PRE: At least 1 game should be created
		POST: A list of all the games will be retrieved
		</pre>
		@method listGames
		@return (JSON) all the games
	*/
	 Proxy.prototype.listGames = function() {};

	  	 /**
		Get a list of all the available games
		<pre>
		PRE: none
		POST: The game will be assigned a unique id
		</pre>
		@method createGame
	*/
	 Proxy.prototype.createGame = function() {};

	   	 /**
		Join an availale game
		<pre>
		PRE: Must be logged in
		PRE: Game can't be full
		POST: Player will join the game
		</pre>
		@method joinGame
	*/
	 Proxy.prototype.joinGame = function(id, color) {};


	   	 /**
		Returns the game model that you are currently playing in
		<pre>
		PRE: Cookies must be set
		POST: The game model is retrieved
		</pre>
		@method getGameModel
		@return (JSON) the model of the game
	*/
	 Proxy.prototype.getGameModel = function() {};

	   	 /**
		Resets the current game 
		<pre>
		PRE: Cookies must be set
		POST: game will return to original state
		</pre>
		@method resetGame
	*/
	 Proxy.prototype.resetGame = function() {};

	   	 /**
		Gets a set of commands for the current game 
		<pre>
		PRE: Cookies must be set
		POST: Returns a list of the actions taken through the /moves/* methods. This will return an empty list if no
moves have been taken
		</pre>
		@method getCommands
	*/
	 Proxy.prototype.getCommands = function() {};
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
	 Proxy.prototype.sendCommand = function() {};
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
	 Proxy.prototype.addAI = function(name) {};
	 	 /**
		Lists all the AI players of the current game
		<pre>
		PRE: Cookies must be set
		POST: list of AI players retrieved
		</pre>
		@method listAI
	*/
	 Proxy.prototype.listAI = function(name) {};
	 	 /**
		Changes the log level of the program
		<pre>
		PRE: Cookies must be set
		PRE: ALL, SEVERE, WARNING,INFO, CONFIG, FINE, FINER, FINEST, OFF are the only accepted inputs
		POST: Setting it changes how verbose the server is.
		</pre>
		@method changeLogLevel
	*/
     Proxy.prototype.changeLogLevel = function() {};
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
      Proxy.prototype.sendChat = function() {};
      /**
		Used to roll a number at the beginning of your turn
		<pre>
		PRE: Cookies must be set
		PRE: must be the players turn
		POST: gives the number rolled
		</pre>
		@method rollNumber
	*/
       Proxy.prototype.rollNumber = function() {};
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
        Proxy.prototype.finishTurn = function() {};
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
         Proxy.prototype.buyDevCard = function() {};
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
          Proxy.prototype.playYearPlenty = function() {};
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
           Proxy.prototype.playRoadBuilding = function() {};
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
	 Proxy.prototype.playSoldier = function() {};
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
            Proxy.prototype.playMonopoly = function() {};
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
             Proxy.prototype.playMonument = function() {};
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
              Proxy.prototype.buildRoad = function() {};
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
               Proxy.prototype.buildSettlement = function() {};
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
                Proxy.prototype.buildCity = function() {};
                            /**
		Offers a trade to another player
		<pre>
		PRE: Cookies must be set
		PRE: player must have the resources
		POST: trade extended to another player
		</pre>
		@method offerTrade
	*/
                 Proxy.prototype.offerTrade = function() {};
                           /**
		Accept or reject a trade offered to you
		<pre>
		PRE: Cookies must be set
		PRE: player must have the resources
		POST: trade offer is closed
		</pre>
		@method acceptTrade
	*/
                  Proxy.prototype.acceptTrade = function() {};
                               /**
		Discards selected cards because of a 7 rolled
		<pre>
		PRE: Cookies must be set
		PRE: player must have > 7 cards
		POST: player has 7 cards left
		</pre>
		@method discardCards
	*/
                   Proxy.prototype.discardCards = function() {};


     return Proxy;
})();