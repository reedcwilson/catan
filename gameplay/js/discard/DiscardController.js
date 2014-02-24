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
			this.totalCards; //total cards to discard
			this.totalcardsgivenup=0; //totalcardsremaining
		
         }

		core.forceClassInherit(DiscardController,Controller);

		core.defineProperty(DiscardController.prototype,"waitingView");



		DiscardController.prototype.initFromModel = function() {
			this.updateFromModel();

		}

		DiscardController.prototype.updateFromModel = function() {
			var clientModel = this.getClientModel();
			var playerID = clientModel.clientID;
			var player = clientModel.players[clientModel.loadIndexByClientID(playerID)];
			//console.log("new c",this.getObserverSubject());
			if(clientModel.turnTracker.status != "Discarding")
				{
					if(this.getWaitingView())
					this.getWaitingView().closeModal();
				}
			
			if(this.startDiscarding)
			{
				
				if(clientModel.turnTracker.status == "Discarding")
				{
					this.startDiscarding=false;
					var res = player.resources;
					var totalRes = res.brick + res.ore + res.sheep + res.wheat + res.wood;
					this.playerResources = {"wood":[res.wood,0],"brick":[res.brick,0],"sheep":[res.sheep,0],"wheat":[res.wheat,0],"ore":[res.ore,0]};
					console.log(this.playerResources);
					if(totalRes < 8)
					{
						this.getWaitingView().showModal();
					}
					else
					{

						this.getView().showModal();
						this.getView().setResourceMaxAmount(ResourceTypes[0],res.wood);
						this.getView().setResourceMaxAmount(ResourceTypes[1],res.brick);
						this.getView().setResourceMaxAmount(ResourceTypes[2],res.sheep);
						this.getView().setResourceMaxAmount(ResourceTypes[3],res.wheat);
						this.getView().setResourceMaxAmount(ResourceTypes[4],res.ore);
						this.enableResource(ResourceTypes[0],0,res.wood);
						this.enableResource(ResourceTypes[1],0,res.brick);
						this.enableResource(ResourceTypes[2],0,res.sheep);
						this.enableResource(ResourceTypes[3],0,res.wheat);
						this.enableResource(ResourceTypes[4],0,res.ore);

						for(var i = 0; i < ResourceTypes.length; i++)
						{
							this.getView().setResourceAmount(ResourceTypes[i],0);
						}
						this.totalCards = totalRes -7;
						var statemessage = "0/" + this.totalCards;
						
						this.getView().setStateMessage(statemessage);
						
						this.getView().setDiscardButtonEnabled(false);
						
					}
				}
			}
		}

		DiscardController.prototype.enableResource= function(resource, amount, total)
		{
			if(amount < total)
			{
				this.getView().setResourceAmountChangeEnabled(resource, true, false);
				if(amount > 0)
				{
					this.getView().setResourceAmountChangeEnabled(resource, true, true);
				}
				if(this.totalcardsgivenup ==this.totalCards)
				{
					this.getView().setResourceAmountChangeEnabled(resource, false, true);
					if(amount ==0)
					{
						this.getView().setResourceAmountChangeEnabled(resource, false, false);
					}
				}
			}
			else if(amount == total && total != 0)
			{
				this.getView().setResourceAmountChangeEnabled(resource, false, true);
				
			}



			this.getView().setResourceAmount(resource, amount);

		}

		/**
		 Called by the view when the player clicks the discard button.
         It should send the discard command and allow the game to continue.
		 @method discard
		 @return void
		 */	
		DiscardController.prototype.discard = function(){
			this.startDiscarding = true;
			var myPlayerIndex = this.getClientModel().loadIndexByClientID(this.getClientModel().getClientID());
			var resourceList = {brick:this.playerResources["brick"][1],ore:this.playerResources["ore"][1],sheep:this.playerResources["sheep"][1],wheat:this.playerResources["wheat"][1],wood:this.playerResources["wood"][1]};
			this.getClientModel().sendMove({type:"discardCards",playerIndex:myPlayerIndex,discardedCards:resourceList});
			this.getView().closeModal();
		}
        
		/**
		 Called by the view when the player increases the amount to discard for a single resource.
		 @method increaseAmount
		 @param {String} resource the resource to discard
		 @return void
		 */
		DiscardController.prototype.increaseAmount = function(resource){
			this.totalcardsgivenup += 1;
			this.playerResources[resource][1] += 1;
			this.getView().setResourceAmount(resource,this.playerResources[resource][1]);
			this.enableResource(resource, this.playerResources[resource][1], this.playerResources[resource][0])
			var statemessage = "" +this.totalcardsgivenup+ "/" + this.totalCards;
			this.getView().setStateMessage(statemessage);
			if(this.totalcardsgivenup ==this.totalCards)
				{
					this.getView().setDiscardButtonEnabled(true);
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
			this.totalcardsgivenup -= 1;
			this.playerResources[resource][1] -= 1;
			this.getView().setResourceAmount(resource,this.playerResources[resource][1]);
			this.enableResource(resource, this.playerResources[resource][1], this.playerResources[resource][0])
			var statemessage = "" +this.totalcardsgivenup+ "/" + this.totalCards;
			this.getView().setStateMessage(statemessage);
			if(this.totalcardsgivenup !=this.totalCards)
				{
					this.getView().setDiscardButtonEnabled(false);
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

