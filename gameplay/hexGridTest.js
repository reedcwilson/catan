jQuery(function () {
	var jqXHR = jQuery.getJSON('server-REST.json');
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
		hex.edges[0].setOwner(0);
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
		hex.vertexes[0].setOwner(0);
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

	// returning the jqXHR promise in case we want to add any other promise callbacks
	return jqXHR;
}

function loadMap()
{
	var jqXHR = ajaxConnect("GET", "/game/model");
	jqXHR.done(function (data) {
		test("Loaded Map", function() {
			var map = new catan.models.Map(data.map.radius);
			ok(true,"Loaded the map!");
		});
	});
}
