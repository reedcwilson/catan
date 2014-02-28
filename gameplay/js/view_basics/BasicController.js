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

	BaseController.prototype.getClientID = function() {
		var id = this.getClientModel().clientID;
		if(id == undefined) {
			var mycookie = decodeURIComponent(document.cookie);
			var x = mycookie.indexOf('catan.user={"');
			mycookie = mycookie.substring(x);
			var n = mycookie.indexOf("}");
			var start = mycookie.indexOf("{");
			mycookie = mycookie.substring(start,n+1);
			var myjson = JSON.parse(mycookie);
			console.log(myjson);
			id = myjson.playerID;
		}
		return id;
	}
        
	return BaseController;	
}());

