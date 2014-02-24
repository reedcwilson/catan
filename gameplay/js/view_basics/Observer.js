/**
    This is the namespace to hold the base observer
    @module catan.misc
    @namespace misc
*/

var catan = catan || {};
catan.core = catan.core || {};

catan.core.ObserverList = (function observerListClass(){

	function ObserverList(){
		this.observerList = [];
	}

	ObserverList.prototype.add = function(obj) {
		return this.observerList.push(obj);
	};

	ObserverList.prototype.count = function(){
		return this.observerList.length;
	};

	ObserverList.prototype.get = function(index) {
		if(index > -1 && index < this.observerList.length){
			return this.observerList[index];
		}
	};

	ObserverList.prototype.indexOf = function(obj, startIndex) {
		var i = startIndex;
		while(i <this.observerList.length) {
			if(this.ovserverList[i] === obj){
				return i;
			}
			i++
		}
		return -1;
	};
		
	ObserverList.prototype.removeAt = function(index){
		this.ovserverList.splice(index, 1);
	};
	return ObserverList;
}());

catan.core.Subject = (function Subject_Class(){

	function Subject(){
		this.observers = new catan.core.ObserverList();
	}

	Subject.prototype.addObserver = function(observer){
		this.observers.add(observer);
	};

	Subject.prototype.removeObserver = function(observer){
		this.observers.removeAt(this.observers.indexOf(observer, 0));
	};

	Subject.prototype.notify = function(context, obs){
		var observerCount = obs.count();
		for(var i=0; i<observerCount; i++){
			obs.get(i).updateFromModel(context);
		}
	};
	
	return Subject;
}());

catan.core.Observer = (function Observer_Class(){

	function Observer(){
	}

	Observer.prototype.updateFromModel = function(){};

	Observer.prototype.initFromModel = function() {
		// this.updateFromModel();
	};
	return Observer;
}());

