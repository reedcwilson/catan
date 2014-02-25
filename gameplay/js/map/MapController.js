//STUDENT-EDITABLE-BEGIN
/**
	This this contains interfaces used by the map and robber views
	@module catan.map
	@namespace map
*/

var catan = catan || {};
catan.map = catan.map || {};

catan.map.Controller = (function catan_controller_namespace() {
	
    var EdgeLoc = catan.map.View.EdgeLoc;
	var VertexLoc = catan.map.View.VertexLoc;
	var PortLoc = catan.map.View.PortLoc;
    
	var HexLocation = catan.models.hexgrid.HexLocation;
	var VertexLocation = catan.models.hexgrid.VertexLocation;
	var EdgeLocation= catan.models.hexgrid.EdgeLocation;
	var VertexDirection = catan.models.hexgrid.VertexDirection;
	var EdgeDirection= catan.models.hexgrid.EdgeDirection;   

	var MapController = (function main_controller_class() {
    
 		core.forceClassInherit(MapController,catan.core.BaseController);
        
		core.defineProperty(MapController.prototype,"robView");
		core.defineProperty(MapController.prototype,"modalView");
        core.defineProperty(MapController.prototype,"printed");
        /**
		 * @class MapController
		 * @constructor
		 * @param {MapView} view - The initialized map view
		 * @param {MapOverlay} modalView - The overlay to use for placing items on the board.
		 * @param {ClientModel} model - The client model
		 * @param {RobberOverlay} robView - The robber overlay to be used when the robber is being placed.  This is undefined for the setup round.
		 */
		function MapController(view, modalView, model, robView){
			catan.core.BaseController.call(this,view,model);
			this.setModalView(modalView);
			this.setRobView(robView);
			view.setController(this);
			this.setPrinted(false);
		}
		MapController.prototype.initFromModel = function(){
			view = this.getView();
			var map = this.getClientModel().getMap();
			var hexes = map.hexgrid.getHexes();
			var ports = map.getPorts();
			var numbers = map.getNumbers();
			//Load Hexes
			hexes.map(function (hex) {
				var hexType = hex.getLandType();
				hexType = hexType.toLowerCase();
				view.addHex(hex.getLocation(), hexType);
			});
			//Load Ports
			ports.map(function (port) {
				var inputResource = port.inputResource;
				if(inputResource == undefined) {
					inputResource = "three";
				}
				inputResource = inputResource.toLowerCase();
				var portLoc = new catan.map.View.PortLoc(port.location.x,port.location.y,port.validVertex1.location.getDirection());
				view.addPort(portLoc,inputResource, true);
			});
			//Load Numbers
			for(var number in numbers) {
				numbers[number].map(function (newNumber) {
					view.addNumber(newNumber, number, true);
				});
			}
			this.updateFromModel();
        }

        MapController.prototype.updateFromModel = function(){
			var model = this.getClientModel();
			var view = this.getView();
			view.placeRobber(model.map.getRobber());
			var hexes = model.map.hexgrid.getHexes();
			var self = this;
			hexes.map(function (hex){
				//load roads
				hex.edges.map(function (edge) {
					var owner = edge.ownerID;
					if(owner != -1) {
						var player = model.loadPersonByIndex(owner);
						var dir = edge.location.getDir();
						var noDraw = dir !== "S" && dir !== "SE" && dir !== "SW";
						if(!noDraw){
							view.placeRoad(edge.location,player.color,noDraw);
						}
					}
				});
				//load cities and settlements
				hex.vertexes.map(function (vertex){
					var owner = vertex.ownerID;
					if(owner != -1){
						if(vertex.location.getDir() === "W" || vertex.location.getDir() === "E")
						{
							var player = model.loadPersonByIndex(owner);
							vertex.location.equals = VertexLoc.prototype.equals;
							if(vertex.worth == 1){
								view.placeSettlement(vertex.location,player.color,false);
							}
							else if(vertex.worth == 2){
								view.placeCity(vertex.location,player.color,false);
							}
						}
					}
				});
			});
			
			if(model.getTurnTracker().status == "Robbing"){
				if(this.getModalView() && this.getPrinted() == false)
				{
					this.setPrinted(true);
					//this.getModalView().showModal("Robber");
				}
			}
        }

        /**
		 This method is called by the Rob View when a player to rob is selected via a button click.
		 @param {Integer} orderID The index (0-3) of the player who is to be robbed
		 @method robPlayer
		*/
		MapController.prototype.robPlayer = function(orderID){
			var model = this.getClientModel();
			var clientIndex = model.loadIndexByClientID(model.clientID);
			model.sendMove({type:"Soldier",playerIndex:clientIndex,victimIndex:orderID,robberSpot:"2"});
			this.robView().closeModal();
		}
        
        /**
		 * Starts the robber movement on the map. The map should pop out and the player should be able
         * move the robber.  This is called when the user plays a "solider" development card.
		 * @method doSoldierAction
		 * @return void
		**/		
		MapController.prototype.doSoldierAction = function(){    
			this.getModalView().showModal("Solider");
			this.getView().startDrop("robber");

		}
        
		/**
		 * Pops the map out and prompts the player to place two roads.
         * This is called when the user plays a "road building" progress development card.
		 * @method startDoubleRoadBuilding
		 * @return void
		**/	
		MapController.prototype.startDoubleRoadBuilding = function(){
			this.getModalView().showModal("Road");
			this.startMove("road", true, false);
		}
		
        
        /**
		 * Pops the map out and prompts the player to place the appropriate piece
         * @param {String} pieceType - "road", "settlement", or "city
         * @param {boolean} free - Set to true in road building and the initial setup
         * @param {boolean} disconnected - Whether or not the piece can be disconnected. Set to true only in initial setup
		 * @method startMove
		 * @return void
		**/	
		MapController.prototype.startMove = function (pieceType,free,disconnected){
			var model = this.getClientModel();
			this.getModalView().showModal(pieceType);
			console.log(model);
			var playerIndex = model.loadIndexByClientID(model.getClientID())
			var color = model.loadPersonByIndex(playerIndex).color;
			this.getView().startDrop(pieceType, color);
		};
        
		/**
		 * This method is called from the modal view when the cancel button is pressed. 
		 * It should allow the user to continue gameplay without having to place a piece. 
		 * @method cancelMove
		 * @return void
		 * */
		MapController.prototype.cancelMove = function(){
			this.getView().cancelDrop();
		}

		/**
		 This method is called whenever the user is trying to place a piece on the map. 
         It is called by the view for each "mouse move" event.  
         The returned value tells the view whether or not to allow the piece to be "dropped" at the current location.

		 @param {MapLocation} loc The location being considered for piece placement
		 @param {String} type The type of piece the player is trying to place ("robber","road","settlement","city")
		 @method onDrag
		 @return {boolean} Whether or not the given piece can be placed at the current location.
		*/
		MapController.prototype.onDrag = function (loc, type) {
			loc.getX = function() {return loc.x};
			loc.getY = function() {return loc.y};
			var clientModel = this.getClientModel();
			var hex = clientModel.getMap().hexgrid.getHex(loc);
            var id = clientModel.loadIndexByClientID(clientModel.clientID)
			if(hex) {
				if(type.type == "road") {
					var edge = hex.edges[this.getIndexOfEdge(loc.dir)];
					console.log('it"s a road');
					return clientModel.canPlaceRoad(edge, id);
				}
				if(type.type == "settlement")
				{
					var vertex = hex.vertexes[this.getIndexOfVertex(loc.dir)];
					return clientModel.canPlaceSettlement(vertex, id);
				}
				if(type.type == "city")
				{
					var vertex = hex.vertexes[this.getIndexOfVertex(loc.dir)];
					return clientModel.canPlaceCity(vertex, id);
				}
				if(type.type == "robber")
				{
					return clientModel.canPlaceRobber(hex);
				}
				return true;
			}
		};

		MapController.prototype.getIndexOfVertex = function(dir) {
			var index = -1;
			for (var val in vdLookup)
			{
				index++;
				if(dir == vdLookup[val]) {
					return val;
				}
			}
			return -1;
		}

		MapController.prototype.getIndexOfEdge = function(dir) {
			var index = -1;
			for(var val in edLookup)
			{
				index++;
				if(dir == edLookup[val]) {
					return val;
				}
			}
			return -1;
		}

    var edLookup = ["NW","N","NE","SE","S","SW"]
	var EdgeDirection = core.numberEnumeration(edLookup);

    var vdLookup = ["W","NW","NE","E","SE","SW"]
	var VertexDirection = core.numberEnumeration(vdLookup);
		/**
		 This method is called when the user clicks the mouse to place a piece.
         This method should close the modal and possibly trigger the Rob View.

		 @param {MapLocation} loc The location where the piece is being placed
		 @param {String} type The type of piece being placed ("robber","road","settlement","city")
		 @method onDrop
		*/
		MapController.prototype.onDrop = function (loc, type) {
			loc.getX = function() {return loc.x};
			loc.getY = function() {return loc.y};
			console.log(type.type);
			var model = this.getClientModel();
			var isfree = model.turnTracker.status == "FirstRound" || model.turnTracker.status == "SecondRound";
			if(type.type == "road") {
				loc.direction = loc.dir;
				model.sendMove({type:"buildRoad",playerIndex:model.loadIndexByClientID(model.clientID),roadLocation:loc,free:isfree});
			}
			if(type.type == "settlement"){
				console.log(loc);
				loc.direction = loc.dir;
				model.sendMove({type:"buildSettlement",playerIndex:model.loadIndexByClientID(model.clientID),vertexLocation:loc,free:isfree});
			}
			if(type.type == "city") {
				loc.direction = loc.dir;
				model.sendMove({type:"buildCity",playerIndex:model.loadIndexByClientID(model.clientID),vertexLocation:loc,free:false});
			}
			if(type.type == "robber") {
				var hex = model.getMap().hexgrid.getHex(loc);
				var playersToRob = new Array();
				for(var vertexLoc in hex.vertexes)
				{
					var playerToAdd;
					var playerInfo = new Array();;
					if(hex.vertexes[vertexLoc].ownerID != -1)
					{
						playerToAdd = model.loadPersonByIndex(hex.vertexes[vertexLoc].ownerID);
						if(playerToAdd !== model.loadPersonByIndex(model.loadIndexByClientID(model.clientID))) {
							playerInfo.color = playerToAdd.color;
							playerInfo.name = playerToAdd.name;
							playerInfo.playerNum = vertexLoc;
							playerInfo.cards = playerToAdd.getNumOfCards();
							var exsists = false;
							for(var item in playersToRob)
							{
								if(playersToRob[item].name == playerInfo.name){
									exsists = true;
								}
							}
							if(playersToRob.count == 0 || !exsists)
							{
								playersToRob.push(playerInfo);
							}
						}
					}
				}
				if(playersToRob.count != 0) {
					this.getRobView().setPlayerInfo(playersToRob);
					this.getRobView().showModal(playersToRob);
				}
				console.log(hex);
				
			}
			this.getModalView().closeModal();
		};

/**		var MapState = function() {

			return MapState;
		} ());
        */
		return MapController;
	} ());

	return MapController;

} ());

