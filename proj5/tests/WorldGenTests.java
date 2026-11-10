import core.AutograderBuddy;
import core.World;
import edu.princeton.cs.algs4.StdDraw;
import org.junit.jupiter.api.Test;
import tileengine.TERenderer;
import tileengine.TETile;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorldGenTests {
    @Test
    public void basicTest() {
        // put different seeds here to test different worlds
        TETile[][] tiles = AutograderBuddy.getWorldFromInput("n1234567890123456789s");

        TERenderer ter = new TERenderer();
        ter.initialize(tiles.length, tiles[0].length);
        ter.renderFrame(tiles);
        StdDraw.pause(5000); // pause for 5 seconds so you can see the output
    }

    @Test
    public void basicInteractivityTest() {
        TETile[][] world1 =
                AutograderBuddy.getWorldFromInput("n123swasdwasd");

        TETile[][] world2 =
                AutograderBuddy.getWorldFromInput("n123swasdwasd");

        assertNotNull(world1);
        assertNotNull(world2);

        assertTrue(Arrays.deepEquals(world1, world2),
                "The same seed and input should generate the same world.");
    }

    @Test
    public void basicSaveTest() {
        TETile[][] savedWorld =
                AutograderBuddy.getWorldFromInput("n123swasd:q");

        TETile[][] loadedWorld =
                AutograderBuddy.getWorldFromInput("lwasd");

        TETile[][] expectedWorld =
                AutograderBuddy.getWorldFromInput("n123swasdwasd");

        assertNotNull(savedWorld);
        assertNotNull(loadedWorld);

        assertTrue(Arrays.deepEquals(expectedWorld, loadedWorld),
                "Loading a saved game and continuing should match "
                        + "playing the same moves without saving.");
    }
    @Test
    public void debugDeterminismTest() {
        World world1 = new World(123);
        World world2 = new World(123);

        System.out.println(
                "World deterministic: "
                        + TETile.toString(world1.getWorldMap()).equals(
                        TETile.toString(world2.getWorldMap())
                )
        );

        TETile[][] game1 = AutograderBuddy.getWorldFromInput("n123s");
        TETile[][] game2 = AutograderBuddy.getWorldFromInput("n123s");

        System.out.println(
                "Input deterministic: "
                        + TETile.toString(game1).equals(TETile.toString(game2))
        );
    }
}
