var catan = catan || {};
var catan.models = catan.models || {};

/**
 * @module catan.models
 * @namespace models
 */
catan.models.Port = (function() 
{

    /**
     * The Port class contains information about a trading post including what
     * vertices it touches, what resources can be traded, and the trade ratio.
     * 
     * @class Port
     * @constructor
     * @param {string} inputResource the resource associated with the port
     * @param {hexLocation} loc the location of the port
     * @param {string} the orientation of the port
     * @param {number} the ports resource ratio
     * @param {vertexLocation} vert1 the location of the first vertex
     * @param {vertexLocation} vert2 the location of the second vertex
     */
    var Port = function(inputResource, loc, orientation, ratio, vert1, vert2) {
      this.inputResource = inputResource;
      this.loc = loc;
      this.orientation = orientation;
      this.ratio = ratio;
      this.vert1 = vert1;
      this.vert2 = vert2;
    };
    
    /**
     * Checks to see if the Player can trade with the Port.
     *
     * <pre>        
     *    PRE: It's the player's turn.
     *    POST: Port is unchanged.            
     * </pre>
     *
	 * @method canTradeWith
	 * @return {boolean} Whether or not the player can trade with the port.
     */
    Port.prototype.canTradeWith = function (){}; 
	
	/**
     * Carries out trade with the port.
     *
     * <pre>        
     *    PRE: It's a valid trade.
	 * 	  PRE: It's the player's turn.
     *    POST: Trade is carried out, player's resources are updated.            
     * </pre>
     *
	 * @param{TradeOffer} The trade desired from the port.
	 * @method tradeWith		
     */
    Port.prototype.tradeWith = function (){};
	
    return Port;
})();
