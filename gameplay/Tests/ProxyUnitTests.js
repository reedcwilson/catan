
module("initialization");
test( "MessageList Print Message", function() {
  var testbest = new catan.models.MessageList();
  testbest.setInfo(JSON.stringify({lines:[{message: "hey", source: "jake"}]}));

  ok(testbest.printList() == "hey", "successfully printed message");

});

module("request");
asyncTest("Sending Chat", function(){
  var prox = new catan.models.Proxy();
  var sender = new catan.models.CommandObject({type: "sendChat",playerIndex:1, content:"hey buddy"});
  prox.send(sender, function(data){
    ok(data.chat.lines[0].message = "hey buddy", "Sent chat successfully");
    start();
  });
});

asyncTest("Building Road", function(){
  var prox = new catan.models.Proxy();
  var sender = new catan.models.CommandObject({type: "buildRoad",playerIndex:1,roadLocation: {x:1,y:1,direction:"NE"},free:true});
  prox.send(sender, function(data){
    ok(data.map, "Built road successfully");
    start();
  });
});

asyncTest("Rolling Number", function(){
  var prox = new catan.models.Proxy();
  var sender = new catan.models.CommandObject({type: "rollNumber",playerIndex:1,number:2});
  prox.send(sender, function(data){
    ok(data.map, "Rolled successfully");
    start();
  });
});

asyncTest("Finishing Turn", function(){
  var prox = new catan.models.Proxy();
  var sender = new catan.models.CommandObject({type: "finishTurn",playerIndex:1});
  prox.send(sender, function(data){
    ok(data.map, "Turn finished successfully");
    start();
  });
});
