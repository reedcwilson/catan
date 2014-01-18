/**
    This is the namespace for domestic trading
    @module catan.trade
    @submodule catan.trade.domestic
    @namespace domestic
*/

var catan = catan || {};
catan.trade = catan.trade || {};
catan.trade.domestic = catan.trade.domestic || {};

catan.trade.domestic.AcceptView = (function (){
	
	var BasicOverlay = catan.misc.BasicOverlay;
	var BasicElements = catan.definitions.DisplayElement.BasicElements;
	var DisplayElement = catan.definitions.DisplayElement;
	var ResourceTypes = catan.definitions.ResourceTypes;
	var GroupNames = catan.definitions.GroupNames;
    
    /**
        A view for accepting a trade from a player.  It inherits from misc.BaseOverlay.
        @class AcceptView 
        @constructor
        @extends misc.BaseOverlay
    **/
	this.AcceptView = function(idPrefix,titlePrefix){
		
		this.setQuestionLabel(document.createElement("label"));
		this.setButtons(makeButton.call(this));
		this.setPlayerNameLabel(document.createElement("label"));		
		this.setGiveElems({});
		this.setGetElems({});
        
		BasicOverlay.call(this, "Accept Trade Offer?");
	};
    
	AcceptView.prototype = core.inherit(BasicOverlay.prototype);
	
	core.defineProperty(AcceptView.prototype, "questionLabel");
	core.defineProperty(AcceptView.prototype, "buttons");
	core.defineProperty(AcceptView.prototype, "playerNameLabel");
	core.defineProperty(AcceptView.prototype, "GiveElems");
	core.defineProperty(AcceptView.prototype, "GetElems");
	
	AcceptView.prototype.generateBody = function(){
    
		var divContainer = document.createElement("div");
		divContainer.setAttribute("class","text-center");
		
		divContainer.appendChild(makeOfferArea.call(this));
			
		return divContainer;
	};
    
	AcceptView.prototype.generateFooter = function(){
				
        var divContainer = document.createElement("div");
                divContainer.appendChild(this.getButtons().getView());
                this.getButtons().setMessage("Accept", "No Thanks!");
            
        return divContainer;
    }
	
	AcceptView.prototype.tradeFunction = function(accepted){   
		this.getController().acceptTrade(accepted);
		clearView.call(this);
	}
	
	var makeOfferArea = function(){
		
		var div = document.createElement("div");
        div.appendChild(this.getPlayerNameLabel());
        div.appendChild(buildView(this.getGiveElems()));
        
        var label = document.createElement("label");
        label.textContent = "in exchange for"
        div.appendChild(label);
        div.appendChild(buildView(this.getGetElems()));
								
		return div;
	}
    
	/**
	 * Displays the name of the player that offered the trade.
	 * @method setPlayerName
	 * @param{String} playerName
	 */	
	AcceptView.prototype.setPlayerName = function(playerName){
		this.getPlayerNameLabel().innerHTML = playerName +" offered to give you";
	}
    
	/**
	 * Enables or disables accepting a trade
	 * @method setAcceptEnabled
	 * @param{Boolean} enabled
	 */
	AcceptView.prototype.setAcceptEnabled = function(enabled){
		
		this.getButtons().setMessage("Can't accept");
		this.getButtons().disable();
		if(enabled){
			this.getButtons().setMessage("Accept");
			this.getButtons().enable();
		}
	}
    
	/**
	 * Adds a resource to the display (what the player will have to give up)
	 * @method addGiveResource
	 * @param{String} resource the element to display (a resource: "wood","brick","sheep","wheat","ore")
	 * @param{int}amount
	 */
	AcceptView.prototype.addGiveResource = function(resource, amount){
		this.getGiveElems()[resource].show();
		this.getGiveElems()[resource].updateLabel(Math.abs(amount));
	}
    
	/**
	 * Adds a resource to the display (what the player will receive from the offer)
	 * @method addGetResource
	 * @param{String} resource the element to display (a resource: "wood","brick","sheep","wheat","ore")
	 * @param{int} amount
	 */
	AcceptView.prototype.addGetResource = function(resource, amount){
		
		this.getGetElems()[resource].show();
		this.getGetElems()[resource].updateLabel(Math.abs(amount));
	}
	
	var buildView = function(container){
			var groupDiv = document.createElement("div");
				groupDiv.setAttribute("style","display: inline-block; padding-bottom:20px;");	
				
			for(resourceIndex in ResourceTypes){
				var value = ResourceTypes[resourceIndex];
			
				var displayElem = new DisplayElement.ComboElement(GroupNames.resourceDisplay, value);
					groupDiv.appendChild(displayElem.getView());
					displayElem.hide();
				
				container[value] = displayElem;
			}
			return groupDiv;
	};
	
	var clearView = function(){
		console.log("clearing view");
		for(resourceIndex in ResourceTypes){
				var value = ResourceTypes[resourceIndex];
			
				this.getGiveElems()[value].hide();
				this.getGetElems()[value].hide();
			}
	}
    
	var makeButton = function(actionBool){
		var action = core.makeAnonymousAction(this, this.tradeFunction, [true]);
		var cancel = core.makeAnonymousAction(this, this.tradeFunction, [false]);
		var button = new DisplayElement.ButtonArea(action, cancel);
		button.setMessage("Accept","No Thanks!");
		return button;
	}
		
	return AcceptView;
}());

