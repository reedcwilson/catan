package com.catan.main.datamodel.map;

import com.catan.main.datamodel.hexgrid.HexGrid;
import com.catan.main.datamodel.hexgrid.base.MapObject;
import com.catan.main.datamodel.hexgrid.edge.Edge;
import com.catan.main.datamodel.hexgrid.edge.EdgeDirection;
import com.catan.main.datamodel.hexgrid.edge.EdgeLocation;
import com.catan.main.datamodel.hexgrid.edge.EdgeValue;
import com.catan.main.datamodel.hexgrid.hex.Hex;
import com.catan.main.datamodel.hexgrid.hex.HexLocation;
import com.catan.main.datamodel.hexgrid.vertex.Vertex;
import com.catan.main.datamodel.hexgrid.vertex.VertexDirection;
import com.catan.main.datamodel.hexgrid.vertex.VertexLocation;
import com.catan.main.datamodel.hexgrid.vertex.VertexValue;
import com.catan.main.datamodel.player.ResourceHand;

import java.io.Serializable;
import java.util.*;

public class Map implements Serializable {

    //region Static Fields
    private static Resource[] HEX_TYPES = {Resource.Ore, Resource.Wheat, Resource.Wood, Resource.Ore, Resource.Wheat, Resource.Sheep, Resource.Wheat, Resource.Sheep, Resource.Wood, Resource.Brick, null, Resource.Brick, Resource.Sheep, Resource.Sheep, Resource.Wood, Resource.Brick, Resource.Ore, Resource.Wood, Resource.Wheat};
    private static HexLocation[] HEX_LOCATIONS = {new HexLocation(-2, 0), new HexLocation(-2, 1), new HexLocation(-2, 2), new HexLocation(-1, 2), new HexLocation(0, 2), new HexLocation(1, 1), new HexLocation(2, 0), new HexLocation(2, -1), new HexLocation(2, -2), new HexLocation(1, -2), new HexLocation(0, -2), new HexLocation(-1, -1), new HexLocation(-1, 0), new HexLocation(-1, 1), new HexLocation(0, 1), new HexLocation(1, 0), new HexLocation(1, -1), new HexLocation(0, -1), new HexLocation(0, 0)};
    private static EdgeLocation[] PORT_LOCATIONS = {new EdgeLocation(1, -3, EdgeDirection.S), new EdgeLocation(3, -3, EdgeDirection.SW), new EdgeLocation(3, -1, EdgeDirection.NW), new EdgeLocation(2, 1, EdgeDirection.NW), new EdgeLocation(0, 3, EdgeDirection.N), new EdgeLocation(-2, 3, EdgeDirection.NE), new EdgeLocation(-3, 2, EdgeDirection.NE), new EdgeLocation(-3, 0, EdgeDirection.SE), new EdgeLocation(-1, -2, EdgeDirection.S)};
    private static Resource[] PORT_TYPE = {Resource.Ore, null, Resource.Sheep, null, null, Resource.Brick, Resource.Wood, null, Resource.Wheat};
    private static Integer[] NUMBERS = {Integer.valueOf(5), Integer.valueOf(2), Integer.valueOf(6), Integer.valueOf(3), Integer.valueOf(8), Integer.valueOf(10), Integer.valueOf(6), Integer.valueOf(12), Integer.valueOf(11), Integer.valueOf(4), null, Integer.valueOf(8), Integer.valueOf(10), Integer.valueOf(9), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(9), Integer.valueOf(3), Integer.valueOf(11)};
    //endregion

    //region Fields
    private int radius;
    private HexGrid hexGrid;
    private HashSet<Port> ports = new HashSet();
    private HexLocation robber;
    private HashMap<Integer, HashSet<HexLocation>> numbers;
    //endregion

    public Map(int radius) {
        this.hexGrid = new HexGrid(radius, new CatanHex(null), radius - 1, radius - 1);
        this.numbers = new HashMap();
        this.radius = radius;
    }

