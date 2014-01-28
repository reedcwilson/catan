var catan = catan || {};
catan.models = catan.models || {};

catan.models.Player = (function() {
  /**
    The Player class contains the information about the player, including
    the number of cities, color, id, etc..


    @class Player
    @constructor
    */
  function Player(){};

  /**
    Checks to see if the player has the Resource
    <pre>
    PRE: The player's resource list is defined
    PRE: resourceList is defined
    </pre>
    @method has Resource
    @param {resourceList} resourceList the resouceList to see if the player has >= the amount of resources in the list
    @return {boolean} Whether or not the Player has the resource
    */
  var hasResource = function(resourceList) { };
  function hasResource () {};

  /**
    Returns the number of points a player has
    <pre>
    PRE: Points != null
    POST: Returns the value
    </pre>
    @method getPoints
    @return (Number) the number of points the player has
    */
  var getPoints = function () {};

  /**
    Adds a city into a Player's points
    <pre>
    PRE: amount >= 0
    POST: newpoints = oldpoints + amount
    </pre>
    @method addPoints
    */
  var addPoints = function (amount) {};

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
  var addDevCard = function (DevCard) {};

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
  var playDevCard = function (DevCard) {};

  /**
    A get method to determine if the player has discarded
    <pre>
    PRE: None
    POST: None
    </pre>
    @method hasDiscarded
    @return {Boolean} Whether or not the player has discarded
    */
  var hasDiscarded = function () {};

  /**
    A set method to set the discard variable
    <pre>
    PRE: None
    POST: The player's discarded boolean = bool
    </pre>
    @method setDiscarded
    @param {Boolean} bool The boolean to set the discarded variable to
    */
  var setDiscarded = function (bool) {};

  /**
    A set method to set whether the Player has the Largest Army
    <pre>
    PRE: None
    POST: The player's largestArmy = bool;
    </pre>
    @method setLargestArmy
    @param {Boolean} bool The boolean to set the largestArmy variable to
    */

  var setLargestArmy = function (bool) {};

  /**
    A get method to see if the player has the LargestArmy
    <pre>
    PRE: largestArmy is defined
    POST: None
    </pre>
    @method hasLargestArmy
    @return {Boolean} Whether or not the player has the largest army
    */
  var hasLargestArmy = function () {};

  /**
    A set method to set whether or not the Player has the LongestRoad
    <pre>
    PRE: bool is a boolean
    POST: The players longestRoad = bool;
    </pre>
    @method setLongestRoad
    @param {Boolean} bool The boolean to set the longestRoad variable to.
    */
  var setLongestRoad = function (bool) {};

  /**
    A get method to return the player's name
    <pre>
    PRE: Name is defined
    POST: None
    </pre>
    @method getName
    @return {String} The name of the player
    */
  var getName = function () {};

  /**
    A set method to set a player's name
    <pre>
    PRE: string is a correct player name
    POST: The player's name = newName
    </pre>
    @method setName
    @param {String} newName The new player's name
    */
  var setName = function (newName) {};

  /**
    A get method to get the player's order number
    <pre>
    PRE: The player has an order number
    POST: None
    </pre>
    @method getOrderNumber
    @return {Number} The number of the player's order
    */
  var getOrderNumber = function () {};

  /**
    A set method to set the player's order number
    <pre>
    PRE: number is a number 1-4
    POST: The player's order number = ordNumber
    </pre>
    @method setOrderNumber
    @param {Number} ordNumber The number to set the player's order number
    */
  var setOrderNumber = function (ordNumber) {};

  /**
    A set method to set the player's color
    <pre>
    PRE: string is a legitamite color
    POST: The player's color = newColor
    </pre>
    @method setColor
    @param {String} newColor The player's new color
    */
  var setColor = function (newColor) {};

  /**
    A get method to get the player's color
    <pre>
    PRE: Color is defined
    POST: None
    </pre>
    @method getColor
    @return {String} The color of the player
    */
  var getColor = function () {};
	
    return {
      hasResource:hasResource,
      getPoints:getPoints,
      addPoints:addPoints,
      addDevCard:addDevCard,
      playDevCard:playDevCard,
      hasDiscarded:hasDiscarded,
      setDiscarded:setDiscarded,
      setLargestArmy:setLargestArmy,
      hasLargestArmy:hasLargestArmy,
      setLongestRoad:setLongestRoad,
      getName:getName,
      setName:setName,
      getOrderNumber:getOrderNumber,
      setOrderNumber:setOrderNumber,
      setColor:setColor,
      getColor:getColor
    }
})();

