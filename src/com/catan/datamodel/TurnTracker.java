package com.catan.datamodel;

public class TurnTracker {
	
	// backing fields
	private String _status;
	private int _currentTurn;
	
	// getters and setters
	public String get_status() {
		return _status;
	}
	public void set_status(String _status) {
		this._status = _status;
	}
	public int get_currentTurn() {
		return _currentTurn;
	}
	public void set_currentTurn(int _currentTurn) {
		this._currentTurn = _currentTurn;
	}

}
