var catan = catan || {};

catan.TradeOffer = (function() {
	/**
		The Trade Offer holds whose trading and what to be traded
		
		@class TradeOffer
		@constructor
	*/
	function TradeOffer(){};

	/**
		Does the nessecary calculations for the trade to take place.
		<pre>
		PRE: The trade is defined
		POST: If True the proper cards trade hands
		POST: If False no cards trade hands
		</pre>
		@method acceptTrade
		@param {Boolean} bool True if the player accepts the trade, False if he doesn't
	*/
	function acceptTrade(bool) {};

	/**
		Returns a counter trade proposal to the player
		<pre>
		PRE:
		POST:
		</pre>
		@method proposeCounter
		@param {ResourceList} counterTrade the Trade that the recipient is willing to make
	*/
	function proposeCounter(counterTrade){};
	
	return TradeOffer;
})();
