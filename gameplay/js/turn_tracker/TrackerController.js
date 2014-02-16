//STUDENT-EDITABLE-BEGIN
/**
The namespace for the turn tracker

@module catan.turntracker
@namespace turntracker
**/

var catan = catan || {};
catan.turntracker = catan.turntracker || {};

catan.turntracker.Controller = (function turntracker_namespace() {	

	var Controller = catan.core.BaseController;
    
	/**
		The controller class for the Turn Tracker
		@class TurnTrackerController 
		@extends misc.BaseController
		@param {turntracker.View} view The view for this object to control.
		@param {models.ClientModel} clientModel The clientModel for this object to control.
		@constructor
	**/
	var TurnTrackerController = (function TurnTrackerController_Class(){
	
		function TurnTrackerController(view, clientModel){
			Controller.call(this,view,clientModel);
            // NOTE: The view.updateViewState and view.updatePlayer will not work if called from here.  Instead, these
            //          methods should be called later each time the client model is updated from the server.
		}

		core.forceClassInherit(TurnTrackerController,Controller);

		//Only needs to be called once, on load
		TurnTrackerController.prototype.initFromModel = function() {
			var clientModel = this.getClientModel();
			var playerID = clientModel.getClientID();
			var view = this.getView();
            for(var i in clientModel.players) {
				player = clientModel.players[i];
				if(i==playerID) {
					view.setClientColor(player.color);
				}
				view.initializePlayer(i, player.name, player.color);
				console.log(i, player.name, player.color);
            }
		}

		/**
		 * Called by the view when the local player ends their turn.
		 * @method endTurn
		 * @return void
		 */
		TurnTrackerController.prototype.endTurn = function(){
		}
		
		return TurnTrackerController;
	} ());

	return TurnTrackerController;
} ());

