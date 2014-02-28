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
		Parses the CommandObject and makes the appopriate call to the server
		<pre>
			PRE: Cookies must be set
			POST: Command will be completed
		</pre>
		@method send
		@return (JSON) the model of the game
	*/

	Proxy.prototype.send = function(CommandObject, update, observerNotify, model){
			jQuery.post(CommandObject.url, JSON.stringify(CommandObject.type), function(data){
				update(data);
				if(observerNotify != undefined){
					console.log("observerNotify", observerNotify);
					observerNotify();
				}
				if(CommandObject.url == "/moves/Soldier" || CommandObject.url == "/moves/robPlayer")
				{
					model.robbing = true;
				}
		});			
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
	Proxy.prototype.getModel = function(clientmodel, success){
		jQuery.get("/game/model", function(data){
			clientmodel.init(data);
			if(success != undefined) {
				success(data);
			}
		});
	};

	Proxy.prototype.updateModel = function(clientmodel) {
		jQuery.get("/game/model", function(data){
			clientmodel.update(data);
		});
	}
	   	 /**
		Resets the current game 
		<pre>
			PRE: Cookies must be set
			POST: game will return to original state
		</pre>
		@method resetGame
	*/
	 Proxy.prototype.resetGame = function(update) {

	 		jQuery.post("/game/reset", function(data){
      		update(data);
      	});
	 };
	 
     return Proxy;
})();
