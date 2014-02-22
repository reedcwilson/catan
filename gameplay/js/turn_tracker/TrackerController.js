//STUDENT-EDITABLE-BEGIN
/**
The namespace for the turn tracker

@module catan.turntracker
@namespace turntracker
**/

var catan = catan || {};
catan.turntracker = catan.turntracker || {};

catan.turntracker.Controller = (function turntracker_namespace() 
{	

	var Controller = catan.core.BaseController;
    
	/**
		The controller class for the Turn Tracker
		@class TurnTrackerController 
		@extends misc.BaseController
		@param {turntracker.View} view The view for this object to control.
		@param {models.ClientModel} clientModel The clientModel for this object to control.
		@constructor
	**/
	var TurnTrackerController = (function TurnTrackerController_Class()
	{
	
		function TurnTrackerController(view, clientModel)
		{
			Controller.call(this,view,clientModel);
		}

		core.forceClassInherit(TurnTrackerController,Controller);

		TurnTrackerController.prototype.initFromModel = function() 
		{
			var clientModel = this.getClientModel();
			var playerID = clientModel.getClientID();
			var view = this.getView();
			clientModel.players.map(function (player) {
				if(player.getPlayerID() == clientModel.getClientID()) 
				{
					view.setClientColor(player.color);
				}
				view.initializePlayer(player.getOrderNumber(), player.name, player.color);
			});
			this.updateFromModel();
		}

		TurnTrackerController.prototype.updateFromModel = function() 
		{
			var clientModel = this.getClientModel();
			var view = this.getView();

			this.updatePlayerInfo(view, clientModel);
            this.updateTurnButton(view, clientModel);
		}

		TurnTrackerController.prototype.updatePlayerInfo = function(view, clientModel) 
		{
			var self = this;
			clientModel.players.map(function (player){
				var myHighlight = self.isCurrentTurn(player.getOrderNumber());
				view.updatePlayer({playerIndex:player.getOrderNumber(),score:player.victoryPoints,highlight:myHighlight,army:player.largestArmy,road:player.longestRoad});
            });
		}

		TurnTrackerController.prototype.updateTurnButton = function(view, clientModel)
		{
			var playerID = this.loadIndexByClientID(clientModel.getClientID());
			
            if(view.getStateElem() != undefined){
            	if(this.isCurrentTurn(playerID) && clientModel.turnTracker.status != "Rolling") 
            	{
            		view.updateStateView(true, "End Turn");
            	}
            	else{
					view.updateStateView(true, "End turn for: " + clientModel.turnTracker.currentTurn);
            	}
            }
		}

		/**
		 * Called by the view when the local player ends their turn.
		 * @method endTurn
		 * @return void
		 */
		TurnTrackerController.prototype.endTurn = function(){
			var myPlayerIndex = this.loadIndexByClientID(this.getClientModel().getClientID());
			this.getView().updateStateView(false, "Waiting for other Players...");
			this.getClientModel().sendMove({type:"finishTurn",playerIndex:myPlayerIndex});
		}
		
		return TurnTrackerController;
	} ());

	return TurnTrackerController;
} ());

