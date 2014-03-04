//STUDENT-EDITABLE-BEGIN
var catan = catan || {};
catan.resources = catan.resources || {};

/**
    This is the namespace for resources
    @module catan.resources
    @namespace resources
*/
    
catan.resources.Controller = (function resources_namespace() {

	var Controller = catan.core.BaseController;
	var Definitions = catan.definitions;
	
	/*	these are the values to use for updating the view: */
    var ROAD = Definitions.ROAD;
    var SETTLEMENT = Definitions.SETTLEMENT;
    var CITY = Definitions.CITY;
    var BUY_CARD = Definitions.BUY_CARD;
    var PLAY_CARD = Definitions.PLAY_CARD;
    var ARMY = Definitions.ARMY;
    var Resources = Definitions.ResourceTypes;
    var Buyables = new Array(ROAD, SETTLEMENT, CITY, BUY_CARD, PLAY_CARD, ARMY);
		
	var ResourceBarController = (function ResourceBarController_Class(){
    
		/**
		 Controller class for the Resources View.
		 @class ResourceBarController
		 @constructor
		 @extends misc.BaseController
		 @param{resources.View} view The resource view
		 @param{models.ClientModel} clientModel The client model
		 @param{Object} actions The actions to take for each user input. The value of actions.elem_name is a function that is called when the specific element is selected (accessed by calling actions["elem_name"]).  The valid element names are defined in StudentDefinitions.js
        */
		function ResourceBarController(view,clientModel,actions){
			this.setActions(actions);
			Controller.call(this,view,clientModel);
			this.settlementResources = new Array();
			this.settlementResources['wood'] = 1;
			this.settlementResources['brick'] = 1;
			this.settlementResources['sheep'] = 1;
			this.settlementResources['wheat'] = 1;
			this.roadResources = new Array();
			this.roadResources['brick'] = 1;
			this.roadResources['wood'] = 1;
			this.cityResources = new Array();
			this.cityResources['wheat'] = 2;
			this.cityResources['ore'] = 3;
			this.devCardResources = new Array();
			this.devCardResources['sheep'] = 1;
			this.devCardResources['wheat'] = 1;
			this.devCardResources['ore'] = 1;

		};

		core.forceClassInherit(ResourceBarController,Controller);
        
		ResourceBarController.prototype.constructor = ResourceBarController;
        
		core.defineProperty(ResourceBarController.prototype, "Actions");
		core.defineProperty(ResourceBarController.prototype, "roadResources");
		core.defineProperty(ResourceBarController.prototype, "settlementResources");
		core.defineProperty(ResourceBarController.prototype, "cityResources");
		core.defineProperty(ResourceBarController.prototype, "devCardResources");

		ResourceBarController.prototype.updateFromModel = function() {
			var clientModel = this.getClientModel();
			var view = this.getView();
			var playerID = clientModel.getClientID();
			var player = clientModel.players[clientModel.getPlayerIndex()];
			if(player == undefined) {
				player = clientModel.players[0];
			}
			for (var resource in player.resources) {
				view.updateAmount(resource, player.resources[resource]);
			}
			view.updateAmount("Roads", player.roads);
			if(player.hasResources(this.roadResources) && clientModel.isCurrentTurn(player.playerID) && player.roads > 0) {
				view.setActionEnabled("Roads", true);
			}
			else {
				view.setActionEnabled("Roads", false);
			}
			view.updateAmount(SETTLEMENT, player.settlements);
			if(player.hasResources(this.settlementResources) && clientModel.isCurrentTurn(player.playerID) && player.settlements > 0) {
				view.setActionEnabled("Settlements", true);
			}
			else {
				view.setActionEnabled("Settlements", false);
			}
			view.updateAmount("Cities", player.cities);
			if(player.hasResources(this.cityResources) && clientModel.isCurrentTurn(player.playerID) && player.cities > 0) {
				view.setActionEnabled("Cities", true);
			}
			else {
				view.setActionEnabled("Cities", false);
			}
			if(player.hasResources(this.devCardResources) && clientModel.isCurrentTurn(player.playerID) && this.bankHasResources()) {
				view.setActionEnabled("BuyCard", true);
			}
			else {
				view.setActionEnabled("BuyCard", false);
			}
			view.updateAmount("Soldiers", player.soldiers);
			
			
		};

		ResourceBarController.prototype.bankHasResources = function() {
			var bank = this.getClientModel().getDeck();
			for(var item in bank)
			{
				if(bank[item] > 0)
				{
					return true;
				}
			}
			return false;
		}
		
		/**
		 * The action to take on clicking the resource bar road button. Brings up the map 
		 * overlay and allows you to place a road.
		 * 
		 * @method buildRoad
		 * @return void
		 */
		ResourceBarController.prototype.buildRoad = function(){
        
            // NOTE: YOU DON'T NEED TO CHANGE THIS METHOD

            // This calls the "startMove" method on the Map Controller.
			this.getActions()[ROAD]();
		}
        
		/**
		 * The action to take on clicking the resource bar settlement button. Brings up the map 
		 * overlay and allows you to place a settlement.
		 * 
		 * @method buildSettlement
		 * @return void
		 */
		ResourceBarController.prototype.buildSettlement = function(){
        
            // NOTE: YOU DON'T NEED TO CHANGE THIS METHOD

            // This calls the "startMove" method on the Map Controller.
			this.getActions()[SETTLEMENT]();
		}

		/**
		 * The action to take on clicking the resource bar city button. Brings up the map 
		 * overlay and allows you to place a city.
		 * 
		 * @method buildCity
		 * @return void
		 */
		ResourceBarController.prototype.buildCity = function(){
        
            // NOTE: YOU DON'T NEED TO CHANGE THIS METHOD

            // This calls the "startMove" method on the Map Controller.
			this.getActions()[CITY]();
		}
        
		/**
		 * The action to take on clicking the resource bar "buy a card" button. 
		 * Should bring up the "buy a card" overlay.
		 * 
		 * @method buyCard
		 * @return void
		 */
		ResourceBarController.prototype.buyCard = function(){
        
            // NOTE: YOU DON'T NEED TO CHANGE THIS METHOD

            // This calls the "showModal" method on the Buy Card View.
			this.getActions()[BUY_CARD]();
		}
        
		/**
		 * The action to take on clicking the resource bar "play a card" button. 
		 * Should bring up the "play a card" overlay.
		 * 
		 * @method playCard
		 * @return void
		 */
		ResourceBarController.prototype.playCard = function(){
        
            // NOTE: YOU DON'T NEED TO CHANGE THIS METHOD

            // This calls the "showModal" method on the Dev Card View.
			this.getActions()[PLAY_CARD]();
		}
		
		return ResourceBarController;
		
	}());
	
	return ResourceBarController;
}());

