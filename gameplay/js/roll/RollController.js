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
		
		function RollController(view, resultView, clientModel){
			this.setRollResultView(resultView);
			Controller.call(this,view,clientModel);
			this.rollInterval = false;
			this.showRollResult = false;
			clientModel.rollStupid = "NoRoll";
		};
        
		var counter;
        var _rollTimeout = 5;
        RollController.prototype.updateFromModel = function() {
          var model = this.getClientModel();
          var turnTracker = model.getTurnTracker();
          if(model.rollStupid === "NoRoll" && model.isCurrentTurn(model.getClientID()))
          {
			model.rollStupid = "Roll!";
          }
          if (model.canRoll()) {
            turnTracker.rollStatus = "Rolling";
            model.rollStupid = "Rolling!";
            var person = model.loadPersonByIndex(model.getTurnTracker().getCurrentTurn());
			var view = this.getView();
            view.changeMessage('Rolling automatically in ' + _rollTimeout + "...");

            view.showModal();
            counter = setInterval(core.makeAnonymousAction(view, tick), 1000);
          }
          if(!model.isCurrentTurn(model.getClientID())) {
				model.rollStupid = "NoRoll";
          }
        };
        
        var tick = function() {
          this.changeMessage('Rolling automatically in ' + --_rollTimeout + "...");
          if (_rollTimeout == -1) {
            clearInterval(counter);
            this.rollDice();
          }
        }
        
		/**
		 * This is called from the roll result view.  
         * It should close the roll result view and allow the game to continue.
		 * @method closeResult
		 * @return void
		**/
		var roll;
		RollController.prototype.closeResult = function(){
         	this.getRollResultView().closeModal();
         	var clientModel = this.getClientModel();

            var commandObj = {
              type:"rollNumber",
              playerIndex:this.getClientModel().getClientID(),
              number:roll
            }
         	clientModel.sendMove(commandObj);
		}
		
		/**
		 * This method generates a dice roll
		 * @method rollDice
		 * @return void
		**/
		RollController.prototype.rollDice = function(){
			clearInterval(counter);
			var view = this.getView();
            view.closeModal();
			roll = Math.floor(Math.random() * 6) + 1;
			roll += Math.floor(Math.random() * 6) + 1;
			this.getRollResultView().setAmount(roll);
			this.getRollResultView().showModal();
            _rollTimeout = 5;
            view.changeMessage('Rolling automatically in ' + _rollTimeout + "...");

		};
		
		return RollController;
	}());
	
	return RollController;

}());

