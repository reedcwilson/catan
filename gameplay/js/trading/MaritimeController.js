//STUDENT-EDITABLE-BEGIN
/**
    This is the namespace for maritime trading
    @module catan.trade
    @submodule catan.trade.maritime
    @namespace maritime
*/

var catan = catan || {};
catan.trade = catan.trade || {};
catan.trade.maritime = catan.trade.maritime || {};

catan.trade.maritime.Controller = (function trade_namespace()
{
    
	var Definitions = catan.definitions;
	var ResourceTypes = Definitions.ResourceTypes;
    
	var MaritimeController = ( function MaritimeController_Class()
	{

        var Controller = catan.core.BaseController;
        
        /**
		@class MaritimeController
		@constructor 
		@extends misc.BaseController
		@param {maritime.View} view
		@param {models.ClientModel} clientModel
		*/
		function MaritimeController(view,clientModel)
		{
			Controller.call(this,view,clientModel);
		};
        
		MaritimeController.prototype = core.inherit(Controller.prototype);
		
		var trading = false;
		var ports;
		var resourceGet = "getResource";
		var resourceGive = "giveResource";
		
		MaritimeController.prototype.updateFromModel = function()
		{
			
			var view = this.getView();
			var client = this.getClientModel();
			var player = client.players[client.getPlayerIndex()];
			ports = this.setPortValues();
			var hand = [];
			this.setHandValues(hand, player);

			if(client.isCurrentTurn(player.playerID))
			{
				if(!trading)
				{					
					view.showGiveOptions(hand);				
					view.hideGetOptions();
					if(hand.length != 0)
						view.setMessage("Choose what to give");
					else
						view.setMessage("You don't have enough resources");
						
					view.enableTradeButton(false);
				}
			}
			else
			{
				view.setMessage("Not your turn");
				view.hideGetOptions();
				view.showGiveOptions([]);
			}
		};	

		MaritimeController.prototype.setPortValues = function()
		{
			var client = this.getClientModel();
			var map = client.map;
			var index = client.getPlayerIndex();
			temp = [];
			
			temp["wood"] 	= map.getBestRatio("wood", index);
			temp["brick"] 	= map.getBestRatio("brick", index);
			temp["sheep"] 	= map.getBestRatio("sheep", index);
			temp["wheat"] 	= map.getBestRatio("wheat", index);
			temp["ore"] 	= map.getBestRatio("ore", index);
			
			return temp;
		}; 
		
		MaritimeController.prototype.setHandValues = function(hand, player)
		{
			for (var resource in ports) 
			{
				if(player.resources[resource] >= ports[resource])
					hand.push(resource);					
			}
		}; 
		
		/**
         * Called by the view when the player "undoes" their give selection
		 * @method unsetGiveValue
		 * @return void
		 */
		MaritimeController.prototype.unsetGiveValue = function()
		{
			var view = this.getView();
			trading = false;			
			this.updateFromModel();
		};
        
		/**
         * Called by the view when the player "undoes" their get selection
		 * @method unsetGetValue
		 * @return void
		 */
		MaritimeController.prototype.unsetGetValue = function()
		{
			var view = this.getView();
			view.showGetOptions(["wood","brick","sheep","wheat","ore"]);
			view.setMessage("Choose what to get");
			view.enableTradeButton(false);
		};
        
		/**
         * Called by the view when the player selects which resource to give
		 * @method setGiveValue
		 * @param{String} resource The resource to trade ("wood","brick","sheep","wheat","ore")
		 * @return void
		 */
		MaritimeController.prototype.setGiveValue = function(resource)
		{
			var view = this.getView();
			trading = true;
			view.selectGiveOption(resource, ports[resource]);
			view.showGetOptions(["wood","brick","sheep","wheat","ore"]);
			view.setMessage("Choose what to get");
			resourceGive = resource;
		};
        
		/**
         * Called by the view when the player selects which resource to get
		 * @method setGetValue
		 * @param{String} resource The resource to trade ("wood","brick","sheep","wheat","ore")
		 * @return void
		 */
		MaritimeController.prototype.setGetValue = function(resource)
		{
			var view = this.getView();
			view.selectGetOption(resource, 1);
			view.setMessage("Trade");
			view.enableTradeButton(true);
			resourceGet = resource;
		};
        
        function capFirst(str)
		{
            return str[0].toUpperCase() + str.slice(1);
        }
        
		/** Called by the view when the player makes the trade
		 * @method makeTrade
		 * @return void
		 */
		MaritimeController.prototype.makeTrade= function()
		{
			var client = this.getClientModel();
			var turnOrder = client.getPlayerIndex();			
			trading = false;			
			client.sendMove({type:'maritimeTrade', playerIndex: turnOrder, ratio: ports[resourceGive], inputResource: capFirst(resourceGive), outputResource: capFirst(resourceGet)});			
		}
		
       return MaritimeController;
	}());

	return MaritimeController;
}());


