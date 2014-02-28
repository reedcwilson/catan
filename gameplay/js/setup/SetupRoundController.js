//STUDENT-EDITABLE-BEGIN
/**
    This is the namespace for the intitial game round
    @module catan.setup
    @namespace setup
*/

var catan = catan || {};
catan.setup= catan.setup || {};

catan.setup.PlaceRoad = function(roundCtrl)
{
	this.roundCtrl = roundCtrl;

	this.place = function(){

		roundCtrl.getMapController().startMove("road", true, true);
		roundCtrl.changeState(new catan.setup.PlaceSettlement(roundCtrl));
		
	}
}
catan.setup.PlaceSettlement = function(roundCtrl)
{
	this.roundCtrl = roundCtrl;

	this.place = function(){

		roundCtrl.getMapController().startMove("settlement",true, true);
		roundCtrl.changeState(new catan.setup.PlaceRoad(roundCtrl));
		
	}
}

catan.setup.Controller = (function Setup_Class(){
	
	var Controller = catan.core.BaseController;

    SetupRoundController.prototype = core.inherit(Controller.prototype);
    core.defineProperty(SetupRoundController.prototype, "MapController");
    core.defineProperty(SetupRoundController.prototype, "State");
    
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
			this.setState(new catan.setup.PlaceRoad(this));
			this.updateFromModel();
	}
	SetupRoundController.prototype.changeState = function(state) {
		this.setState(state);
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
	var myCount = 0;
	SetupRoundController.prototype.updateFromModel = function() {
		var model = this.getClientModel();
		if(model.turnTracker.status != "FirstRound" && model.turnTracker.status != "SecondRound") {
				window.location = "catan.html"
		}
		var clientID = this.getClientID();
		model.clientID = clientID;


		
		var player = model.loadPersonByIndex(model.loadIndexByClientID(model.clientID));
		
		if(model.turnTracker.status == "SecondRound")
		{
			if(myCount == 0)
			{
			player.startedRoad = false;
			myCount = 4;
			}
		}	
		if(this.getMapController() != undefined) {
			if(player.startedRoad == false)
			
			{
			console.log("it wasn't player.startedRoad")
				if(model.isCurrentTurn(model.clientID))
				{
					console.log("it wasn't model.isCurretn")
				if(player.roads == 15 || (player.roads == 14 && player.settlements ==4))
			{
				player.startedRoad = true;
				player.startedSettlement = false;
				//this.getMapController().startMove("road", true, true);
				this.getState().place();
		
				console.log("round two and you got called but nothing happened");
				
			}
		}
	}
			else if(player.startedSettlement == false &&
			model.isCurrentTurn(model.clientID) &&
			((player.roads == 14 && player.settlements == 5) || (player.roads==13 && player.settlements==4)))
			{	
				player.startedSettlement = true;
				//this.getMapController().startMove("settlement",true, true);
				this.getState().place();
			
			}
		
	}
	};

	return SetupRoundController;
}());

