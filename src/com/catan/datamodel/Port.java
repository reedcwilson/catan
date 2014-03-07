package com.catan.datamodel;

public class Port {

	// backing fields
	private int _ratio;
	private String _inputResource;
	private String _orientation;
	private Location _location;
	private Vertex _validVertex1;
	private Vertex _validVertex2;
	
	// getters and setters
	public int get_ratio() {
		return _ratio;
	}
	public void set_ratio(int _ratio) {
		this._ratio = _ratio;
	}
	public String get_inputResource() {
		return _inputResource;
	}
	public void set_inputResource(String _inputResource) {
		this._inputResource = _inputResource;
	}
	public String get_orientation() {
		return _orientation;
	}
	public void set_orientation(String _orientation) {
		this._orientation = _orientation;
	}
	public Location get_location() {
		return _location;
	}
	public void set_location(Location _location) {
		this._location = _location;
	}
	public Vertex get_validVertex1() {
		return _validVertex1;
	}
	public void set_validVertex1(Vertex _validVertex1) {
		this._validVertex1 = _validVertex1;
	}
	public Vertex get_validVertex2() {
		return _validVertex2;
	}
	public void set_validVertex2(Vertex _validVertex2) {
		this._validVertex2 = _validVertex2;
	}
}
