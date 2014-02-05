catan = catan || {};
catan.models = catan.models || {};

/**
	This module contains the MessageLine

	@module catan.models
	@namepsace models
*/

catan.models.MessageLine = (function() {

  /**
    The MessageLine class is an object containing a single message

    <pre>
      Invariants:
        INVARIANT: the message should never change after it is instantiated
        INVARIANT: the source should never change after it is instantiated

      PRE: message is a string
      PRE: source is a string
      POST: message is now set
      POST: source is now set
    </pre>

    @class MessageLine
    @constructor
    @param {String} the message
    @param {String} the source of the message
    */
  function MessageLine(message, source) { };

  /**
    Returns the message

    <pre>
      PRE: none
      POST: none
    </pre>

    @method getMessage
    @return {String}
   */
  MessageLine.prototype.getMessage = function() { };

  /**
    Returns the source

    <pre>
      PRE: none
      POST: none
    </pre>

    @method getSource
    @return {String}
   */
  MessageLine.prototype.getSource = function() { };

});
