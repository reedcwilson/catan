var catan = catan || {};
var catan.models = catan.models || {};

catan.models.Map = (function() 
{

    /**
        The Map class contains all the most important information about the game board including the Ports, the Robber's location,
		and all the hexes and their numbers.
	
        @class Map
        @constructor       
    */
    function Map() {}
    
    /**
		Checks to see if a Road can be placed by the player at the specified location.
        <pre>        
            PRE: HexGrid and Edges exist.
			PRE: It's the player's turn.
            POST: Edge is unchanged.            
        </pre>
		@method canPlaceRoad
		@return {boolean} Whether or not the player can place the road.
    */
    Map.prototype.canPlaceRoad = function (){}; 
	
	    /**
		Checks to see if a Settlement can be placed by the player at the specified location.
        <pre>        
            PRE: HexGrid and Edges exist.
			PRE: It's the player's turn.
            POST: Vertex is unchanged.            
        </pre>
		@method canPlaceSettlement
		@return {boolean} Whether or not the player can place the settlement.
    */
    Map.prototype.canPlaceSettlement = function (){};
	
		/**
		Checks to see if a City can be placed by the player at the specified location.
        <pre>        
            PRE: HexGrid and Edges exist.
			PRE: It's the player's turn.
            POST: Vertex is unchanged.            
        </pre>
		@method canPlaceSettlement
		@return {boolean} Whether or not the player can place the city.
    */
    Map.prototype.canPlaceCity = function (){};
	
    return Map;
})();