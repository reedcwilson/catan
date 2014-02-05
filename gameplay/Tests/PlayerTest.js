test( "hello test", function() {
  ok( 1 == "1", "Passed!" );
});

test( "Create Player", function() {
	var player = new catan.models.Player();
	ok( true, 'Passed!');
});
