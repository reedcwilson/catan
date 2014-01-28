var catan = catan || {};
catan.models = catan.models || {};


catan.models.Edge = (function() {
	/**
		A specific edge on a Hex

		@class Edge
		@constructor
	*/
	function Edge(){};

	/**
		Checks to see if this edge is available. (does not have a road built on it)
		<pre>
		PRE: none
		POST: Returns true if there is no road on this edge, False if there is a road on the edge
		</pre>
		@method isEdgeAvailable
		@return {Bool} true if there is no road on this edge, False if there is a road on the edge
	*/
	
	function isEdgeAvailable() {};

	/**
		Returns the owner of the edge
		<pre>
		PRE: none
		POST: The owner id is returned. -1 if no owner
		</pre>
		@method getOwner
		@return {Number} The owner id. -1 if no owner
	*/
	function getOwner() {};

	/**
		Set the owner to the edge (this player placed a road on the edge)
		<pre>
		PRE: The edge must be available
		POST: The player now owns this edge. No other player can own it.
		</pre>
		@method setOwner
		@param {Owner} the id of the player
	*/
	function setOwner(owner) {};
	
	return Edge;
})();