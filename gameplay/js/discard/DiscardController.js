//STUDENT-EDITABLE-BEGIN
/**
    This is the namespace for discarding cards
    @module catan.discard
    @namespace discard
*/

var catan = catan || {};
catan.discard = catan.discard || {};

catan.discard.Controller = (function discard_namespace(){

	var Controller = catan.core.BaseController;
	var Definitions = catan.definitions;
	var ResourceTypes = catan.definitions.ResourceTypes;
	
    /**
     * @class DiscardController
     * @constructor
     * @extends misc.BaseController
     * @param{discard.DiscardView} view
     * @param{misc.WaitOverlay} waitingView
     * @param{models.ClientModel} clientModel
     */
	var DiscardController = (function DiscardController_Class(){
	
		function DiscardController(view, waitingView, clientModel){
        
            Controller.call(this,view,clientModel);
            this.setView(view);
            view.setController(this);
            waitingView.setController(this);
            this.setWaitingView(waitingView);
			this.playerResources = {};	
			this.startDiscarding = true;
			this.totalCards; 
			this.totalcardsgivenup=0; 
		
         }

		core.forceClassInherit(DiscardController,Controller);
		core.defineProperty(DiscardController.prototype,"waitingView");

		DiscardController.prototype.initFromModel = function() {
			this.updateFromModel();
		}
	

		DiscardController.prototype.updateFromModel = function() {
			var clientModel = this.getClientModel();
			var playerID = clientModel.clientID;
			var discardView = this.getView();
			var player = clientModel.players[clientModel.getPlayerIndex()];
			var currentStatus = clientModel.turnTracker.status;
		
			if(currentStatus != "Discarding"){
					if(this.getWaitingView())
					this.getWaitingView().closeModal();
					this.startDiscarding = true;
				}
			else if(this.startDiscarding){
				if(currentStatus == "Discarding"){
					
					this.startDiscarding=false;
					var res = player.resources;
					var totalRes = res.brick + res.ore + res.sheep + res.wheat + res.wood;
					this.playerResources = {"wood":[res.wood,0],"brick":[res.brick,0],"sheep":[res.sheep,0],"wheat":[res.wheat,0],"ore":[res.ore,0]};
					
					if(totalRes < 8)
					{
						this.getWaitingView().showModal();
					}
					else
					{
						
						this.totalcardsgivenup=0;
						discardView.showModal();
						discardView.setResourceMaxAmount(ResourceTypes[0],res.wood);
						discardView.setResourceMaxAmount(ResourceTypes[1],res.brick);
						discardView.setResourceMaxAmount(ResourceTypes[2],res.sheep);
						discardView.setResourceMaxAmount(ResourceTypes[3],res.wheat);
						discardView.setResourceMaxAmount(ResourceTypes[4],res.ore);
						this.enableResource(ResourceTypes[0],0,res.wood);
						this.enableResource(ResourceTypes[1],0,res.brick);
						this.enableResource(ResourceTypes[2],0,res.sheep);
						this.enableResource(ResourceTypes[3],0,res.wheat);
						this.enableResource(ResourceTypes[4],0,res.ore);

						for(var i = 0; i < ResourceTypes.length; i++)
						{
							discardView.setResourceAmount(ResourceTypes[i],0);
						}
						this.totalCards = Math.floor(totalRes/2);
						var statemessage = "0/" + this.totalCards;
						
						discardView.setStateMessage(statemessage);
						
						discardView.setDiscardButtonEnabled(false);
						
					}
				}
			}
		}

		DiscardController.prototype.enableResource= function(resource, amount, total)
		{
			var discardView = this.getView();
			if(amount < total)
			{
				this.setResource(resource, amount, total);
			}
			else if(amount == total && total != 0)
			{
				discardView.setResourceAmountChangeEnabled(resource, false, true);
				
			}
			discardView.setResourceAmount(resource, amount);
		}

		DiscardController.prototype.setResource = function(resource, amount, total)
		{
			var discardView = this.getView();
			discardView.setResourceAmountChangeEnabled(resource, true, false);
				if(amount > 0)
				{
					discardView.setResourceAmountChangeEnabled(resource, true, true);
				}
				if(this.totalcardsgivenup ==this.totalCards)
				{
					discardView.setResourceAmountChangeEnabled(resource, false, true);
					if(amount ==0)
					{
						discardView.setResourceAmountChangeEnabled(resource, false, false);
					}
				}
				
		}

		/**
		 Called by the view when the player clicks the discard button.
         It should send the discard command and allow the game to continue.
		 @method discard
		 @return void
		 */	
		DiscardController.prototype.discard = function(){
			var discardView = this.getView();
			this.startDiscarding = true;
			var myPlayerIndex = this.getClientModel().getPlayerIndex();
			var resourceList = {brick:this.playerResources["brick"][1],ore:this.playerResources["ore"][1],sheep:this.playerResources["sheep"][1],wheat:this.playerResources["wheat"][1],wood:this.playerResources["wood"][1]};
			this.getClientModel().sendMove({type:"discardCards",playerIndex:myPlayerIndex,discardedCards:resourceList});
			discardView.closeModal();
			this.getWaitingView().showModal();
		}
        
		/**
		 Called by the view when the player increases the amount to discard for a single resource.
		 @method increaseAmount
		 @param {String} resource the resource to discard
		 @return void
		 */
		DiscardController.prototype.increaseAmount = function(resource){
			var discardView = this.getView();
			this.totalcardsgivenup += 1;
			this.playerResources[resource][1] += 1;
			discardView.setResourceAmount(resource,this.playerResources[resource][1]);
			this.enableResource(resource, this.playerResources[resource][1], this.playerResources[resource][0])
			var statemessage = "" +this.totalcardsgivenup+ "/" + this.totalCards;
			discardView.setStateMessage(statemessage);
			if(this.totalcardsgivenup ==this.totalCards)
				{
					discardView.setDiscardButtonEnabled(true);
					for(myResource in this.playerResources)
					{
						this.enableResource(myResource, this.playerResources[myResource][1], this.playerResources[myResource][0])
					}
				}
		}
        
		/**
		 Called by the view when the player decreases the amount to discard for a single resource.
		 @method decreaseAmount
		 @param {String} resource the resource to discard
		 @return void
		 */
		DiscardController.prototype.decreaseAmount = function(resource){
			var discardView = this.getView();
			this.totalcardsgivenup -= 1;
			this.playerResources[resource][1] -= 1;
			discardView.setResourceAmount(resource,this.playerResources[resource][1]);
			this.enableResource(resource, this.playerResources[resource][1], this.playerResources[resource][0])
			var statemessage = "" +this.totalcardsgivenup+ "/" + this.totalCards;
			discardView.setStateMessage(statemessage);
			if(this.totalcardsgivenup !=this.totalCards)
				{
					discardView.setDiscardButtonEnabled(false);
					for(myResource in this.playerResources)
					{
						this.enableResource(myResource, this.playerResources[myResource][1], this.playerResources[myResource][0])
					}

				}
		}
		
		return DiscardController;
	}());
	
    return DiscardController;
}());

