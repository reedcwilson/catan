test("Hello World", function(){
	console.log('meow');
	ok(1 == "1", "Basic Failure");
});

test("Hex Grid", function(){
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
});

test("Change", function(){
	console.log('meow');
	ok(true,"");
});

