package com.catan.main.datamodel.game;

public class CreateGameRequest {

    //region Fields
    boolean randomTiles;
    boolean randomNumbers;
    boolean randomPorts;
    String name;
    //endregion

    public CreateGameRequest(boolean randomTiles, boolean randomNumbers, boolean randomPorts, String name) {
        this.randomTiles = randomTiles;
        this.randomNumbers = randomNumbers;
        this.randomPorts = randomPorts;
        this.name = name;
    }

    //region Properties
    public boolean isRandomTiles() {
        return this.randomTiles;
    }
    public void setRandomTiles(boolean randomTiles) {
        this.randomTiles = randomTiles;
    }

    public boolean isRandomNumbers() {
        return this.randomNumbers;
    }
    public void setRandomNumbers(boolean randomNumbers) {
        this.randomNumbers = randomNumbers;
    }

    public boolean isRandomPorts() {
        return this.randomPorts;
    }
    public void setRandomPorts(boolean randomPorts) {
        this.randomPorts = randomPorts;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    //endregion
}