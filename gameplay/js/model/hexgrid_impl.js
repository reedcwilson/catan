//STUDENT-EDITABLE-BEGIN   
var catan = catan || {};
catan.models = catan.models || {};

/**
	This module contains the map
	
	@module		catan.models
	@namespace models
*/

catan.models.Map = (function mapNameSpace(){
    
    var hexgrid = catan.models.hexgrid;
    
    var Map = (function Map_Class(){
       
		function Map(radius)
		{
			this.hexGrid = hexgrid.HexGrid.getRegular(radius, CatanHex);
		}
		Map.prototype.loadMap = function(mapJSON)
		{

		}
		
		Map.prototype.loadPorts = function(portJSON)
		{

		}

		Map.prototype.loadNumbers = function(numbersJSON)
		{

		}
		return Map;
		
    }());

    /**
	This class represents an edge. It inherits from BaseContainer.
    The data in this class (that you get from the JSON model) is independent of the hexgrid, except for the location.
    Therefore, we leave it up to you to decide how to implement it.
    It must however implement one function that the hexgrid looks for: 'isOccupied' - look at its documentation.
    From the JSON, this object will have two properties: location, and ownerID.
    Besides the 'isOccupied' method, you may add any other methods that you need.
    
    @constructor
    @extends hexgrid.BaseContainer
	
	@class CatanEdge
	*/
    var CatanEdge = (function CatanEdge_Class(){
        core.forceClassInherit(CatanEdge, hexgrid.BaseContainer);
        core.defineProperty(CatanEdge, "ownerID");
        function CatanEdge()
        {
			this.ownerID = -1;
        }

		CatanEdge.prototype.isOccupied = function()
		{
			return this.ownerID != -1
		}

		CatanEdge.prototype.setOwner = function(id)
		{
			this.ownerID = id;
		}

        return CatanEdge;
    }());
    
    /**
	This class represents a vertex. It inherits from BaseContainer.
    The data in this class (that you get from the JSON model) is independent of the hexgrid, except for the location.
    Therefore, we leave it up to you to decide how to implement it.
    It must however implement one function that the hexgrid looks for: 'isOccupied' - look at its documentation.
    From the JSON, this object will have three properties: location, ownerID and worth.
    Besides the 'isOccupied' method, you may add any other methods that you need.
    
    @constructor
    @extends hexgrid.BaseContainer
	
	@class CatanVertex
	*/
    var CatanVertex = (function CatanVertex_Class(){
    
        core.forceClassInherit(CatanVertex, hexgrid.BaseContainer);
        core.defineProperty(CatanVertex, "worth");
        core.defineProperty(CatanVertex, "ownerID");
        
        function CatanVertex()
        {
			this.worth = 0;
			this.ownerID = -1;
        }

		CatanVertex.prototype.isOccupied = function()
		{
			return this.ownerID != -1
		}

		CatanVertex.prototype.setOwner = function(id)
		{
			this.ownerID = id;
		}
		
		CatanVertex.prototype.getWorth = function()
		{
			return this.worth;
		}

		CatanVertex.prototype.setWorth = function(newWorth)
		{
			this.worth = newWorth;
		}
        return CatanVertex;
    }()); 
    
    
    /**
	This class represents a Hex. You may add any methods that you need (e.g., to get the resource/hex type, etc.)
    
    In order to work with the hexgrid, this class must extend hexgrid.BasicHex (already done in the code). You also need to implement
    a CatanVertex and CatanEdge classes (stubs are provided in this file).  Look at their documentation to see what needs to be done there.
     
    The hexgrid will be passed an instance of this class to use as a model, and will pull the constructor from that instance. 
    (The core.forceInherit sets the constructor, in case you are curious how that works)
      
    @constructor
    @param {hexgrid.HexLocation} location - the location of this hex. It's used to generate locations for the vertexes and edges.
    @extends hexgrid.BasicHex
	
	@class CatanVertex
	*/
	//TODO Add Resource Holding, Value Holding, Port info, has robber info
    var CatanHex = (function CatanHex_Class(){
    
        core.forceClassInherit(CatanHex, hexgrid.BasicHex);
		core.defineProperty(CatanHex, "landType");
		core.defineProperty(CatanHex, "isLand");
		core.defineProperty(CatanHex, "rollNumber");
        
        function CatanHex(location){          
            hexgrid.BasicHex.call(this,location,CatanEdge,CatanVertex);
        } 

		CatanHex.prototype.getLandType = new function(){
			return this.landType;
		}

		CatanHex.prototype.getIsLand = new function(){
			return this.isLand;
		}

		CatanHex.prototype.getRollNumber = new function(){
			return this.getRollNumber;
		}

        CatanHex.prototype.setLandType = new function(resourceType){
			this.landType = resourceType;
        }

        CatanHex.prototype.setIsLand = new function(bool){
        	this.isLand = bool;
        }

        CatanHex.prototype.setRollNumber = new function(number){
			this.rollNumber = number;
        }
        return CatanHex;

        
    }());

	var CatanPort = (function CatanPort_Class(){
		core.forceClassInherit(CatanPort, CatanHex);
		core.defineProperty(CatanHex, "tradeRatio");
		core.defineProperty(CatanHex, "inputResource");
		core.defineProperty(CatanHex, "validVertex1");
		core.defineProperty(CatanHex, "validVertex2");

		function CatanPort(ratio, resource, direction1, direction2) {
			this.tradeRatio = ratio;
			this.inputResource = resource;
		}

		CatanPort.prototype.getTradeRatio = new function(){
			return this.getTradeRatio;
		}

		CatanPort.prototype.getInputResource = new function(){
			return this.inputResource;
		}

		CatanPort.prototype.getValidVertex1 = new function(){
			return this.validVertex1;
		}

		CatanPort.prototype.getValidVertex2 = new function(){
			return this.ValidVertex2;
		}
	}());
    
	return Map;

}());


