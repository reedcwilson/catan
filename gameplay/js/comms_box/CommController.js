//STUDENT-EDITABLE-BEGIN
/**
    This is the namespace for the communication classes (log and chat)
    @module catan.comm
    @namespace comm
**/

var catan = catan || {};
catan.controllers = catan.controllers || {};

catan.comm.Controller = (function () {
	
	var Controller = catan.core.BaseController;
	
    /**
        The basic controller class to extend from
        @class BaseCommController 
        @extends misc.BaseController
        @param {comm.BaseCommView} logView The view for this object to control.
        @param {models.ClientModel} model The view for this object to control.
        @constructor
    **/
	var BaseCommController = (function BaseCommController_Class(){
		
		BaseCommController.prototype = core.inherit(Controller.prototype);
		BaseCommController.prototype.contructor = BaseCommController;
		
		function BaseCommController(logView, model){
			Controller.call(this,logView,model);
		}

		/**
			Function called by the log and chat controllers updateFromModel
			Updates the log or chat model depending on the type send in
			@param {string} type The type of what you want to update 'log' for the log and 'chat' for the 'chat'
		**/
		BaseCommController.prototype.internalUpdate = function(type) {
			var model = this.getClientModel();
			var mylog;
			if(type == 'log') {
				mylog = model.getLog().getLines();
			}
			else {
				mylog = model.getChat().getLines();
			}		
			mylog.map(function (logpiece) {
				model.players.map(function (player) {
					if(player.name == logpiece.source) {
						logpiece.className = player.color;		
					}
				});
			});
			if(mylog.length != undefined) {
				this.getView().resetLines(mylog);
			}
		}
		
		return BaseCommController;
	}());
	
    
	var LogController = (function LogController_Class(){

        LogController.prototype = core.inherit(BaseCommController.prototype);
		LogController.prototype.constructor = LogController;

		/**
		The controller class for the Log
		@class LogController 
		@constructor
		@extends comm.BaseCommController
		@param {comm.LogView} logView The view for this object to control.
		@param {models.ClientModel} model The view for this object to control.
		**/
		function LogController(logView,model){
			BaseCommController.call(this,logView,model);
		}

		LogController.prototype.updateFromModel = function(){
			this.internalUpdate('log');
		}
        
		return LogController;
	}());
	
    
	var ChatController = (function ChatController_Class(){

        ChatController.prototype = core.inherit(BaseCommController.prototype);
		ChatController.prototype.constructor = ChatController;

		/**
		The controller class for the Chat
		@class ChatController 
		@constructor
		@extends comm.BaseCommController
		@param {comm.ChatView} logView The view for this object to control.
		@param {comm.ClientModel} model The view for this object to control.
		**/
		function ChatController(chatView,model){
			BaseCommController.call(this,chatView,model);
		}

		ChatController.prototype.updateFromModel = function(){
			this.internalUpdate('chat');
		}
        
		/**
		Called by the view whenever input is submitted
		@method addLine
		@param {String} lineContents The contents of the submitted string
		**/
		ChatController.prototype.addLine = function(lineContents){
			var clientModel = this.getClientModel();
			var myPlayerIndex = clientModel.getPlayerIndex();

            var commandObj = {
              type:"sendChat",
              playerIndex:myPlayerIndex, 
              content:lineContents
            }

			clientModel.sendMove(commandObj);		
		};
		
		return ChatController;
	}());
	
	return {
		LogController:LogController,
		ChatController:ChatController
	};
	
} ());

