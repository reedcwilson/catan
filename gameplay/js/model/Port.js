var catan = catan || {};
var catan.models = catan.models || {};

catan.models.Port = (function() 
{

    /**
        The Port class contains information about a trading post including what vertices it touches, what resources can be traded,
		and the trade ratio.
	
        @class Port
        @constructor       
    */
    function Port() {}
    
    /**
		Checks to see if the Player can trade with the Port.
        <pre>        
            PRE: It's the player's turn.
            POST: Port is unchanged.            
        </pre>
		@method canTradeWith
		@return {boolean} Whether or not the player can trade with the port.
    */
    Port.prototype.canTradeWith = function (){}; 
	
	    /**
		Carries out trade with the port.
        <pre>        
            PRE: It's a valid trade.
			PRE: It's the player's turn.
            POST: Trade is carried out, player's resources are updated.            
        </pre>
		@param{TradeOffer} The trade desired from the port.
		@method tradeWith		
    */
    Port.prototype.tradeWith = function (){};
	
    return Port;
})();