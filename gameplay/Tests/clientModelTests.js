
module("overhead");
test( "Constructor Test", function() 
{
  expect(15);

  var client = new catan.models.ClientModel(0);
  ok(client != null, 'Client Model is not null');
  ok(client.getClientID() == 0, 'ClientID = 0');
  ok(client.getBank() == undefined, 'Bank is undefined');
  ok(client.getChat() == undefined, 'Chat is undefined');
  ok(client.getDeck() == undefined, 'Deck is undefined');
  ok(client.getBiggestArmy() == undefined, 'BiggestArmy is undefined');
  ok(client.getLog() == undefined, 'Log is undefined');
  ok(client.getLongestRoad() == undefined, 'LongestRoad is undefined');
  ok(client.getMap() == undefined, 'Map is undefined');
  ok(client.getPlayers() == undefined, 'Players is undefined');
  ok(client.getPlayerIndex() == -1, 'PlayerIndex = -1');
  ok(client.getProxy() != undefined, 'Proxy is not undefined');
  ok(client.getTradeOffer() == undefined, 'TradeOffer is undefined');
  ok(client.getTurnTracker() == undefined, 'TurnTracker is undefined');
  ok(client.getWinner() == undefined, 'Winner is undefined');
});

test("InitFromServer Test", function() 
{
  expect(17);

  var client = new catan.models.ClientModel(0);
  client.initFromServer();
  client.init(model)

  ok(client.getBank().brick == 23, 'Bank has 23 Bricks');		 		
  ok(client.getBiggestArmy() == 2, 'Player 2 has the Biggest Army');
  ok(client.getChat().printList() == '', 'Chat is empty');
  ok(client.getClientID() == 0, 'ClientID = 0'); 
  ok(client.getDeck().soldier == 14, 'Deck has 14 Soldiers');
  ok(client.getDeck().monopoly == 2, 'Deck has 2 Monopolies');
  ok(client.getDeck().monument == 5, 'Deck has 5 Monuments');
  ok(client.getDeck().roadBuilding == 2, 'Deck has 2 RoadBuilding');
  ok(client.getDeck().yearOfPlenty == 2, 'Deck has 2 YearofPlenty');
  ok(client.getLog().getLines()[0].getMessage() == 'Sam built a road', 'Log begins with "Sam built a road"');
  ok(client.getLongestRoad() == -1, 'No one has the Longest Road');
  ok(client.getMap().getRadius() == 4, 'Map has a radius of 4');
  ok(client.playerIndex == 0, "Player Index was 0");
  ok(client.getTurnTracker().getCurrentTurn() == 0, "It's Player 0's turn");
  ok(client.getWinner() == -1, "There is no winner yet.");
  ok(client.getPlayers()[client.loadIndexByClient(11)].getRoads() == 13, "Player 11 has 13 Roads");
  ok(client.getTradeOffer().getSender() == -1, "There is no Trade Offer");

}); 

test("Querry Functions", function()
{
  var client = new catan.models.ClientModel(0);
  client.initFromServer();
  client.init(model)
  client.rollStupid = "Roll!";

  ok(client.isCurrentTurn(0) == true, "It's Sam's turn");
  ok(client.isCurrentTurn(1) == false, "It's not Brooke's turn");
  ok(client.canRoll() == true, "Sam can roll");
  ok(client.canEndTurn() == false, "Sam can't end turn yet");
  
});