    //region Static Generate Methods
    public static Map generateNewMap(boolean randomTiles, boolean randomNumbers, boolean randomPorts, long seed) {
        ArrayList<Integer> numbers = new ArrayList(Arrays.asList(NUMBERS));
        ArrayList<Resource> hexTypes = new ArrayList(Arrays.asList(HEX_TYPES));
        ArrayList<HexLocation> hexLocations = new ArrayList(Arrays.asList(HEX_LOCATIONS));
        ArrayList<Resource> portTypes = new ArrayList(Arrays.asList(PORT_TYPE));
        ArrayList<EdgeLocation> portLocations = new ArrayList(Arrays.asList(PORT_LOCATIONS));
        if (randomTiles) {
            Collections.shuffle(numbers, new Random(seed));
        }
        if (randomNumbers) {
            Collections.shuffle(hexTypes, new Random(seed));
        }
        if (randomPorts) {
            Collections.shuffle(portTypes, new Random(seed));
        }
        if (numbers.indexOf(null) != hexTypes.indexOf(null)) {
            int newDesertIndex = hexTypes.indexOf(null);
            int oldDesertIndex = numbers.indexOf(null);
            int lastIndex = numbers.size() - 1;

            int elementToMove = numbers.get(newDesertIndex).intValue();
            int lastElement = numbers.get(lastIndex).intValue();

            numbers.set(oldDesertIndex, Integer.valueOf(lastElement));
            numbers.set(lastIndex, Integer.valueOf(elementToMove));
            numbers.set(newDesertIndex, null);
        }
        Map map = new Map(4);
        for (int spotIndex = 0; spotIndex < hexLocations.size(); spotIndex++) {
            HexLocation location = hexLocations.get(spotIndex);
            Resource resource = hexTypes.get(spotIndex);
            Integer number = numbers.get(spotIndex);
            if (resource != null) {
                map.setResourceHex(location.getX(), location.getY(), resource);
                map.setNumber(location, number);
            } else {
                map.setRobber(location);
                map.setDesertHex(location.getX(), location.getY());
            }
        }
        map.surroundWithWater();
        for (int count = 0; count < portTypes.size(); count++) {
            EdgeLocation portLoc = portLocations.get(count);
            Resource portType = portTypes.get(count);
            map.setPort(portLoc.getX(), portLoc.getY(), portLoc.getDirection(), portType);
        }
        return map;
    }
    public static Map generateNewMap(boolean randomTiles, boolean randomNumbers, boolean randomPorts) {
        long seed = System.nanoTime();
        return generateNewMap(randomTiles, randomNumbers, randomPorts, seed);
    }
    public static Map getDefaultMap() {
        return generateNewMap(false, false, false);
    }
    //endregion

    //region City
    public void addCity(int x, int y, VertexDirection direction, int owner) {
        addCity(new VertexLocation(x, y, direction), owner);
    }
    public void addCity(VertexLocation vertexLocation, int owner) {
        City city = new City(owner);
        Vertex v = this.hexGrid.getVertex(vertexLocation);
        v.setValue(city);
    }
    public boolean canAddCity(VertexLocation vertexLocation, int owner) {
        boolean canAdd = true;
        Vertex vertex = this.hexGrid.getVertex(vertexLocation);
        if (vertex == null) {
            return false;
        }
        VertexValue val = vertex.getValue();
        canAdd = (val != null) && (val.getOwnerID() == owner) && ((val instanceof Settlement));
        return canAdd;
    }
    //endregion

    //region Road
    public void addRoad(EdgeLocation edgeLocation, int owner) {
        Road road = new Road(owner);
        this.hexGrid.getEdge(edgeLocation).setValue(road);
    }
    public void addRoad(int x, int y, EdgeDirection direction, int owner) {
        addRoad(new EdgeLocation(x, y, direction), owner);
    }
    public boolean canAddRoad(EdgeLocation edgeLocation, int owner, boolean canBeDisconnected) {
        boolean connected = false;
        boolean isLand = false;
        Edge edge = this.hexGrid.getEdge(edgeLocation);
        if ((edge != null) && (edge.getValue() != null) && (((EdgeValue) edge.getValue()).getOwnerID() != -1)) {
            return false;
        }
        for (EdgeLocation eLocation : edgeLocation.getEquivalenceGroup()) {
            CatanHex hex = (CatanHex) this.hexGrid.getHex(eLocation);
            if (hex != null) {
                isLand = (isLand) || (hex.isLand);
            }
        }
        if (!isLand) {
            return false;
        }
        if (canBeDisconnected) {
            VertexLocation v1 = edgeLocation.getNeighborVertices()[0];
            VertexLocation v2 = edgeLocation.getNeighborVertices()[1];
            boolean canPlace1 = canAddSettlement(v1, owner, canBeDisconnected);
            boolean canPlace2 = canAddSettlement(v2, owner, canBeDisconnected);

            return (canPlace1) || (canPlace2);
        }
        for (EdgeLocation eLocation : edgeLocation.getConnectedEdges()) {
            Edge road = this.hexGrid.getEdge(eLocation);
            connected = (connected) || ((road != null) && (road.getValue() != null) && (((EdgeValue) road.getValue()).getOwnerID() == owner));
        }
        for (VertexLocation neighbor : edgeLocation.getNeighborVertices()) {
            Vertex vertex = this.hexGrid.getVertex(neighbor);
            if ((vertex != null) && (vertex.getValue() != null) && (vertex.getValue().getOwnerID() == owner)) {
                connected = true;
            }
        }
        return connected;
    }
    //endregion

