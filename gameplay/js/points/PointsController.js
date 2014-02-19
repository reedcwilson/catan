//STUDENT-EDITABLE-BEGIN
var catan = catan || {};
catan.points = catan.points || {};
catan.points.Controller = catan.points.Controller || {};

/**
    This is the namespace for point display
    @module catan.points
    @namespace points
*/

catan.points.Controller = (function VPController_Class(){

	var Controller = catan.core.BaseController;

	PointController.prototype = core.inherit(Controller.prototype);

	core.defineProperty(PointController.prototype, "GameFinishedView");

	/** 
		@class PointController
		@constructor 
		@extends misc.BaseController
		@param {points.View} view
		@param {misc.GameFinishedView} gameFinishedView
		@param {models.ClientModel} clientModel
	*/
	function PointController(view, gameFinishedView, clientModel){
		this.setGameFinishedView(gameFinishedView);
		Controller.call(this,view,clientModel);
		this.updateFromModel();
	}

	PointController.prototype.updateFromModel = function(){
		var clientModel = this.getClientModel();
		var playerID = clientModel.clientID;
		var player = clientModel.players[this.loadIndexByClientID(playerID)];
		this.getView().setPoints(player.victoryPoints);
		if(player.victoryPoints >= 10) {
			this.getGameFinishedView().setWinner(player.name, true);
			this.getGameFinishedView().showModal();
		}
	}
	
	return PointController;	
}());
// STUDENT-REMOVE-END

