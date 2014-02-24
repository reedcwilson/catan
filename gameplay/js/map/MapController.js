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
						var player = self.loadPersonByIndex(owner);
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
							var player = self.loadPersonByIndex(owner);
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
		}
        
        /**
		 * Starts the robber movement on the map. The map should pop out and the player should be able
         * move the robber.  This is called when the user plays a "solider" development card.
		 * @method doSoldierAction
		 * @return void
		**/		
		MapController.prototype.doSoldierAction = function(){    
			this.getModalView().showModal("Solider");
		}
        
		/**
		 * Pops the map out and prompts the player to place two roads.
         * This is called when the user plays a "road building" progress development card.
		 * @method startDoubleRoadBuilding
		 * @return void
		**/	
		MapController.prototype.startDoubleRoadBuilding = function(){
			this.getModalView().showModal("Road");
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
			console.log("Building: " + pieceType);
			this.getModalView().showModal(pieceType);
		};
        
		/**
		 * This method is called from the modal view when the cancel button is pressed. 
		 * It should allow the user to continue gameplay without having to place a piece. 
		 * @method cancelMove
		 * @return void
		 * */
		MapController.prototype.cancelMove = function(){
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
			return true;
		};

		/**
		 This method is called when the user clicks the mouse to place a piece.
         This method should close the modal and possibly trigger the Rob View.

		 @param {MapLocation} loc The location where the piece is being placed
		 @param {String} type The type of piece being placed ("robber","road","settlement","city")
		 @method onDrop
		*/
		MapController.prototype.onDrop = function (loc, type) {
		};

/**		var MapState = function() {

			return MapState;
		} ());
        */
		return MapController;
	} ());

	return MapController;

} ());

