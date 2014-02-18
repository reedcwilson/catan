// STUDENT-EDITABLE-BEGIN
/**
    This is the namespace to hold the base classes
    @module catan.misc
    @namespace misc
*/

var catan = catan || {};
catan.core = catan.core || {};

catan.core.BaseController = (function baseControllerClass(){

	/** 
		This class serves as the basis for all controller classes.		
		This constructor should be called by all child classes.
		
		@class BaseController
		@constructor 
		@param view - The controller's view
		@param {models.ClientModel} clientModel - The controller's client model
	*/
	function BaseController(view,clientModel){
		this.setView(view);
		this.setClientModel(clientModel);
		this.initFromModel();
	};
	
	core.forceClassInherit(BaseController, catan.core.Observer);
	core.defineProperty(BaseController.prototype,"View");
	core.defineProperty(BaseController.prototype,"ClientModel");
        

	/**
		This method will return the play at the specific index.  For example if index is 3 it will get the 4th player whose ID might be 11.
	*/
	BaseController.prototype.loadPersonByIndex = function(index){
		var myint = -1;
		for(var newplayer in this.getClientModel().players)
		{
			myint++;
			if(myint == index) {
				return this.getClientModel().players[newplayer];
			}
		}
	}

	/**
		Takes the playerID as input and sees if it is that player's current turn
	*/
	BaseController.prototype.isCurrentTurn = function(playerID){
			var clientModel = this.getClientModel();
			var currentPlayerID = this.loadPersonByIndex(clientModel.turnTracker.currentTurn);
			if(playerID == currentPlayerID.playerID){
				return true;
			}
			return false;
		}
	return BaseController;	
}());

