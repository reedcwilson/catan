//STUDENT-EDITABLE-BEGIN
/**
    This is the namespace for development cards
    @module catan.devCards
    @namespace devCards
*/

var catan = catan || {};
catan.devCards = catan.devCards || {};

catan.devCards.Controller = (function()
{

	var Controller = catan.core.BaseController;
	var Definitions = catan.definitions;
	
	var DevCardController = (function card_namespace()
	{		
		core.forceClassInherit(DevCardController,Controller);
		core.defineProperty(DevCardController.prototype, "BuyView");
		core.defineProperty(DevCardController.prototype, "soldierAction");
		core.defineProperty(DevCardController.prototype, "roadAction");

		/**
		 * @class DevCardController
		 * @constructor
		 * @extends misc.BaseController
		 * @param {devCards.DevCardView} view
		 * @param {devCards.BuyCardView} buyView
		 * @param {models.ClientModel} clientModel
		 * @param {function} soldierAction
		 * @param {function} roadAction
		 */
		function DevCardController(view, buyView, clientModel, soldierAction, roadAction)
		{
			Controller.call(this,view,clientModel);
			this.setBuyView(buyView);
			this.setSoldierAction(soldierAction);
			this.setRoadAction(roadAction);
		}

		DevCardController.prototype.initFromModel = function()
		{			
			var clientModel = this.getClientModel();
			var view = this.getView();	
		}
		
		DevCardController.prototype.updateFromModel = function()
		{			
			var clientModel = this.getClientModel();
			var view = this.getView();	
					
			var player = clientModel.players[this.loadIndexByClientID(clientModel.clientID)];
			
			view.updateAmount("soldier", player.newDevCards.soldier + player.oldDevCards.soldier);
			view.updateAmount("yearOfPlenty", player.newDevCards.yearOfPlenty + player.oldDevCards.yearOfPlenty);
			view.updateAmount("monopoly", player.newDevCards.monopoly + player.oldDevCards.monopoly);
			view.updateAmount("roadBuilding", player.newDevCards.roadBuilding + player.oldDevCards.roadBuilding);
			view.updateAmount("monument", player.newDevCards.monument + player.oldDevCards.monument);
			
			view.setCardEnabled("soldier", player.canPlayDevCard("soldier"));
			view.setCardEnabled("yearOfPlenty", player.canPlayDevCard("yearOfPlenty"));
			view.setCardEnabled("monopoly", player.canPlayDevCard("monopoly"));
			view.setCardEnabled("roadBuilding", player.canPlayDevCard("roadBuilding"));
			view.setCardEnabled("monument", player.canPlayDevCard("monument"));
		}
		
		/**
		 * Called when the player buys a development card
		 * @method buyCard
		 * @return void
		 */
		DevCardController.prototype.buyCard = function()
		{
			var clientModel = this.getClientModel();			
			clientModel.sendMove({type:'buyDevCard', playerIndex: clientModel.getTurnTracker().getCurrentTurn()});			
		}
        
		/**
		 * Called when the player plays a year of plenty card
		 * @method useYearOfPlenty
		 * @param {String} resource1 The first resource to obtain
		 * @param {String} resource2 The second resource to obtain
		 * @return void
		 */
		DevCardController.prototype.useYearOfPlenty = function(resource1, resource2)
		{
			var clientModel = this.getClientModel();			
			clientModel.sendMove({type:'buyDevCard', playerIndex: clientModel.getTurnTracker().getCurrentTurn(), resource1: resource1, resource2: resource2});
		}
        
		/**
		 * Called when the player plays a monopoly card
		 * @method useMonopoly
		 * @param {String} resource the resource to obtain
		 * @return void
		 */
		DevCardController.prototype.useMonopoly= function(resource)
		{
			var clientModel = this.getClientModel();			
			clientModel.sendMove({type:'Monopoly', resource: resource, playerIndex: clientModel.getTurnTracker().getCurrentTurn()});
		}
        
		/**
		 * Called when the player plays a monument card
		 * @method useMonument
		 * @return void
		 */
		DevCardController.prototype.useMonument = function()
		{
			var clientModel = this.getClientModel();			
			clientModel.sendMove({type:'Monument', playerIndex: clientModel.getTurnTracker().getCurrentTurn()});
		}
        
		/**
		 * Called when the player plays a soldier card
		 * @method useSoldier
		 * @return void
		 */
		DevCardController.prototype.useSoldier= function()
		{
			this.getSoldierAction()();
		}
        
		/**
		 * Called when the player plays the road building card
		 * @method useRoadBuild
		 * @return void
		 */
		DevCardController.prototype.useRoadBuild = function(resource)
		{
			this.getRoadAction()();
		}

		return DevCardController;
	}());
	
	return DevCardController;
}());

