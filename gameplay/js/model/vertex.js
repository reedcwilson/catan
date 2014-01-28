var catan = catan || {};
catan.models = catan.models || {};


catan.models.Vertex = (function() {
	/**
		A specific vertex on a Hex

		@class Vertex
		@constructor
	*/
	function Vertex(){};

	/**
		Checks to see if this vertex is available. (does not have a settlement or city built on it)
		<pre>
		PRE: none
		POST: Returns true if there is no settlement or city on this vertex
		</pre>
		@method isVertexAvailable
		@return {Bool} true if no settlement or city on the vertex, false otherwise
	*/
	
	function isVertexAvailable() {};

	/**
		Returns the current worth of the vertex
		<pre>
		PRE: none
		POST: The worth is returned. -1 if no worth
		</pre>
		@method getWorth
		@return {Number} The current worth of the vertex, -1 if no worth
	*/
	function getWorth() {};

	/**
		Increases the worth of the vertex by 1
		<pre>
		PRE: A settlement must be built on the edge or a city must be built in replace of a settlement
		POST: The value of the vertex will be increased by 1
		</pre>
		@method increaseWorth
	*/
	function increaseWorth() {};
/**
		Assigns a player to this vertex
		<pre>
		PRE: Player must build on this vertex
		PRE: Player cannot build on this vertex if another player already owns the vertex
		POST: The player now owns this vertex. No other player can own it.
		</pre>
		@method setOwnerId
		@param {Owner} the id of the player
	*/
	function setOwnerId(owner) {};
	
	return Vertex;
})();