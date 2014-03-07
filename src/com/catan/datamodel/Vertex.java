package com.catan.datamodel;

public class Vertex {

	// backing fields
	private String _direction;
	private Location _location;
	
	// getters and setters
	public String get_direction() {
		return _direction;
	}
	public void set_direction(String _direction) {
		this._direction = _direction;
	}
	public Location get_location() {
		return _location;
	}
	public void set_location(Location _location) {
		this._location = _location;
	}
}
