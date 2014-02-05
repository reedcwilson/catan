catan = catan || {};
catan.models = catan.models || {};

/**
	This module contains the MessageList

	@module	catan.models
	@namespace models
*/

catan.models.MessageList = (function() {

  /**
    The MessageList class contains a list of messages

    @class MessageList
    @constructor
    */
  function MessageList() { };

  /**
    Prints the list to the console and returns a string representation of the
    list

    <pre>
      PRE: none
      POST: The returned string is either empty if there are no items in the 
      list or it contains a formatted string 
    </pre>

    @method printList
    @return {string} a string representing the messages in the list
   */
  MessageList.prototype.printList = function() { };

});
