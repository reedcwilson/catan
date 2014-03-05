/**
	This contains interface for player
	@module catan.models
	@namespace models
*/

var catan = catan || {};
catan.models = catan.models || {};

/**
	This module contains the player

	@module catan.models
	@namespace models
*/

catan.models.Player = (function playerNameSpace() {

	var Player = (function Player_Class(){

		core.defineProperty(Player.prototype, "MAX_GAME_POINTS");
		core.defineProperty(Player.prototype, "resources");
		core.defineProperty(Player.prototype, "oldDevCards");
		core.defineProperty(Player.prototype, "newDevCards");
		core.defineProperty(Player.prototype, "roads");
		core.defineProperty(Player.prototype, "cities");
		core.defineProperty(Player.prototype, "settlements");
		core.defineProperty(Player.prototype, "soldiers");
		core.defineProperty(Player.prototype, "victoryPoints");
		core.defineProperty(Player.prototype, "monuments");
		core.defineProperty(Player.prototype, "longestRoad");
		core.defineProperty(Player.prototype, "largestArmy");
		core.defineProperty(Player.prototype, "playedDevCard");
		core.defineProperty(Player.prototype, "discarded");
		core.defineProperty(Player.prototype, "playerID");
		core.defineProperty(Player.prototype, "orderNumber");
		core.defineProperty(Player.prototype, "name");
		core.defineProperty(Player.prototype, "color");
		core.defineProperty(Player.prototype, "startedRoad");
		core.defineProperty(Player.prototype, "startedSettlement");
  		/**
    		The Player class contains the information about the player, including
    		the number of cities, color, id, etc..


    		@class Player
   	 		@constructor
    	*/
		function Player(){
			this.setStartedRoad(false);
			this.setStartedSettlement(false);
		}

		/**
			Loads all of the information into a player object from the JSON
			<pre>
				PRE: playerJSON is a properlly formatted JSON with only the player info
				POST: All JSON attributes are stored in corresponding properties.
			</pre>
			@method setInfo
			@param {JSON} playerJSON - The JSON containing the information to load
		*/
		Player.prototype.setInfo = function(playerJSON) {
			for (var item in playerJSON)
			{
				var methodName = item.charAt(0).toUpperCase() + item.slice(1);
				this["set"+methodName](playerJSON[item]);
			}
		}
  		/**
    		Checks to see if the player has the Resource
    		<pre>
    			PRE: The player's resource list is defined
    			PRE: resourceList is defined
    		</pre>
    		@method hasResources
    		@param {resourceList} resourceList the resouceList to see if the player has >= the amount of resources in the list
    		@return {boolean} Whether or not the Player has the resource
    	*/
  		Player.prototype.hasResources = function(resourceList) { 
			for(var item in resourceList)
			{
				if(resourceList[item] > this.resources[item]) {
					return false;
				}
			}
			return true;
  		}


  		/**
    	Adds the DevCard to the Player's list of DevCards
    	<pre>
    		PRE: The player's devList is defined
    		PRE: DevCard is defined
    		POST: The player's devList has one more of the DevCard
    	</pre>
    	@method addDevCard
    	@param {DevCard} DevCard The DevCard to be added to the player's list of dev cards
    	*/
  		Player.prototype.addDevCard = function (devCard) {
			this.newDevCards[devCard] += 1;
  		}

		/**
			Query if player needs to discard

			<pre>
				PRE: The player is defined
				POST: None
			</pre>
			@method needsToDiscard
			@return {bool} whether he needs to discard
		*/
  		Player.prototype.needsToDiscard = function(){
			return this.getNumOfCards() > 7;
  		}

		/**
			Query if player can play a certain devcard

			<pre>
				PRE: The devCards Lists are defined
				PRE: devCard is a proper devCard string
				POST: None
			</pre>
			@method canPlayDevCard
			@param {string} devCard - the Dev card to play. Ex: "yearOfPlenty"
			@return {bool} whether he can play a dev card
		*/
  		Player.prototype.canPlayDevCard = function(devCard){
  			if(devCard == "monument") {
				return this.oldDevCards[devCard] > 0 || this.newDevCards[devCard] > 0;
  			}
  			else {
				return this.oldDevCards[devCard] > 0 && !this.playedDevCard;
			}
  		}

		/**
			Query if player can offer a certain trade

			<pre>
				PRE: tradeOffer is a valid tradeOffer class
				PRE: resources are defiend
				POST: None
			</pre>
			@method canOfferTrade
			@param {TradeOffer} tradeOffer - the tradeOffer to look at
			@return {bool} whether he can offer a trade
		*/
  		Player.prototype.canOfferTrade = function(tradeOffer){
  			var offer = tradeOffer.getOffer();
			return this.hasResources(offer);
  		}

		/**
			Query if player can accept a trade

			<pre>
				PRE: tradeOffer is a valid tradeOffer class
				PRE: resources are defiend
				POST: None
			</pre>
			@method canAcceptTrade
			@param {TradeOffer} tradeOffer - the tradeOffer to look at
			@return {bool} whether he needs to discard
		*/
  		Player.prototype.canAcceptTrade = function(tradeOffer){
  			var offer = tradeOffer.getOffer();
			for(var resource in offer) {			
				if(offer[resource] < 0 && this.resources[resource] < (-1)*offer[resource]) {
					return false;	
				}
			}
			return true;
  		}

		/**
			Determines how many resource cards the player has in his hand.

			<pre>
				PRE: resources are defiend
				POST: None
			</pre>
			@method getNumOfCards
			@return {int} The number of cards in his hand.
		*/
  		Player.prototype.getNumOfCards = function () {
			var numOfCards = 0;
			for (var item in this.resources)
			{
				numOfCards += this.resources[item];
			}
			return numOfCards;
  		}

		return Player;
	}());
	return Player;
}());


/**
	This module contains the tradeOffer

	@module catan.models
	@namespace models
*/

catan.models.TradeOffer = (function TradeOfferSpace() {

		core.defineProperty(TradeOffer.prototype, "sender");
		core.defineProperty(TradeOffer.prototype, "receiver");
		core.defineProperty(TradeOffer.prototype, "offer");
  		/**
    		The tradeOffer class contains the information about trading

    		@class tradeOffer
   	 		@constructor
    	*/
		function TradeOffer(){
		}

		/**
			Loads all of the information into a tradeOffer object from the JSON
			<pre>
				PRE: tradeOfferJSON is a valid JSON format with the correct info
				POST: this.sender = JSON.sender
				POST: this.receiver = JSON.receiver
				POST: this.offer = JSON.offer
			</pre>
			@method setInfo
			@param {JSON} tradeOfferJSON - The JSON containing the information to load
		*/
		TradeOffer.prototype.setInfo = function(tradeOfferJSON){
			if(tradeOfferJSON != undefined)
			{
				this.setSender(tradeOfferJSON.sender);
				this.setReceiver(tradeOfferJSON.receiver);
				this.setOffer(tradeOfferJSON.offer);
			}
			else
			{
				this.setSender(-1);
				this.setReceiver(-1);
			}
		}

	return TradeOffer;
}());
