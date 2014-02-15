//STUDENT-EDITABLE-BEGIN
/**
    This is the namespace the rolling interface
    @module catan.roll
    @namespace roll
*/

var catan = catan || {};
catan.roll = catan.roll || {};

catan.roll.Controller = (function roll_namespace(){

	var Controller = catan.core.BaseController;
    
	/**
		 * @class RollController
		 * @constructor
		 * @extends misc.BaseController
		 * @param{roll.View} view
		 * @param{roll.ResultView} resultView
		 * @param{models.ClientModel} clientModel
		 */ 
	var RollController = (function RollController_Class(){
		
		core.forceClassInherit(RollController,Controller);
 
		core.defineProperty(RollController.prototype,"rollResultView");
		
		function RollController(view,resultView, clientModel){
			this.setRollResultView(resultView);
			Controller.call(this,view,clientModel);
			this.rollInterval = false;
			this.showRollResult = false;
			this.updateFromModel();
		};


        RollController.prototype.updateFromModel = function(){
			var model = this.getClientModel();
			if(model.getTurnTracker().getStatus() == 'Rolling') {
				if(model.getTurnTracker().getCurrentTurn() == model.getClientID()){
					this.getView().showModal();
					this.getView().changeMessage('5');

					var timeout = 5;
					var counter = setInterval(tick, 1000);

					function tick() {
						view.changeMessage(timeout + " Meows");
						console.log(timeout + " Meows");
						timout = timeout -1;
						if(timeout == 0) {
							console.log('Cancelled');
							clearInterval(counter);
							this.rollDice();
						}
					}
					
					//counter = setInterval(timer, 1000);
				}
			}
        }
        
		/**
		 * This is called from the roll result view.  It should close the roll result view and allow the game to continue.
		 * @method closeResult
		 * @return void
		**/
		RollController.prototype.closeResult = function(){
			this.getRollResultView().closeModal();
		}
		
		/**
		 * This method generates a dice roll
		 * @method rollDice
		 * @return void
		**/
		RollController.prototype.rollDice = function(){
			console.log('meow');
		};
		
		return RollController;
	}());
	
	return RollController;

}());