    //region Settlement
    public void addSettlement(int x, int y, VertexDirection direction, int owner) {
        addSettlement(new VertexLocation(x, y, direction), owner);
    }
    public void addSettlement(VertexLocation vertexLocation, int owner) {
        Settlement settlement = new Settlement(owner);
        this.hexGrid.getVertex(vertexLocation).setValue(settlement);
    }
    public boolean canAddSettlement(VertexLocation vertexLocation, int owner, boolean canBeDisconnected) {
        boolean connected = canBeDisconnected;
        boolean isLand = false;
        Vertex vertex = this.hexGrid.getVertex(vertexLocation);
        if (vertex == null) {
            return false;
        }
        VertexValue val = vertex.getValue();
        if ((val != null) && (val.getOwnerID() != -1)) {
            return false;
        }
        for (EdgeLocation eLocation : vertexLocation.getConnectedEdges()) {
            Edge e = this.hexGrid.getEdge(eLocation);
            if (e != null) {
                EdgeValue eVal = (EdgeValue) e.getValue();
                if ((eVal != null) && (eVal.getOwnerID() == owner)) {
                    connected = true;
                }
            }
        }
        for (VertexLocation neighbor : vertexLocation.getNeighborVertices()) {
            Vertex nVertex = this.hexGrid.getVertex(neighbor);
            if (nVertex != null) {
                VertexValue nVal = nVertex.getValue();
                if (((nVal instanceof Settlement)) || ((nVal instanceof City))) {
                    return false;
                }
                Hex hex = this.hexGrid.getHex(neighbor);
                if (hex != null) {
                    if (((CatanHex) hex).getIsLand()) {
                        isLand = true;
                    }
                }
            }
        }
        return (connected) && (isLand);
    }
    //endregion

    //region Robber
    public List<Integer> getPlayersToRob(HexLocation hexLocation, int turnIndex) {
        List<Integer> players = new ArrayList();

        MapObject[] vertexes = getHex(hexLocation).getVertices();
        for (int i = 0; i < vertexes.length; i++) {
            int id = vertexes[i].getValue().getOwnerID();
            if (id != -1) {
                if ((!players.contains(Integer.valueOf(id))) && (id != turnIndex)) {
                    players.add(Integer.valueOf(id));
                }
            }
        }
        return players;
    }
    public HexLocation getRobber() {
        return this.robber;
    }
    public void setRobber(HexLocation robberSpot) {
        this.robber = robberSpot;
    }
    public boolean canPlaceRobber(HexLocation hexLocation) {
        if (hexLocation == null) {
            return false;
        }
        if (this.robber.equals(hexLocation)) {
            return false;
        }
        if (getHex(hexLocation) == null) {
            return false;
        }
        if (!((CatanHex) getHex(hexLocation)).isLand) {
            return false;
        }
        return true;
    }
    //endregion

    //region Vertex/Edge Values
    public EdgeValue getEdgeValue(EdgeLocation edgeLocation) {
        if (this.hexGrid.getEdge(edgeLocation) == null) {
            return null;
        }
        return (EdgeValue) this.hexGrid.getEdge(edgeLocation).getValue();
    }
    public VertexValue getVertexValue(VertexLocation vLocation) {
        if (this.hexGrid.getVertex(vLocation) == null) {
            return null;
        }
        return this.hexGrid.getVertex(vLocation).getValue();
    }
    //endregion

    //region Hex
    public Hex getHex(HexLocation location) {
        return this.hexGrid.getHex(location);
    }
    public void setHex(HexLocation location, CatanHex hex) {
        this.hexGrid.setHex(location, hex);
    }
    public void setHex(int x, int y, CatanHex hex) {
        setHex(new HexLocation(x, y), hex);
    }

