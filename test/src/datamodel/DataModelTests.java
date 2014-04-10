package src.datamodel;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.commands.SendChat;
import com.catan.main.datamodel.devcard.DevCardDeck;
import com.catan.main.datamodel.game.CreateGameRequest;
import com.catan.main.datamodel.game.Game;
import com.catan.main.datamodel.map.Map;
import com.catan.main.datamodel.message.MessageBox;
import com.catan.main.datamodel.player.Bank;
import com.catan.main.datamodel.player.MockTurnTracker;
import com.catan.main.datamodel.player.Player;
import com.catan.main.datamodel.player.TurnTrackerInterface;
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

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // all things you want to do only once (i.e. initialize database driver)
        _game = ServerUtils.createGame(new CreateGameRequest(true, true, true, "test1"));
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
        // i.e. start transaction
        Client client = new Client("time", 0L, 0L);
        ServerUtils.resetGame(client);

    }

    @After
    public void tearDown() throws Exception {
        // clean up after every test
        // i.e. end transaction

    }

    @Test
    public void myTest() {
        try {
            // do stuff
            Player[] players = {null, null, null, null};
            DataModel model = injector.getInstance(DataModel.class);
            model.setBank(new Bank());
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
