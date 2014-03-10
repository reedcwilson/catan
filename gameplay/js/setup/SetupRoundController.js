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
	SetupRoundController.prototype.redirect = function(model){
				if(model.turnTracker.status != "FirstRound" && model.turnTracker.status != "SecondRound") {
				window.location = "catan.html"
		}
	}
	SetupRoundController.prototype.startSecondRound = function(player){
		if(myCount == 0){
			player.startedRoad = false;
			myCount = 4;
			}
	}
	SetupRoundController.prototype.proceedWithSetup = function(player, model){
		if(player.startedRoad == false){
				if(model.isCurrentTurn(model.clientID)){
					if(player.roads == 15 || (player.roads == 14 && player.settlements ==4)){
						player.startedRoad = true;
						player.startedSettlement = false;
						this.getState().place();
					}
				}
			}
			else if(player.startedSettlement == false &&
			model.isCurrentTurn(model.clientID) &&
			((player.roads == 14 && player.settlements == 5) || (player.roads==13 && player.settlements==4)))
			{	
				player.startedSettlement = true;
				this.getState().place();
			
			}
	}

	var myCount = 0;
	SetupRoundController.prototype.updateFromModel = function() {
		var model = this.getClientModel();
		this.redirect(model);
		var clientID = model.getClientID();
		model.clientID = clientID;
		var player = model.loadPersonByIndex(model.getPlayerIndex());
		
		if(model.turnTracker.status == "SecondRound"){
			this.startSecondRound(player);
		}	
		if(this.getMapController() != undefined) {
			this.proceedWithSetup(player, model);
		}
	};

	return SetupRoundController;
}());

