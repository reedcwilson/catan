
QUnit.config.autostart = false;
var model = {};
$(function () {
  getModel(function(data) {
    model = data || model1;
    QUnit.start();
  });
});

module("initialization");
test( "Robber Test", function() {
  expect(2);

  var map = new catan.models.Map(model.map.radius);
  ok(map != null, 'Map is not null');
  map.setInfo(model.map);

  ok(map.hexgrid.getHex(map.getRobber()).hasRobber == true, 
    'Robber hex says it has the robber');
});

test( "Numbers Test", function() {
  expect(5);

  var map = new catan.models.Map(model.map.radius);
  map.setInfo(model.map);

  var nums = map.getNumbers();
  var hex1 = map.hexgrid.getHex(
    new catan.models.hexgrid.HexLocation(nums[2][0].x, nums[2][0].y));
  ok(hex1.rollNumber == 2, "numbers are in the rights hexes");

  var hex2 = map.hexgrid.getHex(
    new catan.models.hexgrid.HexLocation(nums[4][0].x, nums[4][0].y));
  ok(hex2.rollNumber == 4, "numbers are in the rights hexes");

  var hex3 = map.hexgrid.getHex(
    new catan.models.hexgrid.HexLocation(nums[5][1].x, nums[5][1].y));
  ok(hex3.rollNumber == 5, "numbers are in the rights hexes");

  var hex4 = map.hexgrid.getHex(
    new catan.models.hexgrid.HexLocation(nums[6][0].x, nums[6][0].y));
  ok(hex4.rollNumber == 6, "numbers are in the rights hexes");

  var hex5 = map.hexgrid.getHex(
    new catan.models.hexgrid.HexLocation(nums[8][1].x, nums[8][1].y));
  ok(hex5.rollNumber == 8, "numbers are in the rights hexes");
});

test( "Ports Test", function() {
  expect(3);

  var map = new catan.models.Map(model.map.radius);
  map.setInfo(model.map);

  var port1Json = model.map.ports[1];
  var portLoc = new catan.models.hexgrid.HexLocation(
    port1Json.location.x, port1Json.location.y);
  ok(map.hexgrid.getHex(portLoc).isPort == true, 
    "port is in the correct location");

  var port2Json = model.map.ports[3];
  var portLoc = new catan.models.hexgrid.HexLocation(
    port2Json.location.x, port2Json.location.y);
  ok(map.hexgrid.getHex(portLoc).isPort == true, 
    "port is in the correct location");

  var port3Json = model.map.ports[5];
  var portLoc = new catan.models.hexgrid.HexLocation(
    port3Json.location.x, port3Json.location.y);
  ok(map.hexgrid.getHex(portLoc).isPort == true, 
    "port is in the correct location");
});

module("query");
test( "Can Place Road", function() {
  expect(2);

  var map = new catan.models.Map(model.map.radius);
  map.setInfo(model.map);

  var canPlace = false;
  var loc = new catan.models.hexgrid.HexLocation(0,0);

  // set adjacent edge so that user can place road
  var adjacentEdge = map.hexgrid.hexes[3][4].edges[5];
  adjacentEdge.ownerID = 1;

  var edge = map.hexgrid.hexes[3][3].edges[3];

  // test empty edge
  canPlace = map.canPlaceRoad(edge, 1);
  ok(canPlace == true, "user can place road on designated edge");

  // set edge to occupied
  edge.ownerID = 1;

  // test again to see if it changed correctly
  canPlace = map.canPlaceRoad(edge, 1);
  ok(canPlace == false, "user can no longer place road");
});

test( "Can Place Settlement", function() {
  expect(2);

  var map = new catan.models.Map(model.map.radius);
  map.setInfo(model.map);

  var canPlace = false;
  var adjacentVertex = map.hexgrid.hexes[3][3].vertexes[4];
  var vertex = map.hexgrid.hexes[3][3].vertexes[3];

  // test empty vertex 
  canPlace = map.canPlaceSettlement(vertex, 1);
  ok(canPlace == true, "user can place settlement on designated vertex");

  // set adjacent vertex to occupied
  adjacentVertex.ownerID = 1;

  // test again to see if it changed correctly
  canPlace = map.canPlaceSettlement(vertex, 1);
  ok(canPlace == false, "user can no longer place settlement");
});

test( "Can Place City", function() {
  expect(2);

  var map = new catan.models.Map(model.map.radius);
  map.setInfo(model.map);

  var canPlace = false;
  var vertex = map.hexgrid.hexes[3][3].vertexes[3];

  // test empty vertex 
  canPlace = map.canPlaceCity(vertex, 1);
  ok(canPlace == false, "user cannot place city on empty vertex");

  // set vertex to occupied
  vertex.ownerID = 1;

  // test again to see if it changed correctly
  canPlace = map.canPlaceCity(vertex, 1);
  ok(canPlace == true, "user can now place city on settlement");
});

function getModel(callback) {
  $.get('/game/model', function(data, status, jqxhr) {
    callback(data);
  }).fail(function(jqxhr, text) {
    console.log("ahhh!!! couldn't get model!");
  });
}

// check query functions
