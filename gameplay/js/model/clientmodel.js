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
	@param {integer} clientID The id of the client player, extracted from the cookie
    */
	var ClientModel = (function ClientModelClass()
	{   
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
			$.get('/games/model', function(model)
			{	
				this.update(model);
			}			
            success(model);
		}		
		
		ClientModel.prototype.update = function (model) 
		{               
			this.setBank(model.bank);
			this.setBiggestArmy(model.biggestArmy);
			this.setChat(model.chat);
			this.setLog(model.log);
			this.setLongestRoad(model.longestRoad);
			this.setMap(model.map);
			this.setPlayers(model.players);
			this.setTradeOffer(model.tradeOffer);
			this.setTurnTracker(model.turnTracker);
			this.setWinner(model.winner);
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
			return this.getPlayer(this.getPlayerID).needsToDiscard();
		};
		
		ClientModel.prototype.hasResources = function (resourceList) 
		{               
			return this.getPlayer(this.getPlayerID).hasResources(resourceList);
		};
		
		ClientModel.prototype.canPlayDevCard = function (devCard) 
		{               
			return this.getPlayer(this.getPlayerID).canPlayDevCard(devCard);
		};
		
		ClientModel.prototype.getResources = function () 
		{               
			return this.getPlayer(this.getPlayerID).getResources();
		};
		
		ClientModel.prototype.canOfferTrade = function () 
		{               
			
		};
		
		ClientModel.prototype.canAcceptTrade = function () 
		{               
			
		};
		
		// Proxy Calls
		
		ClientModel.prototype.login = function (username, password) 
		{               
			var commandObject = new LoginUser(username, password);
			this.getProxy().send(commandObject);
		};
		
		ClientModel.prototype.register = function (username, password) 
		{               
			var commandObject = new RegisterUser(username, password);
			this.getProxy().send(commandObject);
		};
		
		ClientModel.prototype.changeLogLevel = function (data) 
		{               
			var commandObject = new ChangeLogLevel(data);
			this.getProxy().send(commandObject);
		};
		
		ClientModel.prototype.joinGame = function (data) 
		{               
			var commandObject = new JoinGame(data);
			this.getProxy().send(commandObject);
		};
		
		ClientModel.prototype.gameCommands = function (data) 
		{               
			var commandObject = new GameCommands(data);
			this.getProxy().send(commandObject, this.update);
		};
		
		ClientModel.prototype.addAI = function (data) 
		{               
			var commandObject = new AddAI(data);
			this.getProxy().send(commandObject);
		};
		
		ClientModel.prototype.playMonopoly = function (data) 
		{               
			var commandObject = new PlayMonopoly(data);
			this.getProxy().send(commandObject, this.update);
		};
		
		ClientModel.prototype.finishTurn = function (data) 
		{               
			var commandObject = new FinishTurn(data);
			this.getProxy().send(commandObject, this.update);
		};
		
		ClientModel.prototype.sendChat = function (data) 
		{               
			var commandObject = new SendChat(data);
			this.getProxy().send(commandObject, this.update);
		};
		
		ClientModel.prototype.rollNumber = function (data) 
		{               
			var commandObject = new RollNumber(data);
			this.getProxy().send(commandObject, this.update);
		};
		
		ClientModel.prototype.playMonument = function (data) 
		{               
			var commandObject = new PlayMonument(data);
			this.getProxy().send(commandObject, this.update);
		};
		
		ClientModel.prototype.buyDevCard = function (data) 
		{               
			var commandObject = new BuyDevCard(data);
			this.getProxy().send(commandObject, this.update);
		};
		
		ClientModel.prototype.acceptTrade = function (data) 
		{               
			var commandObject = new AcceptTrade(data);
			this.getProxy().send(commandObject, this.update);
		};
		
		ClientModel.prototype.playYearOfPlenty = function (data) 
		{               
			var commandObject = new PlayYearOfPlenty(data);
			this.getProxy().send(commandObject, this.update);
		};
		
		ClientModel.prototype.createGame = function (data) 
		{               
			var commandObject = new CreateGame(data);
			this.getProxy().send(commandObject);
		};
		
		ClientModel.prototype.robPlayer = function (data) 
		{               
			var commandObject = new RobPlayer(data);
			this.getProxy().send(commandObject, this.update);
		};
		
		ClientModel.prototype.maritimeTrade = function (data) 
		{               
			var commandObject = new MaritimeTrade(data);
			this.getProxy().send(commandObject, this.update);
		};
		
		ClientModel.prototype.playSoldier = function (data) 
		{               
			var commandObject = new playSoldier(data);
			this.getProxy().send(commandObject, this.update);
		};
		
		ClientModel.prototype.buildSettlement = function (data) 
		{               
			var commandObject = new buildSettlement(data);
			this.getProxy().send(commandObject, this.update);
		};
		
		ClientModel.prototype.playRoadBuilding = function (data) 
		{               
			var commandObject = new playRoadBuilding(data);
			this.getProxy().send(commandObject, this.update);
		};
		
		ClientModel.prototype.discardCards = function (data) 
		{               
			var commandObject = new DiscardCards(data);
			this.getProxy().send(commandObject, this.update);
		};
		
		ClientModel.prototype.buildCity = function (data) 
		{               
			var commandObject = new BuildCity(data);
			this.getProxy().send(commandObject, this.update);
		};
		
		ClientModel.prototype.offerTrade = function (data) 
		{               
			var commandObject = new OfferTrade(data);
			this.getProxy().send(commandObject, this.update);
		};
		
		ClientModel.prototype.buildRoad = function (data) 
		{               
			var commandObject = new BuildRoad(data);
			this.getProxy().send(commandObject, this.update);
		};
		
		//Getters and Setters
		
		ClientModel.prototype.setBank = function (bank_in) 
		{               
			this.bank = new catan.models.ResourceList(bank_in);
		};
		
		ClientModel.prototype.getBank = function() 
		{               
			return this.bank;
		};
		
		ClientModel.prototype.setDeck = function (deck_in) 
		{               
			this.deck = new catan.models.DevCardList(deck_in);
		};
		
		ClientModel.prototype.getDeck = function() 
		{               
			return this.deck;
		};
		
		ClientModel.prototype.setBiggestArmy = function (biggestArmy_in) 
		{               
			this.biggestArmy = biggestArmy_in;
		};
		
		ClientModel.prototype.getBiggestArmy = function() 
		{               
			return this.biggestArmy;
		};

		ClientModel.prototype.setChat = function (chat_in) 
		{               
			this.chat = new catan.models.MessageList(chat_in);
		};
		
		ClientModel.prototype.getChat = function() 
		{               
			return this.chat;
		};
		
		ClientModel.prototype.setLog = function (log_in) 
		{               
			this.log = new catan.models.MessageList(log_in);
		};
		
		ClientModel.prototype.getLog = function() 
		{               
			return this.log;
		};
		
		ClientModel.prototype.setLongestRoad = function (longestRoad_in) 
		{               
			this.longestRoad = longestRoad_in;
		};
		
		ClientModel.prototype.getLongestRoad = function() 
		{               
			return this.longestRoad;
		};
		
		ClientModel.prototype.setMap = function (map_in) 
		{               
			this.map = new catan.models.Map(map_in);
		};
		
		ClientModel.prototype.getMap = function() 
		{               
			return this.map;
		};
		
		ClientModel.prototype.setPlayers = function (players_in) 
		{               
			var tempPlayers = [];
			
			for(var i = 0; i < players_in.length; i++)
				tempPlayers[]i = new catan.models.Player(players_in[i]);
				
			this.players = tempPlayers;
		};
		
		ClientModel.prototype.getPlayer = function(index) 
		{               
			return this.players[index];
		};
		
		ClientModel.prototype.setPlayerID = function (playerID_in) 
		{               
			this.playerID = playerID_in;
		};
		
		ClientModel.prototype.getPlayerID = function() 
		{               
			return this.playerID;
		};
		
		ClientModel.prototype.setProxy = function (proxy_in) 
		{               
			this.proxy = proxy_in;
		};
		
		ClientModel.prototype.getProxy = function() 
		{               
			return this.proxy;
		};
		
		ClientModel.prototype.setTradeOffer = function (tradeOffer_in) 
		{               
			this.tradeOffer = new catan.models.TradeOffer(tradeOffer_in);
		};
		
		ClientModel.prototype.getTradeOffer = function() 
		{               
			return this.tradeOffer;
		};
		
		ClientModel.prototype.setTurnTracker = function (turnTracker_in) 
		{               
			this.turnTracker = new catan.models.TurnTracker(turnTracker_in);
		};
		
		ClientModel.prototype.getTurnTracker = function() 
		{               
			return this.turnTracker;
		};
		
		ClientModel.prototype.setWinner = function (winner_in) 
		{               
			this.winner = winner_in;
		};
		
		ClientModel.prototype.getWinner = function() 
		{               
			return this.winner;
		};
        
		return ClientModel;
	}());	
	
	return ClientModel;
}());

