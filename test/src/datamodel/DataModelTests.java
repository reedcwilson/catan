package src.datamodel;

import org.junit.*;
import static org.junit.Assert.* ;

public class DataModelTests {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // all things you want to do only once (i.e. initialize database driver)
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
    }

    @After
    public void tearDown() throws Exception {
        // clean up after every test
        // i.e. end transaction
    }

    @Test
    public void testRandomTestName() {
        try {
            // do stuff

            // these are the possible assertions
            assertTrue(true);
            assertFalse(false);
            assertSame(true, true);
            assertEquals(1, 1);
        } catch (Exception e) {
            // print stuff
        }
    }

    @Test(expected = Exception.class)
    public void testAnotherRandomTestName() throws Exception {
        throw new Exception();
        // test things that should throw an exception
    }
}
