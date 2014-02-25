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
	@param {integer} clientID The id of the local player, extracted from the cookie
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
		core.defineProperty(ClientModel.prototype, "clientID");
		core.defineProperty(ClientModel.prototype, "proxy");
		core.defineProperty(ClientModel.prototype, "tradeOffer");
		core.defineProperty(ClientModel.prototype, "turnTracker");
		core.defineProperty(ClientModel.prototype, "winner");
		core.defineProperty(ClientModel.prototype, "observers");
		
		
		function ClientModel(clientID)
		{
			this.setClientID(clientID);
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
			
			this.getProxy().getModel(this, success);
			self = this;
		}
		var self;
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
			this.setMap(new catan.models.Map(model.map.radius));
			this.setTradeOffer(new catan.models.TradeOffer());
			this.setTurnTracker(new catan.models.TurnTracker());
						
			var tempPlayers = [];			
			for(var i = 0; i < model.players.length; i++)
				tempPlayers[model.players[i].orderNumber] = new catan.models.Player();
	
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
		            
			self.setBank(model.bank);
			self.setBiggestArmy(model.biggestArmy);
			self.getChat().setInfo(model.chat);
			self.setDeck(model.deck);
			self.getLog().setInfo(model.log);
			self.setLongestRoad(model.longestRoad);
			self.getMap().setInfo(model.map);
			self.getTradeOffer().setInfo(model.tradeOffer);
			self.getTurnTracker().setInfo(model.turnTracker);
			self.setWinner(model.winner);			
			
			for(var i = 0; i < model.players.length; i++)
				self.getPlayers()[model.players[i].orderNumber].setInfo(model.players[i]);	


				
		};	

		//Queries
		
		ClientModel.prototype.canPlaceRoad = function (location, id) 
		{               
			return this.getMap().canPlaceRoad(location, id);
		};

		ClientModel.prototype.setupCanPlaceRoad = function (location, id) 
		{               
			return this.getMap().setupCanPlaceRoad(location, id);
		};
		
		ClientModel.prototype.canPlaceSettlement = function (location, id) 
		{               
			return this.getMap().canPlaceSettlement(location, id);
		};
		
		ClientModel.prototype.canPlaceCity = function (location, id) 
		{               
			return this.getMap().canPlaceCity(location, id);
		};
		
		ClientModel.prototype.needsToDiscard = function () 
		{               
			return this.getPlayers()[this.getClientID()].needsToDiscard();
		};
		
		ClientModel.prototype.hasResources = function (resourceList) 
		{               
			return this.getPlayers()[this.getClientID()].hasResources(resourceList);
		};
		
		ClientModel.prototype.canPlayDevCard = function (devCard) 
		{               
			var playerIndex = this.loadIndexByClientID(this.clientID);
			return this.getPlayers()[playerIndex].canPlayDevCard(devCard) && this.isCurrentTurn(this.clientID);
		};
		
		ClientModel.prototype.getResources = function () 
		{               
			return this.getPlayers()[this.getClientID()].getResources();
		};
		
		ClientModel.prototype.canOfferTrade = function () 
		{               
			return this.getPlayers()[this.getClientID()].canOfferTrade(this.getTradeOffer());
		};
		
		ClientModel.prototype.canAcceptTrade = function () 
		{               
			return this.getPlayers()[this.getClientID()].canAcceptTrade(this.getTradeOffer());
		};
		
		ClientModel.prototype.getTradingPartners = function () 
		{               
			var partners = [];
			var players = this.getPlayers();
			
			for(var player in players)
			{
				if(players[player].getPlayerID() != this. getClientID())
				{
					var partner = new Object();
					partner.name = players[player].getName();
					partner.color = players[player].getColor();
					partner.index = players[player].getOrderNumber();
					partners.push(partner);
				}				
			}
			return partners;
		};
		
		// Proxy Calls	
		
		ClientModel.prototype.sendMove = function (data, observerNotify) 
		{    
			this.getProxy().send(new catan.models.CommandObject(data), this.update, this.observers.notify(null, this.observers.observers));
		};	

		ClientModel.prototype.loadIndexByClientID = function(clientID) {
			var myint = -1;
			for(var player in this.players)
			{
				myint++;
				if(clientID == this.players[player].playerID){
					return myint;
				}
			}
			return -1;
		}
		
		ClientModel.prototype.isCurrentTurn = function(playerID){
			var currentPlayerID = this.loadPersonByIndex(this.turnTracker.currentTurn);
			if(playerID == currentPlayerID.playerID){
				return true;
			}
			return false;
		}


		/**
			This method will return the play at the specific index.  For example if index is 3 it will get the 4th player whose ID might be 11.
		*/
		ClientModel.prototype.loadPersonByIndex = function(index){
			var myint = -1;
			for(var newplayer in this.players)
			{
				myint++;
				if(myint == index) {
					return this.players[newplayer];
				}
			}
		}
        
		return ClientModel;
	}());	
	
	return ClientModel;
}());