    public void setDesertHex(int x, int y) {
        setHex(x, y, new DesertHex(null));
    }
    public void setResourceHex(int x, int y, Resource resource) {
        setHex(x, y, new NormalHex(null, resource));
    }
    public void setWaterHex(int x, int y) {
        setHex(x, y, new WaterHex(null));
    }

    public HexGrid getHexGrid() {
        return this.hexGrid;
    }
    //endregion

    //region Ports
    public HashSet<Port> getPorts() {
        return this.ports;
    }
    public void setPort(int x, int y, EdgeDirection d, Resource resource) {
        EdgeLocation edgeLocation = new EdgeLocation(x, y, d);
        Port port = new Port(edgeLocation, resource);
        this.ports.add(port);
    }
    public void setPort(Port port) {
        this.ports.add(port);
    }
    //endregion

    //region Numbers
    public HashMap<Integer, HashSet<HexLocation>> getNumbers() {
        return this.numbers;
    }
    public void setNumber(HexLocation location, Integer number) {
        if (!this.numbers.containsKey(number)) {
            this.numbers.put(number, new HashSet());
        }
        ((HashSet) this.numbers.get(number)).add(location);
    }
    public void setNumber(int x, int y, int number) {
        setNumber(new HexLocation(x, y), Integer.valueOf(number));
    }
    //endregion

    //region Radius
    public int getRadius() {
        return this.radius;
    }
    //endregion

    //region Resources
    public ResourceHand[] getResourcesByRoll(Integer rolledNumber) {
        ResourceHand[] rolledResources =
                {
                        new ResourceHand(),
                        new ResourceHand(),
                        new ResourceHand(),
                        new ResourceHand()
                };
        for (HexLocation location : getNumbers().get(rolledNumber)) {
            if (!location.equals(this.robber)) {
                Hex hex = getHex(location);
                for (MapObject<VertexLocation, VertexValue> mapObject : hex.getVertices()) {
                    VertexValue value = mapObject.getValue();
                    if ((value.getOwnerID() >= 0) && (hex instanceof NormalHex)) {
                        NormalHex h = (NormalHex) hex;
                        rolledResources[value.getOwnerID()].add(h.getLandType(), value.getWorth());
                    }
                }
            }
        }
        return rolledResources;
    }
    public ArrayList<Resource> getLandTypeOfSurrounding(VertexLocation vertexLocation) {
        VertexLocation[] vertices = vertexLocation.getEquivalenceGroup();
        ArrayList<Resource> landTypes = new ArrayList();
        for (VertexLocation vertex : vertices) {
            Hex hex = getHex(vertex);
            if (hex instanceof NormalHex) {
                NormalHex rh = (NormalHex) hex;
                landTypes.add(rh.getLandType());
            }
        }
        return landTypes;
    }
    //endregion

    //region Helper Methods
    private void surroundWithWater() {
        int length = this.radius - 1;
        int cycles = 6;
        int waterHexes = cycles * length;
        int[] changeByCycle = {1, 1, 0, -1, -1, 0};
        int x = 1 - this.radius;
        int y = 0;
        for (int i = 0; i < waterHexes; i++) {
            setWaterHex(x, y);

            int cycle = i / length;
            int xIndex = cycle;
            int yIndex = (cycle - 2 + cycles) % cycles;
            x += changeByCycle[xIndex];
            y += changeByCycle[yIndex];
        }
    }
    //endregion

    //region Overrides

    @Override
    public String toString() {
        return "Map{" +
                "radius=" + radius +
                ", hexGrid=" + hexGrid +
                ", ports=" + ports +
                ", robber=" + robber +
                ", numbers=" + numbers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Map)) return false;

        Map map = (Map) o;

        if (radius != map.radius) return false;
        if (hexGrid != null ? !hexGrid.equals(map.hexGrid) : map.hexGrid != null) return false;
        if (numbers != null ? !numbers.equals(map.numbers) : map.numbers != null) return false;
        if (ports != null ? !ports.equals(map.ports) : map.ports != null) return false;
        if (robber != null ? !robber.equals(map.robber) : map.robber != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = radius;
        result = 31 * result + (hexGrid != null ? hexGrid.hashCode() : 0);
        result = 31 * result + (ports != null ? ports.hashCode() : 0);
        result = 31 * result + (robber != null ? robber.hashCode() : 0);
        result = 31 * result + (numbers != null ? numbers.hashCode() : 0);
        return result;
    }

    //endregion
}
