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

     return Proxy;
})();