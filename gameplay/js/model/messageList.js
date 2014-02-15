catan = catan || {};
catan.models = catan.models || {};

/**
	This module contains the MessageList

	@module	catan.models
	@namespace models
*/

catan.models.MessageList = (function() 
{
	core.defineProperty(MessageList.prototype, "lines");
	
	/**
		The MessageList class contains a list of messages

		@class MessageList
		@constructor
    */
	
	function MessageList() {}

	/**
		Loads all of the information into a MessageList object from the JSON
		<pre>
			PRE: messageJSON is a properlly formatted JSON with only the messageList info
			POST: All JSON attributes are stored in corresponding properties.
		</pre>
		@method setInfo
		@param {JSON} messageJSON - The JSON containing the information to load
    */

	MessageList.prototype.setInfo = function(messageJSON) 
	{     
        var tempLines = [];
        for(var i = 0; i < messageJSON.lines.length; i++)
			tempLines[i] = new catan.models.MessageLine(messageJSON.lines[i].message, messageJSON.lines[i].source);
		this.setLines(tempLines);
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
   
	MessageList.prototype.printList = function() 
	{ 
		var messages ="";
		for(var i =0; i < this.lines.length; i++)	     
			messages += 'Source: ' + this.lines[i].source + ' Message: ' + this.lines[i].message + '\n';    

		return messages;
	};

	return MessageList;

}());

/**
	This module contains the MessageLine

	@module catan.models
	@namespace models
*/

catan.models.MessageLine = (function() 
{

  core.defineProperty(MessageLine.prototype, "source");
  core.defineProperty(MessageLine.prototype, "message");
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
    @param {String} message the message
    @param {String} source the source of the message
    */
	
	function MessageLine(message, source) 
	{
		this.setMessage(message);
		this.setSource(source);
	}
	
	return MessageLine;
}());

