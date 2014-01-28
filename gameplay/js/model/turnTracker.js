
catan = catan || {};
catan.models = catan.models || {};

catan.models.TurnTracker = (function() {

  /**
    The TurnTracker class keeps track of whose turn it is

    @class TurnTracker
    @constructor
    */
  function TurnTracker() { };

  /**
    Checks the list to see if a card is in the list

    <pre>
      PRE: there is a current game
      POST: currentTurn is one more (or back to zero)
      POST: status is updated to the new turn's status
    </pre>

    @method nextTurn
    @return {number}
   */
  TurnTracker.prototype.nextTurn = function() { };

});
