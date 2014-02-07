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

		Map.prototype.setInfo = function(mapJSON){
			//hexes.setInfo
			//ports.setInfo
			//robber.setInfo
			//numbers.setInfo
			
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
        core.defineProperty(CatanEdge.prototype, "ownerID");
        function CatanEdge()
        {
			this.ownerID = -1;
        }
        
		/**
			Returns whether the edge is Occupied
			<pre>
				PRE: ownerID exsists
				POST: None
			</pre>
			@method isOccupied
			@return {bool} If it is occupied or not
		*/
		CatanEdge.prototype.isOccupied = function()
		{
			return this.ownerID != -1
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
        core.defineProperty(CatanVertex.prototype, "worth");
        core.defineProperty(CatanVertex.prototype, "ownerID");
        
        function CatanVertex()
        {
			this.worth = 0;
			this.ownerID = -1;
        }

		/**
			Returns whether the vertex is Occupied
			<pre>
				PRE: ownerID exsists
				POST: None
			</pre>
			@method isOccupied
			@return {bool} If it is occupied or not
		*/
		CatanVertex.prototype.isOccupied = function()
		{
			return this.ownerID != -1
		}

        return CatanVertex;
    }()); 
    
    
    /**
	This class represents a Hex. You may add any methods that you need (e.g., to get the resource/hex type, etc.)
    
    In order to work with the hexgrid, this class must extend hexgrid.BasicHex (already done in the code). You also need to implement
    a CatanVertex and CatanEdge classes (stubs are provided in this file).  Look at their documentation to see what needs to be done there.
     
    The hexgrid will be passed an instance of this class to use as a model, and will pull the constructor from that instance. 
    (The core.forceInherit sets the constructor, in case you are curious how that works)
		
  /**    
    @constructor
    @param {hexgrid.HexLocation} location - the location of this hex. It's used to generate locations for the vertexes and edges.
    @extends hexgrid.BasicHex
	
	@class CatanVertex
	*/
    var CatanHex = (function CatanHex_Class(){
    
        core.forceClassInherit(CatanHex, hexgrid.BasicHex);
		core.defineProperty(CatanHex.prototype, "landType");
		core.defineProperty(CatanHex.prototype, "isLand");
		core.defineProperty(CatanHex.prototype, "rollNumber");
		core.defineProperty(CatanHex.prototype, "hasRobber");

		//if it is not a Port everything else is undefined
		core.defineProperty(CatanHex.prototype, "isPort");
		core.defineProperty(CatanHex.prototype, "tradeRatio");
		core.defineProperty(CatanHex.prototype, "inputResource");
		core.defineProperty(CatanHex.prototype, "validVertex1");
		core.defineProperty(CatanHex.prototype, "validVertex2");
        
        function CatanHex(location){          
            hexgrid.BasicHex.call(this,location,CatanEdge,CatanVertex);
        } 

		/**
			Sets the info on the hex using a hexJSON
			<pre>
				PRE: hexJSON is properlly formatted and contains all of the info about the hex.
				POST: The information is all stored inside this hex object.
			</pre>
			@method setInfo
			@param {JSON} hexJSON - The hexJSON to load the info from.
		*/
		CatanHex.prototype.setInfo = function(hexJSON){
			this.setIsLand(hexJSON.isLand);
			if(hexJSON.isLand){
				this.setLandType(hexJSON.landtype);
				this.setIsPort(false);
			}
			this.setVertexInfo(hexJSON.vertexes);
			this.setEdgeInfo(hexJSON.edges);
		}

		/**
			Takes in the information about the vertexes and sets all of the.  Called by CatanHex.setInfo.
			<pre>
				PRE: vertexJSON is properlly formatted and contains all of the vertexes for the hex
				POST: All of the vertexes info is stored with the hex
			</pre>
			@method setVertexInfo
			@param {JSON} vertexJSON - The information about the edges stored in a JSON.
		*/
		CatanHex.prototype.setVertexInfo = function(vertexJSON) {
			for(var position in vertexJSON) {
				var vertex = this.vertexes[position];
				var vertexinfo = vertexJSON[position];
				vertex.setWorth(vertexinfo.value.worth);
				vertex.setOwnerID(vertexinfo.value.ownerID);
			}
		}

		/**
			Takes in the information about the edges and sets all of them. Called by CatanHex.setInfo.
			<pre>
				PRE: edgeJSON is properlly formatted and contains all of the edges for the hex
				POST: All of the edges have the proper info from the JSON
			</pre>
			@method setEdgeInfo
			@param {JSON} edgeJSON - The information about the edges stored in a JSON.
		*/
		CatanHex.prototype.setEdgeInfo = function(edgeJSON) {
			for(var position in edgeJSON) {
				var edge = this.edges[position];
				var edgeinfo = edgeJSON[position];
				edge.setOwnerID(edgeinfo.value.ownerID);
			}
		}

		/**
			Sets the information about the port using the port info
			<pre>
				PRE: portJSON is properlly formatted and contains only the port info.
				POST: All values are taken from the portJSON and stored locally
			</pre>
			@method setPortInfo
			@param {JSON} portJSON - The JSON containing the info about the port
		*/
		CatanHex.prototype.setPortInfo = function(portJSON){
			this.setIsPort(true);
			this.setTradeRatio(portJSON.ratio);
			this.setInputResource(portJSON.inputResource);
			var vertexDirection = catan.models.hexgrid.VertexDirection[portJSON.validVertex1.direction]
			var validVertex1 = this.getVertex(vertexDirection);
			this.setValidVertex1(validVertex1);
			vertexDirection = catan.models.hexgrid.VertexDirection[portJSON.validVertex2.direction]
			var validVertex2 = this.getVertex(vertexDirection);
			this.setValidVertex2(validVertex2);
		}

        return CatanHex;

        
    }());
	return Map;

}());


