catan = catan || {};
catan.models = catan.models || {};

/**
	This module contains the MessageList

	@module	catan.models
	@namespace models
*/

catan.models.MessageList = (function() {

 core.defineProperty(MessageList.prototype, "lines");
  /**
    The MessageList class contains a list of messages

    @class MessageList
    @constructor
    */
  function MessageList() { 
  };



/**
      Loads all of the information into a MessageList object from the JSON
      <pre>
        PRE: messageJSON is a properlly formatted JSON with only the messageList info
        POST: All JSON attributes are stored in corresponding properties.
      </pre>
      @method setInfo
      @param {JSON} messageJSON - The JSON containing the information to load
    */

  MessageList.prototype.setInfo = function(messageJSON) {
     
        var temp = JSON.parse(messageJSON);
        this.setLines(temp.lines);
    }

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
  MessageList.prototype.printList = function() { 
    var messages ="";
    for(var i =0; i < this.lines.length; i++){
      messages = messages + this.lines[i].message;
    }

    return messages;
  };

  return MessageList;

}());
