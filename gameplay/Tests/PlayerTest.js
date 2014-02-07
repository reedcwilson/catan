QUnit.config.autostart = false;
var model = {};
var player = new catan.models.Player();
jQuery(function () {
  getModel(function(data) {
    model = data || model1;
    QUnit.start();
  });
});

test("Test Player DevCards", function() {
	console.log(model.players[0]);
	player.setInfo(model.players[0]);
	var devCardsList = new Array();
	devCardsList["monopoly"] = 0;
	devCardsList["monument"] = 0;
	devCardsList["roadBuilding"] = 0;
	devCardsList["soldier"] = 0;
	devCardsList["yearOfPlenty"] = 1;
	player.setOldDevCards(devCardsList);
	ok(player.canPlayDevCard("yearOfPlenty"), "Can play Year of Plenty");
	ok(!player.canPlayDevCard("monopoly"), "Cannot play monopoly");
	player.playDevCard("yearOfPlenty");
	ok(!player.canPlayDevCard("yearOfPlenty"), "Played Year of Plenty");
	player.addDevCard("soldier");
	ok(player.newDevCards["soldier"] == 1, "Successfully added a devCard");
});

test("Test Player Resource", function() {
	var resourceList = new Array();
	resourceList["brick"] = 0;
	resourceList["ore"] = 0;
	resourceList["sheep"] = 1;
	resourceList["wheat"] = 1;
	resourceList["wood"] = 0;
	ok(player.hasResources(resourceList, "Has resources!"));
	resourceList["wood"] = 7;
	ok(!player.hasResources(resourceList, "Doesn't have resources!"));
	ok(player.getNumOfCards() == 3, "Has 3 Resource Cards");
	ok(!player.needsToDiscard(), "The player shouldn't discard");
	player.setResources(resourceList);
	ok(player.needsToDiscard(), "The player needs to discard. The player has: " + player.getNumOfCards());
});

test("Test Player Trade", function() {
	//Testing Trade
	var tradeList = new Array();
	tradeList["brick"] = 0;
	tradeList["ore"] = -1;
	tradeList["sheep"] = 1;
	tradeList["wheat"] = 1;
	tradeList["wood"] = 7;
	var tradeOffer = new catan.models.TradeOffer();
	tradeOffer.setOffer(tradeList);
	ok(player.canOfferTrade(tradeOffer), "Can offer all of it's resources");
	ok(!player.canAcceptTrade(tradeOffer), "Cannot accept because he doesn't have a ore");
	tradeList["ore"] = 0;
	tradeList["sheep"] = -1;
	tradeOffer.setOffer(tradeList);
	ok(player.canAcceptTrade(tradeOffer),"Can accept trade");
	tradeList["wood"]= 8;
	tradeOffer.setOffer(tradeList);
	ok(!player.canOfferTrade(tradeOffer),"Cannot offer trade");
});

function getModel(callback) {
  $.get('/game/model', function(data, status, jqxhr) {
    callback(data);
  }).fail(function(jqxhr, text) {
    console.log("ahhh!!! couldn't get model!");
  });
}

