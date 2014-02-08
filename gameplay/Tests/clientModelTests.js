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

test( "Init Test", function() 
{
  expect(2);

  var client = new catan.models.ClientModel(0);
  ok(client != null, 'Client Model is not null');
  
  ok(client.getPlayerID() == 0, 'PlayerID = 0');
});