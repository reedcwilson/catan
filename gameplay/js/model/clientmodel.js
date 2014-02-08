//STUDENT-EDITABLE-BEGIN
/**
	This module contains the top-level client model class
	
	@module		catan.models
	@namespace models
*/

var catan = catan || {};
catan.models = catan.models || {};

catan.models.ClientModel  = (function clientModelNameSpace()
{
    /** 
	This the top-level client model class that contains the local player, map contents, etc.
	
	@class ClientModel
	@constructor
	@param {integer} playerID The id of the local player, extracted from the cookie
    */
	var ClientModel = (function ClientModelClass()
	{ 
		core.defineProperty(ClientModel.prototype, "bank");
		core.defineProperty(ClientModel.prototype, "chat");
		core.defineProperty(ClientModel.prototype, "deck");
		core.defineProperty(ClientModel.prototype, "biggestArmy");
		core.defineProperty(ClientModel.prototype, "log");
		core.defineProperty(ClientModel.prototype, "longestRoad");
		core.defineProperty(ClientModel.prototype, "map");
		core.defineProperty(ClientModel.prototype, "players");
		core.defineProperty(ClientModel.prototype, "playerID");
		core.defineProperty(ClientModel.prototype, "proxy");
		core.defineProperty(ClientModel.prototype, "tradeOffer");
		core.defineProperty(ClientModel.prototype, "turnTracker");
		core.defineProperty(ClientModel.prototype, "winner");
		
		function ClientModel(playerID)
		{
			this.setPlayerID(playerID);
			this.setProxy(new catan.models.Proxy());
		}      
        
        /**
         * This is called to fetch the game state from the server the very first time.
         * It should: 1) fetch the game state JSON from the server, 2) update the client model with the
         * returned data, 3) notify all client model listeners that the model has changed, and 4) invoke
         * the success callback function with the object received from the server.
         * 
         * @method initFromServer
         * @param {function} success - A callback function that is called after the game state has been fetched from the server and the client model updated. This function is passed a single parameter which is the game state object received from the server.
         * */
		ClientModel.prototype.initFromServer = function(success)
		{
            // TODO: 1) fetch the game state from the server, 2) update the client model, 3) call the "success" function.
			
			this.getProxy().getModel(this.init, success);
		}
		
		 /**
         * This is called to initialize the ClientModel member variables.
         * 
         * @method init
         * @param {JSON} model - The JSON model of the game.
         * */
		ClientModel.prototype.init = function (model) 
		{  
			this.setChat(new catan.models.MessageList());			
			this.setLog(new catan.models.MessageList());
			this.setMap(new catan.models.Map());
			this.setTradeOffer(new catan.models.TradeOffer());
			this.setTurnTracker(new catan.models.TurnTracker());
						
			var tempPlayers = [];			
			for(var i = 0; i < model.players.length; i++)
				tempPlayers[model.players[i].playerID] = new catan.models.Player();
			this.setPlayers(tempPlayers);
			
			this.update(model);			
		};
		
		/**
         * This is called to update all the model classes held by the ClientModel based on the new JSON model from the server.
         * 
         * @method update
         * @param {JSON} model - The JSON model of the game.
         * */
		ClientModel.prototype.update = function (model) 
		{               
			this.setBank(model.bank);
			this.setBiggestArmy(model.biggestArmy);
			this.getChat().setInfo(model.chat);
			this.setDeck(model.deck);
			this.getLog().setInfo(model.log);
			this.setLongestRoad(model.longestRoad);
			this.getMap().setInfo(model.map);
			this.getTradeOffer().setInfo(model.tradeOffer);
			this.getTurnTracker().setInfo(model.turnTracker);
			this.setWinner(model.winner);
			
			var players = this.getPlayers();
			for(var i = 0; i < players.length; i++)
				players[model.players[i].playerID].setInfo(model.players[i]);
		};	

		//Queries
		
		ClientModel.prototype.canPlaceRoad = function (location) 
		{               
			return this.getMap().canPlaceRoad(location);
		};
		
		ClientModel.prototype.canPlaceSettlement = function (location) 
		{               
			return this.getMap().canPlaceSettlement(location);
		};
		
		ClientModel.prototype.canPlaceCity = function (location) 
		{               
			return this.getMap().canPlaceCity(location);
		};
		
		ClientModel.prototype.needsToDiscard = function () 
		{               
			return this.getPlayers()[this.getPlayerID()].needsToDiscard();
		};
		
		ClientModel.prototype.hasResources = function (resourceList) 
		{               
			return this.getPlayers()[this.getPlayerID()].hasResources(resourceList);
		};
		
		ClientModel.prototype.canPlayDevCard = function (devCard) 
		{               
			return this.getPlayers()[this.getPlayerID()].canPlayDevCard(devCard);
		};
		
		ClientModel.prototype.getResources = function () 
		{               
			return this.getPlayers()[this.getPlayerID()].getResources();
		};
		
		ClientModel.prototype.canOfferTrade = function () 
		{               
			return this.getPlayers()[this.getPlayerID()].canOfferTrade(this.getTradeOffer());
		};
		
		ClientModel.prototype.canAcceptTrade = function () 
		{               
			return this.getPlayers()[this.getPlayerID()].canAcceptTrade(this.getTradeOffer());
		};
		
		// Proxy Calls	
		
		ClientModel.prototype.sendMove = function (data) 
		{    
			this.getProxy().send(new CommandObject(data), this.update);
		};	
        
		return ClientModel;
	}());	
	
	return ClientModel;
}());

