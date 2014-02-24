//STUDENT-EDITABLE-BEGIN
/**
    This is the namespace for the intitial game round
    @module catan.setup
    @namespace setup
*/

var catan = catan || {};
catan.setup= catan.setup || {};

catan.setup.Controller = (function Setup_Class(){
	
	var Controller = catan.core.BaseController;

    SetupRoundController.prototype = core.inherit(Controller.prototype);
    core.defineProperty(SetupRoundController.prototype, "MapController");
    
	/** 
		@class SetupRoundController
		@constructor 
		@extends misc.BaseController
		@param {models.ClientModel} clientModel
		@param {map.MapController} mapController
	*/
	function SetupRoundController(clientModel, mapController) {
			Controller.call(this,undefined,clientModel);
			this.setMapController(mapController);
			this.updateFromModel();
	}

	SetupRoundController.prototype.getClientFromCookie = function() {
		var mycookie = decodeURIComponent(document.cookie);
		var n = mycookie.indexOf(";");
		var start = mycookie.indexOf("{");
		mycookie = mycookie.substring(start,n);
		var myjson = JSON.parse(mycookie);
		console.log(myjson.playerID);
		return myjson.playerID;
	}
	
	SetupRoundController.prototype.updateFromModel = function() {
		var model = this.getClientModel();
		if(model.turnTracker.status != "FirstRound" && model.turnTracker.status != "SecondRound") {
				window.location = "catan.html"
		}
		var clientID = this.getClientFromCookie();
		model.clientID = clientID;
		
		var player = model.loadPersonByIndex(model.loadIndexByClientID(model.clientID));
		if(this.getMapController() != undefined) {
			if(model.isCurrentTurn(model.clientID) && 
				player.startedRoad == false && 
				(player.roads == 15 || (player.roads == 14 && player.settlements ==4)))
			{
				player.startedRoad = true;
				this.getMapController().startMove("road", true, true);
			}
			if(player.startedSettlement == false &&
			model.isCurrentTurn(model.clientID) &&
			((player.roads == 14 && player.settlements == 5) || (player.roads==13 && player.settlements==4)))
			{	
				player.startedSettlement = true;
				this.getMapController().startMove("settlement",true, true);
			}
			
		}
	};

	return SetupRoundController;
}());

