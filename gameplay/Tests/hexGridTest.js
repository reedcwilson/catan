test("Test Basic Init", function() {
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
});

test("Test Map", function() {
	var map = new catan.models.Map(model.map.radius);
	for(var x in model.map.hexGrid.hexes) {
		for(var y in model.map.hexGrid.hexes[x]) {
			hexJSON = model.map.hexGrid.hexes[x][y];
			hexPiece = map.hexGrid.getHex(new catan.models.hexgrid.HexLocation(hexJSON.location.x, hexJSON.location.y));
			hexPiece.setInfo(hexJSON);
			ok(hexPiece.getLandType() == hexJSON.landtype, "The land types are the same!");
			ok(hexPiece.getIsLand() == hexJSON.isLand, "The isLand is the same");
		}
	}
	//Load Port Info
	for(var position in model.map.ports){
		portJSON = model.map.ports[position];
		var hex = map.hexGrid.getHex(new catan.models.hexgrid.HexLocation(portJSON.location.x, portJSON.location.y));
		hex.setPortInfo(portJSON);
		ok(hex.getIsPort(), "This Hex is a port");
		ok(hex.getTradeRatio() == portJSON.ratio, "The ratio's are the same");
		ok(hex.getInputResource() == portJSON.inputResource, "The resources are the same");
	}
	console.log(map);
});
