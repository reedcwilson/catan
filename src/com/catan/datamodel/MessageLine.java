package com.catan.datamodel;

public class MessageLine {
	
	// backing fields
	private String _source;
	private String _message;
	
	// getters and setters
	public String get_source() {
		return _source;
	}
	public void set_source(String _source) {
		this._source = _source;
	}
	public String get_message() {
		return _message;
	}
	public void set_message(String _message) {
		this._message = _message;
	}
}