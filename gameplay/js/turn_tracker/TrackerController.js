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
			var view = this.getView();
			var playerID = clientModel.getClientID();
			clientModel.players.map(function (player) {
				if(player.getPlayerID() == playerID) 
				{
					view.setClientColor(player.color);
				}
				view.initializePlayer(player.getOrderNumber(), player.name, player.color);
			});
			this.updateFromModel();
            clientModel.getTurnTracker().rollStatus = "NeedsRoll";
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
			var view = this.getView();
			clientModel.players.map(function (player){
				var myHighlight = clientModel.isCurrentTurn(player.playerID);
				var displayElem = new catan.definitions.DisplayElement.TurnTrackerPlayerElement(player.name, player.color);
				view.getDisplayElems()[player.getOrderNumber()] = (displayElem);

                var commandObj = {
                  playerIndex:player.getOrderNumber(),
                  score:player.victoryPoints,
                  highlight:myHighlight,
                  army:player.largestArmy,
                  road:player.longestRoad
                }

				view.updatePlayer(commandObj);
                if (!$(".tracker-player-box:eq(" + player.getOrderNumber() + ")").hasClass(player.color)) {
                  view.refreshTrackers();
                }
            });
		}

		TurnTrackerController.prototype.updateTurnButton = function(view, clientModel)
		{
			var playerID = clientModel.getPlayerIndex();
			
            if(view.getStateElem() != undefined){
            	if(clientModel.turnTracker.status == "FirstRound" || clientModel.turnTracker.status == "SecondRound")
            	{
					view.updateStateView(false, "Setup");
            	}
            	else if(clientModel.canEndTurn()) 
            	{
            		view.updateStateView(true, "End Turn");
            	}
            	else
            	{
					view.updateStateView(false, "Waiting for other players...");
            	}
            }
		}

		/**
		 * Called by the view when the local player ends their turn.
		 * @method endTurn
		 * @return void
		 */
		TurnTrackerController.prototype.endTurn = function()
		{
            var model = this.getClientModel();	
			var myPlayerIndex = model.getPlayerIndex();
			this.getView().updateStateView(false, "Waiting for other Players...");
			model.sendMove({type:"finishTurn",playerIndex:myPlayerIndex});
            model.getTurnTracker().rollStatus = "NeedsRoll";
            players = model.players;
            model.rollStupid = "NoRoll";
		}
		
		return TurnTrackerController;
	} ());

	return TurnTrackerController;
} ());

