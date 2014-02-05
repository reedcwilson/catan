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
			loadMap();
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
	if (sendData !== undefined)
	{
		options.data = sendData;
	}

	var jqXHR = jQuery.ajax(options);

	// Making the successFunc parameter optional
	if (successFunc !== undefined)
	{
		jqXHR.done(successFunc);
	}

	jqXHR.fail(function(error) {
		jQuery('#responseBody').html(error.responseText);
	});

	return jqXHR;
}

function loadMap()
{
	var jqXHR = ajaxConnect("GET", "/game/model");
	jqXHR.done(function (data) {
		test("Loaded Map", function() {
			var map = new catan.models.Map(data.map.radius);
			//data.map.numbers
			//data.map.robber
			for(var x in data.map.hexGrid.hexes) {
				for(var y in data.map.hexGrid.hexes[x]) {
					//console.log(JSON.stringify(data.map, null, 2));
					hexJSON = data.map.hexGrid.hexes[x][y];
					hexPiece = map.hexGrid.getHex(new catan.models.hexgrid.HexLocation(hexJSON.location.x, hexJSON.location.y));
					hexPiece.setInfo(hexJSON);
				}
			}
			//console.log(JSON.stringify(data.map.ports, null, 2));
			for(var position in data.map.ports){
				portJSON = data.map.ports[0];
				var hex = map.hexGrid.getHex(new catan.models.hexgrid.HexLocation(portJSON.location.x, portJSON.location.y));
				hex.setPortInfo(portJSON);
			}
			console.log(JSON.stringify(data, null, 2));
			var player = new catan.models.Player();
			player.setInfo(data.players[0]);
			var resourceList = new Array();
			resourceList["brick"] = 0;
			resourceList["ore"] = 0;
			resourceList["sheep"] = 1;
			resourceList["wheat"] = 1;
			resourceList["wood"] = 0;
			ok(player.hasResources(resourceList, "Has resources!"));
			resourceList["wood"] = 5;
			ok(!player.hasResources(resourceList, "Doesn't have resources!"));
			ok(player.getNumOfCards() == 3, "Has 3 Resource Cards");
			console.log(player);
			console.log(map);
			ok(true,"Loaded the map!");
		});
	});
}
