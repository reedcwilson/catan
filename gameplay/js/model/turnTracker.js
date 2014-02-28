catan = catan || {};
catan.models = catan.models || {};

/**
	This module contains the turn tracker
	
	@module		catan.models
	@namespace models
	@constructor
	@class TurnTracker
*/

catan.models.TurnTracker = (function TurnTrackerClass() {

    core.defineProperty(TurnTracker.prototype, "currentTurn");
    core.defineProperty(TurnTracker.prototype, "status");

    function TurnTracker() {
    }

    /**
        Sets the info on the turn using a turnJSON retrieved from the JSON
		<pre>
			PRE: turnJSON is properlly formatted and contains only the turnTracker info
			POST: this.currentTuen = turnJSON.currentTUrn
			POST: this.status = turnJSON.status
		</pre>
        @method setInfo
        @param {JSON} turnJSON - The turnJSON to load the info from.
    */
    TurnTracker.prototype.setInfo = function(turnJSON) {
        this.setStatus(turnJSON.status);
        this.setCurrentTurn(turnJSON.currentTurn);
    };

	return TurnTracker;
}());
