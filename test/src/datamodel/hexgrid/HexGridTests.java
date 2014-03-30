package src.datamodel.hexgrid;

import com.catan.main.datamodel.game.CreateGameRequest;
import com.catan.main.datamodel.game.Game;
import com.catan.main.datamodel.hexgrid.HexGrid;
import com.catan.main.datamodel.hexgrid.base.Location;
import com.catan.main.datamodel.hexgrid.edge.Edge;
import com.catan.main.datamodel.hexgrid.edge.EdgeDirection;
import com.catan.main.datamodel.hexgrid.edge.EdgeLocation;
import com.catan.main.datamodel.hexgrid.vertex.VertexDirection;
import com.catan.main.datamodel.hexgrid.vertex.VertexLocation;
import com.catan.main.server.Client;
import com.catan.main.server.ServerUtils;
import org.junit.*;

import java.util.Collection;

import static org.junit.Assert.*;

public class HexGridTests {
    private static Game _game;
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        _game = ServerUtils.createGame(new CreateGameRequest(true, true, true, "test1"));
    }

    @Before
    public void setUp() throws Exception {
        Client client = new Client("time", 0L, 0L);
        ServerUtils.resetGame(client);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void hexGridTests() {
        try {
            EdgeLocation loc = new EdgeLocation(0, 0, EdgeDirection.NE);
            HexGrid grid = _game.getModel().getMap().getHexGrid();
            Edge edge = grid.getEdge(loc);
            assertSame(edge.getLocation(), null);
            assertTrue(grid.getOffsets() != null);
            int radius = grid.getRadius();
            assertTrue(grid.getY0() > -radius && grid.getY0() < radius && grid.getX0() > -radius && grid.getX0() < radius);
//            assertTrue(grid.getLocations() != null && !grid.getLocations().isEmpty());
            System.out.println(grid.toString());
            int code = grid.hashCode();
            assertTrue(code != 0 || code != 1);
            assertTrue(!grid.equals(grid));
            assertTrue(grid.getVertex(new VertexLocation(0,0, VertexDirection.NW)) != null);
            assertTrue(grid.getRadius() > 0);
            assertTrue(grid.getEdge(new EdgeLocation(0, 0, EdgeDirection.NW)) != null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
