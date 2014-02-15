QUnit.config.autostart = false;
var model = {};
$(function () {
  login(function() {
    join(function() {
      getModel(function(data) {
        model = data || model1;
        QUnit.start();
      });
    });
  });
});

function getModel(callback) {
  $.get('/game/model', function(data, status, jqxhr) {
    callback(data);
  }).fail(function(jqxhr, text) {
    console.log("ahhh!!! couldn't get model!");
  });
}

function login(callback) {
  $.post("/user/login", {
    username: "Sam",
    password: "sam"
  }, function(data){
    console.log("User Logged in");
    callback();
  }).fail(function(data){
    console.log("User failed to login");
    callback();
  })
}

function join(callback) {
  $.post("/games/join", {
    color: 'blue',
    id: 0
  }, function(data){
    console.log("You Joined the game");
    callback();
  }).fail(function(data){
    console.log("Failed to join the game");
    callback();
  })
}


module("overhead");
test( "Constructor Test", function() 
{
  expect(2);

  var client = new catan.models.ClientModel(0);
  ok(client != null, 'Client Model is not null');
  ok(client.getClientID() == 0, 'ClientID = 0');
});

test( "InitFromServer Test", function() 
{
  expect(11);

  var client = new catan.models.ClientModel(0);
  client.init(model);
  
  ok(client.getBank().brick == 23, 'Bank has 23 Bricks');		 		
  ok(client.getBiggestArmy() == 2, 'Player 2 has the Biggest Army');
  ok(client.getChat().printList() == '', 'Chat is empty');
  ok(client.getDeck().soldier == 14, 'Deck has 14 Soldiers');
  ok(client.getLog().getLines()[0].getMessage() == 'Sam built a road', 'Log begins with "Sam built a road"');
  ok(client.getLongestRoad() == -1, 'No one has the Longest Road');
  ok(client.getMap().getRadius() == 4, 'Map has a radius of 4');
  ok(client.getTurnTracker().getCurrentTurn() == 0, "It's Player 0's turn");
  ok(client.getWinner() == -1, "There is no winner yet.");
  ok(client.getPlayers()[11].getRoads() == 13, "Player 11 has 13 Roads");
  ok(client.getTradeOffer().getSender() == null, "There is no Trade Offer");

  //console.log(model);	
}); 
