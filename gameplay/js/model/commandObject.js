var catan = catan || {};
catan.models = catan.models || {};




catan.models.CommandObject = (function() {
	

	function CommandObject(cObject){ 
		my_url = "/moves/" + cObject.type;
		this.setUrl(my_url);
		this.setType(cObject);

	};

	CommandObject.prototype.setUrl = function(url_in){

		this.url = url_in;
	};

	CommandObject.prototype.getUrl = function(){

		return this.url;
	};
	CommandObject.prototype.setType = function(type_in){
		this.type = type_in;
	};

	    return CommandObject;
})();
