test( "Proxy Testing Start", function() {
	ConnectAsSam();

  ok( 1 == "1", "Passed!" );
});


function testProxy(){
test( "MessageList Print Message", function() {
		var testbest = new catan.models.MessageList();
	testbest.setInfo(JSON.stringify({lines:[{message: "hey", source: "jake"}]}));

	ok(testbest.printList() == "hey");

	});
  
	var prox = new catan.models.Proxy();
	var sender = new catan.models.CommandObject({type: "sendChat",playerIndex:1, content:"hey buddy"});

	prox.send(sender, function(data){

		test("sending chat", function(){
			ok(data);
		});
	});

	var sender = new catan.models.CommandObject({type: "buildRoad",playerIndex:1,roadLocation: {x:1,y:1,direction:"NE"},free:true});

	prox.send(sender, function(data){

		test("building a road", function(){
			ok(data);
		});
	});

	var sender = new catan.models.CommandObject({type: "rollNumber",playerIndex:1,number:2});

	prox.send(sender, function(data){

		test("rollNumber", function(){
			ok(data);
		});
	});


	var sender = new catan.models.CommandObject({type: "finishTurn",playerIndex:1});

	prox.send(sender, function(data){

		test("finishTurn", function(){
			ok(data);
		});
	});

	prox.getModel(testGetModel, success);

	 

}
function testGetModel(data){

test("get Model update callback", function(){
			ok(data);
		});

}
function success(data){

test("get Model success callback", function(){
			ok(data);
		});

}

function ConnectAsSam()
{
	var jqXHR = ajaxConnect("POST", "/user/login", 'username=Sam&password=sam');
	jqXHR.done(function (data) {
		var string = 'color=red&id=0';
		var newjqXHR = ajaxConnect("POST", "/games/join", string);
		newjqXHR.done(function (data){
			testProxy();
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

	return jqXHR;
}

<<<<<<< HEAD
=======
	 ok( 1 == "1", "Passed!" );
});
>>>>>>> 8ca74d636ec97ecfe722c0ee5cb1a464ca4ed81a
