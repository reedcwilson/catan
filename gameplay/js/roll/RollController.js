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
		core.defineProperty(RollController.prototype,"rolling");
		
		function RollController(view,resultView, clientModel){
			this.setRollResultView(resultView);
			Controller.call(this,view,clientModel);
			this.rollInterval = false;
			this.showRollResult = false;
			this.rolling = false;
		};
		var counter;
        RollController.prototype.updateFromModel = function() {
          var model = this.getClientModel();
          if (model.getTurnTracker().getStatus() == 'Rolling' && this.rolling != true) {
          this.rolling = true;
          var person = this.loadPersonByIndex(model.getTurnTracker().getCurrentTurn());
            if (person.getPlayerID() == model.getClientID()) {
              var view = this.getView()
              view.showModal();

              var timeout = 5;
              counter = setInterval(tick, 1000);

              function tick() {
                view.changeMessage('Rolling automagically in ' + timeout);
                timeout = timeout - 1;
                if (timeout == 0) {
                  clearInterval(counter);
                  view.rollDice();
                }
              }
            }
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

