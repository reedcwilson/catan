
catan = catan || {};
catan.models = catan.models || {};

catan.models.DevCardList = (function() {

  /**
    The DevCardList class is a list containing DevCards with certain 
    operations

    @class DevCardList
    @constructor

    */
  function DevCardList() { };

  var _list = [];
  var _size;

  /**
    Checks the list to see if a card is in the list

    <pre>
      PRE: card != null
      POST: List is unchanged
    </pre>

    @method contains
    @param{card} card a valid Development Card
    @return {boolean} if the list contains the card
   */
  DevCardList.prototype.contains = function(card) { };

  /**
    Returns the value at the given index

    <pre>
      PRE: index is within the bounds of the array
      POST: value returned is a valid card found at the spot in the array
    </pre>

    @method getValueAtIndex
    @param{number} index a number less than size and greater than zero
    @return {DevCard} the card at the given index
   */
  DevCardList.prototype.getValueAtIndex = function(index) { };

  /**
    Adds the given card to the list

    <pre>
      PRE: card != null
      POST: size() is one greater
    </pre>

    @method addCard
    @param{DevCard} card a valid Development Card
    @return void
   */
  DevCardList.prototype.addCard = function(card) { };

  /**
    Removes the given card from the list

    <pre>
      PRE: card != null
      POST: size is one less
    </pre>

    @method removeCard
    @param{DevCard} card a valid Development Card
    @return void
   */
  DevCardList.prototype.removeCard = function(card) { };

  /**
    Returns the current size of the list

    <pre>
      PRE: List is initialized
      POST: List is unchanged
    </pre>

    @method size
    @return {number} the size of the list
   */
  DevCardList.prototype.size = function() { };

});
