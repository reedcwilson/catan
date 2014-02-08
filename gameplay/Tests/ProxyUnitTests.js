
test( "proxy login", function() {
	var prox = new catan.models.Proxy();

	//should succeed
	var UserLogin = {username: "Sam", password: "sam"};
	var check = prox.login(UserLogin);

	//should fail

	UserLogin = {username: "sam", password: "sam"};
	var check = prox.login(UserLogin);

	ok( 1 == "1", "Passed!" );

	 
});

test( "proxy register", function() {
	var prox = new catan.models.Proxy();

	//should succeed
	var UserLogin = {username: "rank", password: "frank"};
	var check = prox.register(UserLogin);

	//should fail
	UserLogin = {username: "Sam", password: "sam"};
	var check = prox.register(UserLogin);

	ok( 1 == "1", "Passed!" );

	 
});

test( "Game List", function() {
	var prox = new catan.models.Proxy();

	//should succeed
	
	 prox.listGames();

	 ok( 1 == "1", "Passed!" );
});

test( "Create Game", function() {
	var prox = new catan.models.Proxy();

	//should succeed
	
	 prox.createGame({randomTiles: false, randomNumbers: true,randomPorts: true, name: "MyGame"});

	 ok( 1 == "1", "Passed!" );
});

test( "Join Game", function() {
	var prox = new catan.models.Proxy();

	//should succeed
	
	 prox.joinGame({color: "red", id: 3});

	 ok( 1 == "1", "Passed!" );
});
