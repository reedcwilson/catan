//STUDENT-EDITABLE-BEGIN
/**
    This is the namespace for domestic trading
    @module catan.trade
    @submodule catan.trade.domestic
    @namespace domestic
*/

var catan = catan || {};
catan.trade = catan.trade ||{};
catan.trade.domestic = catan.trade.domestic ||{};

catan.trade.domestic.Controller= (function trade_namespace()
{

	var Controller = catan.core.BaseController;
	var Definitions = catan.definitions;
	var ResourceTypes = Definitions.ResourceTypes;
    
	var DomesticController = ( function DomesticController_Class()
	{
    
		/** 
		@class DomesticController
		@constructor 
		@extends misc.BaseController
		@param {domestic.View} view
		@param {misc.WaitOverlay} waitingView
		@param {domestic.AcceptView} acceptView
		@param {models.ClientModel} clientModel
		*/
		function DomesticController(view,waitingView,acceptView,clientModel)
		{
			Controller.call(this,view,clientModel);
			this.waitingView = waitingView;
			this.acceptView = acceptView;
			this.getView().setPlayers(this.getClientModel().getTradingPartners());
			this.clearTrades();
		};
        
		DomesticController.prototype = core.inherit(Controller.prototype);
		
		var resources = ["wood", "brick", "sheep", "wheat", "ore"];
		var handResources = [];
		var tradeResources = [];
		var tradeDirections = [];
		var tradee = -1;
		var tradeAccepted = false;
		DomesticController.prototype.updateFromModel = function()
		{
			var view = this.getView();
			var client = this.getClientModel();
			var player = client.players[client.getPlayerIndex()];
			var tradeOffer = client.getTradeOffer();
			this.initHandFromModel(player);
			
			if(tradeOffer.sender == -1)
			{
				tradeAccepted = false;
				this.waitingView.closeModal();
				if(client.isCurrentTurn(player.playerID))
				{
					if(!this.isTrading())
					{
						view.setTradeButtonEnabled(false);
						view.setStateMessage("Set the trade you want to make");
						view.setResourceSelectionEnabled(true);
						view.setPlayerSelectionEnabled(true);
						this.clearTrades();
					}
					if(this.isTradeReady() && tradee != -1)
					{
						view.setStateMessage("Send trade request!");
						view.setTradeButtonEnabled(true);
					}
					else
					{			
						view.setTradeButtonEnabled(false);
						if(this.isTradeReady())
							view.setStateMessage("Choose who to trade with");
					}
					if(tradee != -1 && !this.isTradeReady())
						view.setStateMessage("Set the trade you want to make");
				}
				else
				{
					view.setStateMessage("Not your turn!");
					view.setTradeButtonEnabled(false);
					view.setResourceSelectionEnabled(false);
					view.setPlayerSelectionEnabled(false);	
				}
			}
			else if(!tradeAccepted)
			{
				if(tradeOffer.receiver == player.orderNumber)
				{				
					var trader = client.loadPersonByIndex(tradeOffer.sender);
					this.acceptView.showModal();
					this.acceptView.setPlayerName(trader.name);
					this.displayOfferResources(this.acceptView);
					this.acceptView.setAcceptEnabled(player.canAcceptTrade(client.getTradeOffer()));
				}
				else
					this.waitingView.showModal();
			}
		};

		DomesticController.prototype.initHandFromModel = function(player)
		{
			for (var resource in resources)
			{
				var index = resources[resource];
				handResources[index] = player.resources[index];
			}
		};
		
		DomesticController.prototype.clearTrades = function()
		{
			for (var resource in resources)
			{			
				var index = resources[resource];
				tradeResources[index] = 0;
				tradeDirections[index] = 0;
			}
		};
		
		DomesticController.prototype.isTrading = function()
		{
			for (var resource in resources)
			{			
				var index = resources[resource];
				if(tradeResources[index] != 0 || tradeDirections[index] != 0)
					return true;
			}
			return false;
		};
		
		DomesticController.prototype.isTradeReady = function()
		{
			var send = false;
			var receive = false;
			for (var resource in resources)
			{			
				var index = resources[resource];
				if(tradeDirections[index] == 1 && tradeResources[index] > 0)
					send = true;
				else if(tradeDirections[index] == -1 && tradeResources[index] > 0)
					receive = true;
			}
			return send && receive;
		};
		
		DomesticController.prototype.generateOffer = function()
		{
			var offer = new Object();			
			
			for (var resource in resources)
			{			
				var index = resources[resource];
				offer[index] = tradeResources[index]*tradeDirections[index];
			}
			return offer;
		}
		
		DomesticController.prototype.displayOfferResources = function(vew)
		{
			var client = this.getClientModel();
			var offer = client.getTradeOffer().offer;
			
			for (var resource in resources)
			{			
				var index = resources[resource];
				if(offer[index] > 0)
					vew.addGiveResource(index, offer[index]);
				if(offer[index] < 0)
					vew.addGetResource(index, offer[index]);
			}							
		}		
         
		/******** Methods called by the Domestic View *********/
        
        /**
        * @method setResourceToSend
        * @param{String} resource the resource to send ("wood","brick","sheep","wheat","ore")
        * @return void
        */
		DomesticController.prototype.setResourceToSend = function(resource)
		{
			var view = this.getView();
			if(handResources[resource] == 0)
				view.setResourceAmountChangeEnabled(resource,false, false);
			else
			{
				view.setResourceAmountChangeEnabled(resource,true, false);
				tradeDirections[resource] = 1;
			}
			tradeResources[resource] = 0;
			view.setResourceAmount(resource, tradeResources[resource]);			
		};
        
		/**
		 * @method setResourceToReceive
		 * @param{String} resource the resource to receive ("wood","brick","sheep","wheat","ore")
		 * @return void
		 */
		 DomesticController.prototype.setResourceToReceive = function(resource)
		{
			var view = this.getView();
			view.setResourceAmountChangeEnabled(resource,true, false);
			tradeResources[resource] = 0;
			view.setResourceAmount(resource, tradeResources[resource]);
			tradeDirections[resource] = -1;
		};
        
		/**
		  * @method unsetResource
		  * @param{String} resource the resource to clear ("wood","brick","sheep","wheat","ore")
		  * @return void
		  */
		DomesticController.prototype.unsetResource = function(resource)
		{
			var view = this.getView();
			tradeResources[resource] = 0;
			view.setResourceAmountChangeEnabled(resource,false,false);
			tradeDirections[resource] = 0;			
		};
        
		/**
		 * @method setPlayerToTradeWith
		 * @param{int} playerNumber the player to trade with
		 * @return void
		 */
		DomesticController.prototype.setPlayerToTradeWith = function(playerNumber)
		{
			tradee = playerNumber;
		};
        
		/**
		* Increases the amount to send or receive of a resource
		* @method increaseResourceAmount
		* @param{String} resource ("wood","brick","sheep","wheat","ore")
		* @return void
		*/
		DomesticController.prototype.increaseResourceAmount = function(resource)
		{
			var view = this.getView();
			if(tradeDirections[resource] == -1 || tradeResources[resource] < handResources[resource] - 1)
				view.setResourceAmountChangeEnabled(resource,true, true);
			else
				view.setResourceAmountChangeEnabled(resource,false, true);
			view.setResourceAmount(resource, ++tradeResources[resource]);			
		};
        
		/**
		 * Decreases the amount to send or receive of a resource
		 * @method decreaseResourceAmount
		 * @param{String} resource ("wood","brick","sheep","wheat","ore")
		 * @return void
		 */
		DomesticController.prototype.decreaseResourceAmount = function(resource)
		{
			var view = this.getView();		
			if(tradeResources[resource] == 1)
				view.setResourceAmountChangeEnabled(resource,true, false);
			else
				view.setResourceAmountChangeEnabled(resource,true, true);
			view.setResourceAmount(resource, --tradeResources[resource]);
		};
        
		/**
		  * Sends the trade offer to the accepting player
		  * @method sendTradeOffer
		  * @return void
		  */
		DomesticController.prototype.sendTradeOffer = function()
		{
			var view = this.getView();	
			var client = this.getClientModel();
			client.sendMove({type:'offerTrade', playerIndex: client.getPlayerIndex(), offer: this.generateOffer(), receiver: tradee});			
			this.clearTrades();
			view.clearTradeView();
		};
        
        
		/******************* Methods called by the Accept Overlay *************/
		 
        /**
        * Finalizes the trade between players
        * @method acceptTrade
        * @param{Boolean} willAccept
        * @return void
		*/
		DomesticController.prototype.acceptTrade = function(willAcceptBool)
		{
			var client = this.getClientModel();
			var player = client.players[client.getPlayerIndex()];
			client.sendMove({type:'acceptTrade', playerIndex: client.getPlayerIndex(), willAccept:willAcceptBool});
			tradeAccepted = true;
			this.acceptView.closeModal();
		};
            
		return DomesticController;
    }());
			
	return DomesticController;
}());


