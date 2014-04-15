package src.datamodel;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.game.CreateGameRequest;
import com.catan.main.datamodel.game.Game;
import com.catan.main.datamodel.player.Bank;
import com.catan.main.datamodel.player.MockTurnTracker;
import com.catan.main.datamodel.player.Player;
import com.catan.main.datamodel.player.TurnTrackerInterface;
import com.catan.main.persistence.ContextCreator;
import com.catan.main.persistence.DataContext;
import com.catan.main.server.Client;
import com.catan.main.server.ServerUtils;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.*;
import static org.junit.Assert.* ;

public class DataModelTests {
    private static Game _game;
    private static Injector injector;
    private static DataContext _dataContext;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // all things you want to do only once (i.e. initialize sqlite driver)
        injector = Guice.createInjector(new AbstractModule() {

            @Override
            protected void configure() {
                bind(TurnTrackerInterface.class).to(MockTurnTracker.class);
            }
        });

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // clean up your resources when completely done testing
        // we may not need this
    }

    @Before
    public void setUp() throws Exception {
        // tasks you want to do before every task
        _dataContext = ContextCreator.getDataContext(ContextCreator.ContextType.SQLITE);
        _dataContext.startTransaction();
        ServerUtils.initialize(_dataContext);
        _game = ServerUtils.createGame(new CreateGameRequest(true, true, true, "test1"));
        Client client = new Client("time", 0L, 0L);
        ServerUtils.resetGame(client);

    }

    @After
    public void tearDown() throws Exception {
        // clean up after every test
        _dataContext.endTransaction(false);

    }

    @Test
    public void myTest() {
        try {
            // do stuff
            Player[] players = {null, null, null, null};
            DataModel model = injector.getInstance(DataModel.class);
            model.setBank(new Bank());
            DataModel cloney = _game.getModel().clone();
            assertFalse(_game.getModel().equals(model));

            assertTrue(model != null);
            assertTrue(model.getTurnTracker().getClass() == MockTurnTracker.class);

            assertTrue(_game.getModel().getPlayers() != model.getPlayers());
            assertTrue(model.getBank() !=  null);




        } catch (Exception e) {
            // print stuff
        }
    }

    @Test(expected = Exception.class)
    public void throwingShouldOccur() throws Exception {
        throw new Exception();
        // test things that should throw an exception
    }
}
