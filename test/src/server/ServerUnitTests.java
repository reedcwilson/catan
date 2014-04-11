package src.server;

import org.junit.* ;
import static org.junit.Assert.* ;

public class ServerUnitTests {

    @Test
    public void test_1() {
        assertEquals("OK", "OK");
        assertTrue(true);
        assertFalse(false);
    }

    public static void main(String[] args) {

        String[] testClasses = new String[] {
                "src.server.ServerUnitTests",
                "src.datamodel.DataModelTests",
                "src.datamodel.hexgrid.HexGridTests"
        };
        org.junit.runner.JUnitCore.main(testClasses);
    }

}

