var catan = catan || {};

catan.ResourceList = (function() {
	/**
		The Resource List contains the different resources and the amounts

		@class ResourceList
		@constructor
	*/
	function ResourceList(){};

	/**
		Adds a resource to the current List
		<pre>
		PRE: Type is a valid list object
		POST: The object in the list gains the number
		</pre>
		@method addResource
		@param {Resource} type The resource to add
		@param {number} number The amount to add
	*/
	
	function addResource(type, number) {};

	/**
		Removes a resource from the current list
		<pre>
		PRE: The resource sent is a valid resouce
		PRE: It has at least the amount of the number to be removed
		POST: The resource lost the amount
		</pre>
		@method removeResource
		@param {Resource} resource The resource to remove from
		@param {Number} number The amount to be removed
	*/
	function removeResource(resource, number) {};

	/**
		Get's the amount of a specific resource
		<pre>
		PRE: The resource sentis a valid resource
		POST: None
		</pre>
		@method getResourceAmount
		@param {Resource} resource The resource to get the amount of
		@return {Number} The amount of resources that exsists
	*/
	function getResourceAmount(resource) {};
	
	return ResourceList;
})();
