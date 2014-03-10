package com.catan.datamodel;

import java.util.ArrayList;

public class DataModel {

	// backing fields
	private DevCardList _deck;
	private Map _map;
	private ArrayList<Player> _players;
	private Log _log;
	private Chat _chat;
	private Resources _bank;
	private TurnTracker _turnTracker;
	private int _biggestArmy;
	private int _longestRoad;
	private int _winner;
	private int _revision;
	
	// getters and setters
	public DevCardList get_deck() {
		return _deck;
	}
	public void set_deck(DevCardList _deck) {
		this._deck = _deck;
	}
	public Map get_map() {
		return _map;
	}
	public void set_map(Map _map) {
		this._map = _map;
	}
	public ArrayList<Player> get_players() {
		return _players;
	}
	public void set_players(ArrayList<Player> _players) {
		this._players = _players;
	}
	public Log get_log() {
		return _log;
	}
	public void set_log(Log _log) {
		this._log = _log;
	}
	public Chat get_chat() {
		return _chat;
	}
	public void set_chat(Chat _chat) {
		this._chat = _chat;
	}
	public Resources get_bank() {
		return _bank;
	}
	public void set_bank(Resources _bank) {
		this._bank = _bank;
	}
	public TurnTracker get_turnTracker() {
		return _turnTracker;
	}
	public void set_turnTracker(TurnTracker _turnTracker) {
		this._turnTracker = _turnTracker;
	}
	public int get_biggestArmy() {
		return _biggestArmy;
	}
	public void set_biggestArmy(int _biggestArmy) {
		this._biggestArmy = _biggestArmy;
	}
	public int get_longestRoad() {
		return _longestRoad;
	}
	public void set_longestRoad(int _longestRoad) {
		this._longestRoad = _longestRoad;
	}
	public int get_winner() {
		return _winner;
	}
	public void set_winner(int _winner) {
		this._winner = _winner;
	}
	public int get_revision() {
		return _revision;
	}
	public void set_revision(int _revision) {
		this._revision = _revision;
	}
}


