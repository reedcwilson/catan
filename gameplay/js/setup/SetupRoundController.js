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
	
	SetupRoundController.prototype.updateFromModel = function() {
		var model = this.getClientModel();
		var mycookie = decodeURIComponent(document.cookie);
		var n = mycookie.indexOf(";");
		var start = mycookie.indexOf("{");
		mycookie = mycookie.substring(start,n);
		var myjson = JSON.parse(mycookie);
		console.log(myjson.playerID);
		model.clientID = myjson.playerID;
		if(this.getMapController() != undefined) {
		
			//if(model.isCurrentTurn(model.clientID))
			//{
				this.getMapController().startMove("road", true, true);
			//}
			
		}
	};

	return SetupRoundController;
}());

