//STUDENT-EDITABLE-BEGIN   
/**
	This module contains the map
	
	@module	catan.models
	@namespace models
*/

var catan = catan || {};
catan.models = catan.models || {};

catan.models.Map = (function mapNameSpace(){
    
    var hexgrid = catan.models.hexgrid;
    /**
     * This represents the catan map
     *
     * @class Map
     * @constructor
     * @param {number} radius the radius of the map
     */
    var Map = function (radius){
      this.hexgrid = hexgrid.HexGrid.getRegular(radius, CatanHex);
    };

    /**
     * Initializes all data for the map
     *
     * <pre>
     *    PRE: json input is valid
     *    POST: state of the map represents the model on the server
     * </pre>
     *
     * @method setInfo
     * @param {JSON} json the model
     */
    Map.prototype.setInfo = function(json) {

      this.setRadius(json.radius);
      this.loadHexes(json);
      this.loadPorts(json);
      this.loadRobber(json);
	  this.loadNumbers(json);
    };

	/**
	 Loads the Hexes information into the model based on the JSON
	 @param {json} json The complete JSON given to the map.
	 @method loadHexes
	*/
	Map.prototype.loadHexes = function(json) {
      for(var x in json.hexGrid.hexes) {
        for(var y in json.hexGrid.hexes[x]) {
          var hexJson = json.hexGrid.hexes[x][y];

          var hexPiece = this.hexgrid.getHex(
              new catan.models.hexgrid.HexLocation(
                hexJson.location.x, hexJson.location.y));

          hexPiece.setInfo(hexJson);
        }
      }
	}

	/**
	 Loads the ports information into the model based on the JSON
	 @param {json} json The complete JSON given to the map.
	 @method loadPorts
	*/
	Map.prototype.loadPorts = function(json) {
      var ports = [];
      for(var pos in json.ports){
        portJson = json.ports[pos];

        var hex = this.hexgrid.getHex(
            new catan.models.hexgrid.HexLocation(
              portJson.location.x, portJson.location.y));

        hex.setPortInfo(portJson);
        ports[pos] = hex;
      }
      this.setPorts(ports);
	}
	
	/**
	 Loads the number information into the model based on the JSON
	 @param {json} json The complete JSON given to the map.
	 @method loadNumbers
	*/
	Map.prototype.loadNumbers = function(json) {
      var numbers = [];
      var i = 0;
      for (var num in json.numbers) {
        for (var pos in json.numbers[num]) {
          var numJson = json.numbers[num][pos];
          var loc = new catan.models.hexgrid.HexLocation(numJson.x, numJson.y);
          var hexPiece = this.hexgrid.getHex(loc);


          if (hexPiece) {
            hexPiece.setRollNumber(num);
          }
          numbers[num] = json.numbers[num]; 
        }
      }
      this.setNumbers(numbers);
	}

	/**
	 Loads the robbber information into the model based on the JSON
	 @param {json} json The complete JSON given to the map.
	 @method loadRobber
	*/
    Map.prototype.loadRobber = function(json) {
      var robberJson = json.robber;
      var robber = new catan.models.hexgrid.HexLocation(
          robberJson.x, robberJson.y);
      var robberHex = this.hexgrid.getHex(robber);
      if (robberHex) {
        robberHex.setHasRobber(true);
      }
      this.setRobber(robber);
    }

    // I am going to assume that the 'canPlace' methods for the map will only
    // need to worry about the map aspect of placing these objects and not
    // about resources

    /**
     * Test to see if a road can be placed
     *
     * <pre>
     *    PRE: it is the player's turn
     *    PRE: a valid edge is given
     *    POST: validity of placement is enforced
     * </pre>
     *
     * @method canPlaceRoad
     * @param {edge} edge the edge in question
     * @return {boolean} returns true if road can be placed
     */
    Map.prototype.canPlaceRoad = function(edge, id) {
      var ownerId = false;
      var connectedEdge = false;
      var land = false;

      // check current edge
      if (edge.isOccupied() == true) {
        return false;
      }

      var vertices = edge.location.getConnected();
      for (var key in vertices) {
        var v = vertices[key];

        // get 3 associated hexes if one is land then valid
        var equivVerts = v.getEquivalenceGroup();
        for (var vKey in equivVerts) {
          var eV = equivVerts[vKey];
          var vHex = this.hexgrid.getHex(
            new catan.models.hexgrid.HexLocation(eV.x, eV.y));
 
          if (vHex.getIsLand()) {
            land = true;
            break;
          }
        }

        // check vertices for settlements
        var hex = this.hexgrid.getHex(
            new catan.models.hexgrid.HexLocation(v.x, v.y));

        if (hex.vertexes[v.direction].ownerID == id) {
          ownerId = true;
        }
      }

      // check the four edges connected to current edge
      var edges = edge.location.getConnectedEdges();
      for (var key in edges) {
        var e = edges[key];
        var hex = this.hexgrid.getHex(
            new catan.models.hexgrid.HexLocation(e.x, e.y));
        if(hex) {
        	if (hex.edges[e.direction].ownerID == id) {
         	 connectedEdge = true;
          	break;
        	}
        }
      }

      return land && (ownerId == true || connectedEdge == true);
    };

    Map.prototype.setupCanPlaceRoad = function(edge, id) {
      var land = false;
      var validRoad = false;

      // check current edge
      if (edge.isOccupied() == true) {
        return false;
      }

      var vertices = edge.location.getConnected();
      for (var key in vertices) {
        var v = vertices[key];

        // get 3 associated hexes if one is land then valid
        var equivVerts = v.getEquivalenceGroup();
        for (var vKey in equivVerts) {
          var eV = equivVerts[vKey];
          var vHex = this.hexgrid.getHex(
            new catan.models.hexgrid.HexLocation(eV.x, eV.y));

          if (this.setupCanPlaceSettlement(vHex.vertexes[eV.direction], id)) {
            validRoad = true;
          }
 
          if (vHex.getIsLand()) {
            land = true;
            break;
          }
        }
      }

      return land && validRoad;
    };

    /**
     * Test to see if a settlement can be placed
     *
     * <pre>
     *    PRE: it is the player's turn
     *    PRE: a valid location is given
     *    POST: validity of placement is enforced
     * </pre>
     *
     * @method canPlaceSettlement
     * @param {CatanVertex} vertex the vertex in question
     * @param {number} playerId the id of the player wishing to place
     * @return {boolean} returns true if settlement can be placed
     */
    Map.prototype.canPlaceSettlement = function(loc, id) {
      if (loc.getOwnerID() != -1)
        return false;

      // if there is no settlement within two vertices
      var edges = loc.location.getConnectedEdges();
      var occupied = false;
      for (var eKey in edges) {

        var edge = edges[eKey];
        var originHex = this.hexgrid.getHex(
            new catan.models.hexgrid.HexLocation(edge.x, edge.y));

        if (originHex && originHex.edges[edge.direction].ownerID == id) {
          occupied = true;
        }
        var vertexes = edge.getConnected();
        for (var vKey in vertexes) {
          var vertex = vertexes[vKey];

          var hex = this.hexgrid.getHex(
              new catan.models.hexgrid.HexLocation(vertex.x, vertex.y));

          if(hex && hex.vertexes[vertex.direction].isOccupied() == true) {
              return false;
          }
        }
      }
      return occupied;
    };

    Map.prototype.setupCanPlaceSettlement = function(loc, id) {
      if (loc.getOwnerID() != -1)
        return false;

      // if there is no settlement within two vertices
      var edges = loc.location.getConnectedEdges();
      var occupied = false;
      for (var eKey in edges) {

        var edge = edges[eKey];
        var vertexes = edge.getConnected();
        for (var vKey in vertexes) {
          var vertex = vertexes[vKey];

          var hex = this.hexgrid.getHex(
              new catan.models.hexgrid.HexLocation(vertex.x, vertex.y));

          if(hex && hex.vertexes[vertex.direction].isOccupied() == true) {
              return false;
          }
        }
      }
      return true;// occupied;
    };

    /**
     * Tests to see if the robber can be placed on given hex
     *
     * <pre>
     *    INVARIANT: robber is valid
     *    PRE: hex is valid
     *    POST: all properties are the same
     *    POST: validity of placement is enforced
     * </pre>
     *
     * @method canPlaceRobber
     * @param {CatanHex} hex the hex in question
     * @return {boolean} returns true if robber can be placed
     */
    Map.prototype.canPlaceRobber = function(hex) {
      if ((this.robber.x == hex.location.x && this.robber.y == hex.location.y) || !hex.getIsLand())
        return false;
      return true;
    };

    /**
     * Returns best trade ratio for given resource per given playerId
     *
     * <pre>
     *    PRE: resource is valid
     *    PRE: playerId is valid
     *    POST: all properties are the same
     *    POST: best ratio is given
     * </pre>
     *
     * @method getBestRatio 
     * @param {string} resource the resource in question
     * @return {number} returns the best numeric ratio
     */
    Map.prototype.getBestRatio = function(resource, playerID) 
	{
      var bestRatio = 4;
      for (var pKey in this.ports) 
	  {
        var port = this.ports[pKey];				
        if (this.ownsPort(port.validVertex1.location, playerID) || this.ownsPort(port.validVertex2.location, playerID)) 
		{
          if (port.tradeRatio == 3)
            bestRatio = 3;
          else if (port.inputResource.toLowerCase() == resource.toLowerCase())
		    return 2;          
        }
      }
      return bestRatio;
    }	
	
	/**
     * Test to see if a port vertex is owned by the player
     *
     * <pre>
     *    PRE: it is the player's turn
     *    PRE: a valid location is given
     *    POST: validity of placement is enforced
     * </pre>
     *
     * @method ownsPort
     * @param {CatanVertex} vertex the port vertex in question
     * @param {number} playerId the id of the player in question
     * @return {boolean} returns true if the port vertex is owned by the player
     */
	Map.prototype.ownsPort = function(loc, id)
	{
		var hex = this.hexgrid.getHex(
          new catan.models.hexgrid.HexLocation(loc.x, loc.y));	  
      return hex.vertexes[loc.direction].ownerID == id;
	}

    /**
     * Test to see if a city can be placed
     *
     * <pre>
     *    PRE: it is the player's turn
     *    PRE: a valid location is given
     *    POST: validity of placement is enforced
     * </pre>
     *
     * @method canPlaceCity
     * @param {CatanVertex} vertex the vertex in question
     * @param {number} playerId the id of the player wishing to place
     * @return {boolean} returns true if city can be placed
     */
    Map.prototype.canPlaceCity = function(loc, id) {
      var hex = this.hexgrid.getHex(
          new catan.models.hexgrid.HexLocation(loc.location.x, loc.location.y));
      return hex.vertexes[loc.location.direction].ownerID == id;
    };

    /**
     * @property hexGrid
     * @type hexGrid
     */
    core.defineProperty(Map.prototype, "hexGrid");
    /**
     * @property numbers
     * @type hexLocation[]
     */
    core.defineProperty(Map.prototype, "numbers");
    /**
     * @property ports
     * @type CatanHex[]
     */
    core.defineProperty(Map.prototype, "ports");
    /**
     * @property radius
     * @type number
     */
    core.defineProperty(Map.prototype, "radius");
    /**
     * @property robber
     * @type hexLocation
     */
    core.defineProperty(Map.prototype, "robber");
    

    /**
	This class represent an edge. It's a container.
    The data on this class (that you get from the JSON model) is independent of the hexgrid, except for the location.
    Therefore, we leave it up to you to decide how to implement it.
    It must however implement one function that the hexgrid looks for 'isOccupied' - look at its documentation.
    From the JSON, the object will have 2 pieces of data: location, and ownerID.
    Besides the 'isOccupied' method, any other methods you add will be for your personal use (probably one or two)
    
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
    var CatanHex = (function CatanHex_Class() {
    
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
				if(hexJSON.landtype == undefined) {
					this.setLandType('desert');
				}
				else {
					this.setLandType(hexJSON.landtype);
				}
				this.setIsPort(false);
			}
			else {
				this.setLandType('water');
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
				vertex.setLocation(new catan.models.hexgrid.VertexLocation(this.getLocation(), parseInt(position)));
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
				edge.setLocation(new catan.models.hexgrid.EdgeLocation(this.getLocation(),parseInt(position)));
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


