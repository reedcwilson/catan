package com.catan.datamodel;

import java.util.ArrayList;

public class Log {
	
	// backing fields
	private ArrayList<MessageLine> _lines;

	// getters and setters
	public ArrayList<MessageLine> get_lines() {
		return _lines;
	}
	public void set_lines(ArrayList<MessageLine> _lines) {
		this._lines = _lines;
	}
}