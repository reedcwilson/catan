
module("overhead");
test( "Init Test", function() 
{
  expect(2);

  var client = new catan.models.ClientModel(0);
  ok(client != null, 'Client Model is not null');
  ok(client.getPlayerID() == 0, 'PlayerID = 0');
});
