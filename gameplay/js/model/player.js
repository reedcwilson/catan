
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

  		/**
    		The Player class contains the information about the player, including
    		the number of cities, color, id, etc..


    		@class Player
   	 		@constructor
    	*/
		function Player(){
		}

		/**
			Loads all of the information into a player object from the JSON
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
  		Player.prototype.addDevCard = function (DevCard) {

  		}

 	 	/**
    		Play's the DevCard from the player's hand.

    		<pre>
    			PRE: The player has the said DevCard
    			POST: The DevCard moves to the played pile
    			POST: The dev card action took place
    		</pre>
    		@method playDevCard
    		@param {DevCard} DevCard The DevCard to be played from the player's hand
    	*/
  		Player.prototype.playDevCard = function (DevCard) {

  		}

		return Player;
	}());
	return Player;
}());

