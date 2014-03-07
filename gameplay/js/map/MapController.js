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
        core.defineProperty(MapController.prototype,"robLoc");
        core.defineProperty(MapController.prototype,"stealing");
        core.defineProperty(MapController.prototype,"roadBuild");
        core.defineProperty(MapController.prototype,"currentRoads");
        core.defineProperty(MapController.prototype,"spot1");
        core.defineProperty(MapController.prototype,"spot2");
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
			this.setStealing(false);
			view.setController(this);
			this.setRoadBuild("no");
		}
		
		MapController.prototype.initFromModel = function(){
			var map = this.getClientModel().getMap();
			this.loadHexes(map.hexgrid.getHexes());
			this.loadPorts(map.getPorts());
			this.loadNumbers(map.getNumbers());
			this.updateFromModel();
        }

        MapController.prototype.updateFromModel = function(){
			var model = this.getClientModel();
			var view = this.getView();
			var hexes = model.map.hexgrid.getHexes();
			var self = this;
			var playerIndex = model.getPlayerIndex();
			var player = model.loadPersonByIndex(playerIndex);

			view.clearPlaceables();
			view.placeRobber(model.map.getRobber());

			this.checkDoubleRoadBuild();
			this.checkRobbing(model);

			hexes.map(function (hex){
				self.loadRoads(model, view, hex);
				self.loadCitiesAndSettlements(model, view, hex);
			});
        }

        /**
		 This method is used with the double road building card. It checks certain conditions and then executes accordingly.
		 @method checkDoubleRoadBuild
		*/
		MapController.prototype.checkDoubleRoadBuild = function(){
			var model = this.getClientModel();
			if(this.getRoadBuild() == "first")
			{
				this.startMove("road", true, false);
				this.setRoadBuild("second");
			}
			if(this.getRoadBuild() == "second" && this.getSpot1() != undefined && this.getSpot2() == undefined)
			{
				this.startMove("road", true, false);
				this.setRoadBuild("roadBuild");
			}
			if(this.getRoadBuild() == "roadBuild" && this.getSpot1() != undefined && this.getSpot2() != undefined)
			{
				this.setRoadBuild("no");
				this.spot1.direction = this.spot1.getDir();
				this.spot2.direction = this.spot2.getDir();
				model.sendMove({type:"Road_Building",playerIndex:model.getPlayerIndex(),spot1:this.getSpot1(),spot2:this.getSpot2()});
				
			}
		}

		MapController.prototype.checkRobbing = function(model){
			if(model.robbing)
			{
				if(model.turnTracker.status == "Robbing" && model.isCurrentTurn(model.getClientID()) && this.getModalView() != undefined)
				{
					model.setRobbing(false);
					this.setStealing(true);
					this.getModalView().showModal("Robber");
					this.getView().startDrop("robber");
				}
			}
		}

        /**
		 This method is called update from model to place all of the cities and settlements on the map
		 @param {ClientModel} model The client model of the game
		 @param {View} view The view to add the roads to
		 @param {CatanHex} hex The Hex to load the roads from.
		 @method loadCitiesAndSettlements
		*/
		MapController.prototype.loadCitiesAndSettlements = function(model, view, hex){
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
		}

	 	/**
		 This method is called init from model to place all of the hexes on the map
		 @param {List} hexes The Hexes to load onto the map
		 @method loadHexes
		*/
        MapController.prototype.loadHexes = function(hexes){
			var view = this.getView();
			hexes.map(function (hex) {
				var hexType = hex.getLandType();
				hexType = hexType.toLowerCase();
				view.addHex(hex.getLocation(), hexType);
			});
        }

        /**
		 This method is called init from model to place all of the numbers on the map
		 @param {List} numbers The list of numbers to load onto the hexes
		 @method loadNumbers
		*/
        MapController.prototype.loadNumbers = function(numbers){
        	var view = this.getView();
			for(var number in numbers) {
				numbers[number].map(function (newNumber) {
					view.addNumber(newNumber, number, true);
				});
			}
        }

        /**
		 This method is called init from model to place all of the ports on the map
		 @param {List} ports The list of ports to load
		 @method loadPorts
		*/
        MapController.prototype.loadPorts = function(ports){
        	var view = this.getView();
			ports.map(function (port) {
				var inputResource = port.inputResource;
				if(inputResource == undefined) {
					inputResource = "three";
				}
				inputResource = inputResource.toLowerCase();
				var portLoc = new catan.map.View.PortLoc(port.location.x,port.location.y,port.validVertex1.location.getDirection());
				view.addPort(portLoc,inputResource, true);
			});
        }

        /**
		 This method is called update from model to place all of the roads on the map
		 @param {ClientModel} model The client model of the game
		 @param {View} view The view to add the roads to
		 @param {CatanHex} hex The Hex to load the roads from.
		 @method loadRoads
		*/
       MapController.prototype.loadRoads = function(model, view, hex){
			hex.edges.map(function (edge) {
				var owner = edge.ownerID;
				if(owner != -1) {
					var player = model.loadPersonByIndex(owner);
					var dir = edge.location.getDir();
					if(dir === "S" || dir === "SE" || dir === "SW"){
						view.placeRoad(edge.location,player.color,false);
					}
				}
			});
		}
        
        /**
		 This method is called by the Rob View when a player to rob is selected via a button click.
		 @param {Integer} orderID The index (0-3) of the player who is to be robbed
		 @method robPlayer
		*/
		MapController.prototype.robPlayer = function(orderID){
			var model = this.getClientModel();
			var clientIndex = model.getPlayerIndex();
			if(this.getStealing() == true) 
			{
				this.setStealing(false);
				model.sendMove({type:"robPlayer",playerIndex:clientIndex,victimIndex:orderID,location:this.robLoc});
			}
			else 
			{
				model.sendMove({type:"Soldier",playerIndex:clientIndex,victimIndex:orderID,location:this.robLoc});
			}
			this.getRobView().closeModal();
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
			//this.startMove("road", true, false);
			this.setRoadBuild("first");
			var model = this.getClientModel();
			var playerIndex = model.getPlayerIndex();
			var player = model.loadPersonByIndex(playerIndex);
			this.setCurrentRoads(parseInt(player.roads));
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
			var playerIndex = model.getPlayerIndex();
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
            var id = clientModel.getPlayerIndex();
			if(hex) {
				if(type.type == "road") {
					var edge = hex.edges[this.getIndexOfEdge(loc.dir)];
                    if (clientModel.turnTracker.status == "FirstRound" || clientModel.turnTracker.status == "SecondRound") {
                      return clientModel.setupCanPlaceRoad(edge, id);
                    }
                    else {
                      return clientModel.canPlaceRoad(edge, id);
                    }
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

   	 	var edLookup = ["NW","N","NE","SE","S","SW"]
    	var vdLookup = ["W","NW","NE","E","SE","SW"]
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
			var model = this.getClientModel();
			var isfree = model.turnTracker.status == "FirstRound" || model.turnTracker.status == "SecondRound";
			if(type.type == "road" && (this.getRoadBuild() == "second" || this.getRoadBuild() == "roadBuild"))
			{	
				if(this.getSpot1() == undefined)
				{
					this.setSpot1(loc);
				}
				else
				{
					this.setSpot2(loc);
				}
			}
			else if(type.type == "road") {
				this.dropObject(loc, "Road", isfree);
			}
			else if(type.type == "settlement"){
				this.dropObject(loc, "Settlement", isfree);
				if(isfree) {
					model.sendMove({type:"finishTurn",playerIndex:model.getPlayerIndex()});
					for(var player in model.players)
            		{
						model.players[player].startedRoad = false;
						model.players[player].startedSettlement = false;
						player.startedRoad = false;
						player.startedSettlement= false;
           			}
				}
			}
			else if(type.type == "city") {
				this.dropObject(loc, "City", false);
			}
			else if(type.type == "robber") {
				this.dropRobber(loc);
			}
			this.getModalView().closeModal();
		};

		MapController.prototype.dropObject = function(loc, type, isfree){
			loc.direction = loc.dir;
			var command = new Object;
			command.type = "build"+type;
			command.playerIndex = this.getClientModel().getPlayerIndex();
			command.vertexLocation = loc;
			command.roadLocation = loc;
			command.free = isfree;
			this.getClientModel().sendMove(command);
		}

		MapController.prototype.dropRobber = function(loc){
			var model = this.getClientModel();
			var hex = model.getMap().hexgrid.getHex(loc);
			var playersToRob = new Array();
			for(var vertexLoc in hex.vertexes)
			{
				var playerToAdd;
				var playerInfo = new Array();;
				if(hex.vertexes[vertexLoc].ownerID != -1)
				{
					playerToAdd = model.loadPersonByIndex(hex.vertexes[vertexLoc].ownerID);
					if(playerToAdd !== model.loadPersonByIndex(model.getPlayerIndex())) {
						playerInfo.color = playerToAdd.color;
						playerInfo.name = playerToAdd.name;
						playerInfo.playerNum = hex.vertexes[vertexLoc].ownerID;
						playerInfo.cards = playerToAdd.getNumOfCards();
						var exsists = false;
						for(var item in playersToRob)
						{
							if(playersToRob[item].name == playerInfo.name){
								exsists = true;
							}
						}
						if((playersToRob.count == 0 || !exsists) && playerInfo.cards > 0)
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
			this.setRobLoc(hex.location);
		}

		return MapController;
	} ());

	return MapController;

} ());

