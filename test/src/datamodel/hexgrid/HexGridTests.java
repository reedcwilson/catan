package src.datamodel.hexgrid;

import com.catan.main.datamodel.game.CreateGameRequest;
import com.catan.main.datamodel.game.Game;
import com.catan.main.datamodel.hexgrid.edge.Edge;
import com.catan.main.datamodel.hexgrid.edge.EdgeDirection;
import com.catan.main.datamodel.hexgrid.edge.EdgeLocation;
import com.catan.main.server.Client;
import com.catan.main.server.ServerUtils;
import org.junit.*;
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
    public void testRandomTestName() {
        try {
            EdgeLocation loc = new EdgeLocation(0, 0, EdgeDirection.NE);
            Edge edge = _game.getModel().getMap().getHexGrid().getEdge(loc);
            assertSame(edge.getLocation(), null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
