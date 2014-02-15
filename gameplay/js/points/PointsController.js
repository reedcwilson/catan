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
		this.loadModel(view, clientModel);
	}

	PointController.prototype.loadModel = function(view, clientModel){
		var playerID = clientModel.playerID;
		view.setPoints(clientModel.players[playerID].victoryPoints);
	}
	
	return PointController;	
}());
// STUDENT-REMOVE-END

