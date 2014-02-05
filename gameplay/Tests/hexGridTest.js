jQuery(function () {
	var jqXHR = jQuery.getJSON('../server-REST.json');
	jqXHR.done(doTests);
	jqXHR.fail(function(jqXHR, textStatus) {
		console.log("error " + textStatus);
		console.log("incoming Text " + jqXHR.responseText);
	});
});

function doTests(data)
{	
	test("Basic Test", function(){
		testBasicInit();
	});
	ConnectAsSam();
	
}

function testBasicInit()
{
	var map = new catan.models.Map(2);
	var hexes = map.hexGrid.getHexes();
	for(var hex in hexes)
	{
		hex = hexes[hex];
		for(var edgenum in hex.edges)
		{
			var edge = hex.edges[edgenum]
			ok(!edge.isOccupied(), "Edge: " + edgenum + " was not occupied"); 
		}
		for (var vertexnum in hex.vertexes)
		{
			var vertex = hex.vertexes[vertexnum];
			ok(!vertex.isOccupied(), "Vertex: " + vertexnum + " was not occupied");
			ok(!vertex.getWorth() != 0, "Vertex: " + vertexnum + " has no worth");
		}
		hex.edges[0].setOwnerID(0);
		for(var edgenum in hex.edges)
		{
			var edge = hex.edges[edgenum]
			if(edgenum == 0) {
				ok(edge.isOccupied(), "Edge: 0 was occupied"); 
			}
			else {
				ok(!edge.isOccupied(), "Edge: " + edgenum + " was not occupied"); 
			}
		}
		hex.vertexes[0].setOwnerID(0);
		hex.vertexes[0].setWorth(1);
		for (var vertexnum in hex.vertexes)
		{
			var vertex = hex.vertexes[vertexnum];
			if(vertexnum == 0) {
				ok(vertex.isOccupied(), "Vertex needs to be occupied");
				ok(vertex.getWorth() == 1, "Vertex worth needs to be 1");
			}
			else {
				ok(!vertex.isOccupied(), "Vertex: " + vertexnum + " was not occupied");
				ok(!vertex.getWorth() != 0, "Vertex: " + vertexnum + " has no worth");
			}
		}
	}
}

function ConnectAsSam()
{
	var jqXHR = ajaxConnect("POST", "/user/login", 'username=Sam&password=sam');
	jqXHR.done(function (data) {
		var string = 'color=red&id=0';
		var newjqXHR = ajaxConnect("POST", "/games/join", string);
		newjqXHR.done(function (data){
			loadModel();
		});
	});
}

function ajaxConnect(sendType, sendUrl, sendData, successFunc)
{
	var options = {
		type: sendType,
		url: sendUrl
	};

	// Making the sendData parameter optional
	if (sendData !== undefined) {
		options.data = sendData;
	}

	var jqXHR = jQuery.ajax(options);

	// Making the successFunc parameter optional
	if (successFunc !== undefined){
		jqXHR.done(successFunc);
	}

	jqXHR.fail(function(error) {
		jQuery('#responseBody').html(error.responseText);
	});

	return jqXHR;
}

function loadModel()
{
	var jqXHR = ajaxConnect("GET", "/game/model");
	jqXHR.done(function (data) {
		test("Loaded Map", function() {
			console.log(JSON.stringify(data, null, 2));
			testMap(data);
			testPlayer(data);
		});
	});
}

function testMap(data) {
	var map = new catan.models.Map(data.map.radius);
	//TODO: data.map.numbers
	//TODO: data.map.robber
	//Load the Hex Info
	for(var x in data.map.hexGrid.hexes) {
		for(var y in data.map.hexGrid.hexes[x]) {
			hexJSON = data.map.hexGrid.hexes[x][y];
			hexPiece = map.hexGrid.getHex(new catan.models.hexgrid.HexLocation(hexJSON.location.x, hexJSON.location.y));
			hexPiece.setInfo(hexJSON);
		}
	}
	//Load Port Info
	for(var position in data.map.ports){
		portJSON = data.map.ports[position];
		var hex = map.hexGrid.getHex(new catan.models.hexgrid.HexLocation(portJSON.location.x, portJSON.location.y));
		hex.setPortInfo(portJSON);
	}
	
	console.log(map);
}

//Test Basic Player Loading
function testPlayer(data){
	var player = new catan.models.Player();
	player.setInfo(data.players[0]);
	testResourceList(player);

	//Test DevCards
	var devCardsList = new Array();
	devCardsList["monopoly"] = 0;
	devCardsList["monument"] = 0;
	devCardsList["roadBuilding"] = 0;
	devCardsList["soldier"] = 0;
	devCardsList["yearOfPlenty"] = 1;
	player.setOldDevCards(devCardsList);
	ok(player.canPlayDevCard("yearOfPlenty"), "Can play Year of Plenty");
	ok(!player.canPlayDevCard("monopoly"), "Cannot play monopoly");
	player.playDevCard("yearOfPlenty");
	ok(!player.canPlayDevCard("yearOfPlenty"), "Played Year of Plenty");
	player.addDevCard("soldier");
	ok(player.newDevCards["soldier"] == 1, "Successfully added a devCard");

	console.log(player);
	ok(true,"Loaded the map!");
}

function testResourceList(player) {
	var resourceList = new Array();
	resourceList["brick"] = 0;
	resourceList["ore"] = 0;
	resourceList["sheep"] = 1;
	resourceList["wheat"] = 1;
	resourceList["wood"] = 0;
	ok(player.hasResources(resourceList, "Has resources!"));
	resourceList["wood"] = 7;
	ok(!player.hasResources(resourceList, "Doesn't have resources!"));
	ok(player.getNumOfCards() == 3, "Has 3 Resource Cards");
	ok(!player.needsToDiscard(), "The player shouldn't discard");
	player.setResources(resourceList);
	ok(player.needsToDiscard(), "The player needs to discard. The player has: " + 		player.getNumOfCards());

	//Testing Trade
	var tradeList = new Array();
	tradeList["brick"] = 0;
	tradeList["ore"] = -1;
	tradeList["sheep"] = 1;
	tradeList["wheat"] = 1;
	tradeList["wood"] = 7;
	var tradeOffer = new catan.models.TradeOffer();
	tradeOffer.setOffer(tradeList);
	ok(player.canOfferTrade(tradeOffer), "Can offer all of it's resources");
	ok(!player.canAcceptTrade(tradeOffer), "Cannot accept because he doesn't have a ore");
	tradeList["ore"] = 0;
	tradeList["sheep"] = -1;
	tradeOffer.setOffer(tradeList);
	ok(player.canAcceptTrade(tradeOffer),"Can accept trade");
	tradeList["wood"]= 8;
	tradeOffer.setOffer(tradeList);
	ok(!player.canOfferTrade(tradeOffer),"Cannot offer trade");
}
