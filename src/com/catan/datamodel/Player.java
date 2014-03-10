package com.catan.datamodel;

public class Player {
	
	// static members
	public final static int MAXPOINTS = 10;
	
	// backing fields
	private int roads;
	private int cities;
	private int settlements;
	private int soldiers;
	private int victoryPoints;
	private int monuments;
	private int playerID;
	private int orderNumber;
	private String name;
	private String color;
	private boolean longestRoad;
	private boolean largestArmy;
	private boolean playedDevCard;
	private boolean discarded;
	private Resources resources;
	private DevCardList oldDevCards;
	private DevCardList newDevCards;
	
	// getters and setters
	public int getRoads() {
		return roads;
	}
	public void setRoads(int roads) {
		this.roads = roads;
	}
	public int getCities() {
		return cities;
	}
	public void setCities(int cities) {
		this.cities = cities;
	}
	public int getSettlements() {
		return settlements;
	}
	public void setSettlements(int settlements) {
		this.settlements = settlements;
	}
	public int getSoldiers() {
		return soldiers;
	}
	public void setSoldiers(int soldiers) {
		this.soldiers = soldiers;
	}
	public int getVictoryPoints() {
		return victoryPoints;
	}
	public void setVictoryPoints(int victoryPoints) {
		this.victoryPoints = victoryPoints;
	}
	public int getMonuments() {
		return monuments;
	}
	public void setMonuments(int monuments) {
		this.monuments = monuments;
	}
	public int getPlayerID() {
		return playerID;
	}
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public boolean isLongestRoad() {
		return longestRoad;
	}
	public void setLongestRoad(boolean longestRoad) {
		this.longestRoad = longestRoad;
	}
	public boolean isLargestArmy() {
		return largestArmy;
	}
	public void setLargestArmy(boolean largestArmy) {
		this.largestArmy = largestArmy;
	}
	public boolean isPlayedDevCard() {
		return playedDevCard;
	}
	public void setPlayedDevCard(boolean playedDevCard) {
		this.playedDevCard = playedDevCard;
	}
	public boolean isDiscarded() {
		return discarded;
	}
	public void setDiscarded(boolean discarded) {
		this.discarded = discarded;
	}
	public Resources getResources() {
		return resources;
	}
	public void setResources(Resources resources) {
		this.resources = resources;
	}
	public DevCardList getOldDevCards() {
		return oldDevCards;
	}
	public void setOldDevCards(DevCardList oldDevCards) {
		this.oldDevCards = oldDevCards;
	}
	public DevCardList getNewDevCards() {
		return newDevCards;
	}
	public void setNewDevCards(DevCardList newDevCards) {
		this.newDevCards = newDevCards;
	}
	public static int getMaxpoints() {
		return MAXPOINTS;
	}
}