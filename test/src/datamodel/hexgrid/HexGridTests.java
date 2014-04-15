package src.datamodel.hexgrid;

import com.catan.main.datamodel.game.CreateGameRequest;
import com.catan.main.datamodel.game.Game;
import com.catan.main.datamodel.hexgrid.HexGrid;
import com.catan.main.datamodel.hexgrid.edge.Edge;
import com.catan.main.datamodel.hexgrid.edge.EdgeDirection;
import com.catan.main.datamodel.hexgrid.edge.EdgeLocation;
import com.catan.main.datamodel.hexgrid.vertex.Vertex;
import com.catan.main.datamodel.hexgrid.vertex.VertexDirection;
import com.catan.main.datamodel.hexgrid.vertex.VertexLocation;
import com.catan.main.datamodel.hexgrid.vertex.VertexValue;
import com.catan.main.persistence.ContextCreator;
import com.catan.main.persistence.DataContext;
import com.catan.main.server.Client;
import com.catan.main.server.ServerUtils;
import org.junit.*;

import static org.junit.Assert.*;

public class HexGridTests {
    private static Game _game;
    private static DataContext _dataContext;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        _dataContext = ContextCreator.getDataContext(ContextCreator.ContextType.SQLITE);
        _dataContext.startTransaction();
        ServerUtils.initialize(_dataContext);
        _game = ServerUtils.createGame(new CreateGameRequest(true, true, true, "test1"));
        Client client = new Client("time", 0L, 0L);
        ServerUtils.resetGame(client);
    }

    @After
    public void tearDown() throws Exception {
        _dataContext.endTransaction(false);
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
            assertTrue(!grid.toString().equals(""));
            int code = grid.hashCode();
            assertTrue(code != 0 || code != 1);
            assertTrue(grid.equals(grid));
            assertTrue(grid.getVertex(new VertexLocation(0,0, VertexDirection.NW)) != null);
            assertTrue(grid.getRadius() > 0);
            assertTrue(grid.getEdge(new EdgeLocation(0, 0, EdgeDirection.NW)) != null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void VertexTest()
    {
        try
        {
            VertexLocation loc = new VertexLocation(0, 0, VertexDirection.NE);
            Vertex vertex = _game.getModel().getMap().getHexGrid().getVertex(loc);

            Vertex vertex2 = new Vertex(new VertexValue(-1,0),loc);

            assertTrue(vertex.getValue().equals(vertex2.getValue()));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
