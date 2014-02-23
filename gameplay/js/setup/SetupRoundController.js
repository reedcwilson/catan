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
		if(this.getMapController() != undefined) {
			var cookie = document.cookie;
			console.log(cookie);
			if(this.isCurrentTurn(model.clientID))
			{
				this.getMapController().startMove("road", true, true);
			}
		}
	};

	return SetupRoundController;
}());

