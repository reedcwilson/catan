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
        RollController.prototype.updateFromModel = function() {
          var model = this.getClientModel();
          var turnTracker = model.getTurnTracker();
          if(model.rollStupid === "NoRoll" && model.isCurrentTurn(model.getClientID()))
          {
			model.rollStupid = "Roll!";
          }
          if (turnTracker.getStatus() == 'Rolling' && model.rollStupid === "Roll!" && model.isCurrentTurn(model.getClientID())) {
            turnTracker.rollStatus = "Rolling";
            model.rollStupid = "Rolling!";
            var person = model.loadPersonByIndex(model.getTurnTracker().getCurrentTurn());
			var view = this.getView()
            view.showModal();

			var tick = function() 
			{
				this.changeMessage('Rolling automatically in ' + timeout + "...");
                timeout = timeout - 1;
                if (timeout == -1) {
                  clearInterval(counter);
                  this.rollDice();
                }
            }

            var timeout = 5;
            counter = setInterval(core.makeAnonymousAction(view, tick), 1000);
            
          }
          if(!model.isCurrentTurn(model.getClientID())) {
				model.rollStupid = "NoRoll";
          }
        };
        
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
         	clientModel.sendMove({type:"rollNumber",playerIndex:this.getClientModel().getClientID(),number:roll});
		}
		
		/**
		 * This method generates a dice roll
		 * @method rollDice
		 * @return void
		**/
		RollController.prototype.rollDice = function(){
			clearInterval(counter);
			this.getView().closeModal();
			roll = Math.floor(Math.random() * 6) + 1;
			roll += Math.floor(Math.random() * 6) + 1;
			this.getRollResultView().setAmount(roll);
			this.getRollResultView().showModal();
		};
		
		return RollController;
	}());
	
	return RollController;

}());

