package com.catan.datamodel;

import java.util.ArrayList;

public class Map {
	
	// backing fields
	private int _radius;
	private HexGrid _hexGrid;
	private Numbers _numbers;
	private ArrayList<Port> _ports;
	private Location _robber;
	
	// getters and setters
	public int get_radius() {
		return _radius;
	}
	public void set_radius(int _radius) {
		this._radius = _radius;
	}
	public HexGrid get_hexGrid() {
		return _hexGrid;
	}
	public void set_hexGrid(HexGrid _hexGrid) {
		this._hexGrid = _hexGrid;
	}
	public Numbers get_numbers() {
		return _numbers;
	}
	public void set_numbers(Numbers _numbers) {
		this._numbers = _numbers;
	}
	public ArrayList<Port> get_ports() {
		return _ports;
	}
	public void set_ports(ArrayList<Port> _ports) {
		this._ports = _ports;
	}
	public Location get_robber() {
		return _robber;
	}
	public void set_robber(Location _robber) {
		this._robber = _robber;
	}
	
}