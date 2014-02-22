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
			
            view.setController(this);
            
            waitingView.setController(this);
            this.setWaitingView(waitingView);


            //waitingView.showModal();
            //view.showModal();
            //console.log("clientModel", clientModel);
           // console.log("resource types", ResourceTypes);
           // console.log("defnitions", Definitions);
            
         }

		core.forceClassInherit(DiscardController,Controller);

		core.defineProperty(DiscardController.prototype,"waitingView");

		DiscardController.prototype.initFromModel = function() {
			this.updateFromModel();

		}

		DiscardController.prototype.updateFromModel = function() {
			//is turn tracker status Discarding?
			var clientModel = this.getClientModel();
			var playerID = clientModel.clientID;
			var player = clientModel.players[this.loadIndexByClientID(playerID)];
			//if(player.resources)
			//console.log("player",player);
		}

		/**
		 Called by the view when the player clicks the discard button.
         It should send the discard command and allow the game to continue.
		 @method discard
		 @return void
		 */	
		DiscardController.prototype.discard = function(){
			var myPlayerIndex = this.loadIndexByClientID(this.getClientModel().getClientID());
			this.getClientModel().sendMove({type:"discardCards",playerIndex:myPlayerIndex,discardedCards:resourceList});
		}
        
		/**
		 Called by the view when the player increases the amount to discard for a single resource.
		 @method increaseAmount
		 @param {String} resource the resource to discard
		 @return void
		 */
		DiscardController.prototype.increaseAmount = function(resource){
		}
        
		/**
		 Called by the view when the player decreases the amount to discard for a single resource.
		 @method decreaseAmount
		 @param {String} resource the resource to discard
		 @return void
		 */
		DiscardController.prototype.decreaseAmount = function(resource){
		}
		
		return DiscardController;
	}());
	
    return DiscardController;
}());

