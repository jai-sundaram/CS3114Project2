import student.TestCase;
import student.TestableRandom;

/**
 * Tests the CommandProcessor and Database classes by feeding in command
 * lines and checking the output.
 *
 * @author Jai Sundaram
 *
 * @version 2026-10-11
 */
public class CommandProcessorTest extends TestCase {

    private CommandProcessor cp;

    /**
     * Creates a fresh processor before each test.
     */
    public void setUp() {
        cp = new CommandProcessor();
        systemOut().clearHistory();
    }


    /**
     * Runs each line through the processor.
     *
     * @param lines
     *            the command lines
     */
    private void run(String... lines) {
        for (String line : lines) {
            cp.processor(line);
        }
    }


    /**
     * Tests the commands from SyntaxTest1.
     */
    public void testSyntax1() {
        run("", "insert p_p          -1 -20", "  insert poi        7 -8",
            "   ", "duplicates", "dump", "search p_p", "remove p_p",
            "remove 1 -1", "remove 1 1", "regionsearch 5    5   4   -2");
        assertFuzzyEquals("Point rejected: (p_p, -1, -20)\n"
            + "Point rejected: (poi, 7, -8)\n"
            + "Duplicate points:\n"
            + "SkipList dump:\n"
            + "Node has depth 1, Value (null)\n"
            + "SkipList size is: 0\n"
            + "QuadTree dump:\n"
            + "Node at 0, 0, 1024: Empty\n"
            + "1 quadtree nodes printed\n"
            + "Point not found: p_p\n"
            + "Point not removed: p_p\n"
            + "Point rejected: (1, -1)\n"
            + "Point not found: (1, 1)\n"
            + "Rectangle rejected: (5, 5, 4, -2)\n",
            systemOut().getHistory());
    }


    /**
     * Tests insert, search, remove and dump from SyntaxTest2.
     */
    public void testSyntax2() {
        TestableRandom.setNextBooleans(false, false, true, false, true,
            true, true, false);
        run("insert p_p          1 20", "  insert poi        10 30",
            "  insert p_42   1 20", "  insert far  200 200",
            "search p_p", "remove p_p", "remove 10 30", "dump");
        assertFuzzyEquals("Point inserted: (p_p, 1, 20)\n"
            + "Point inserted: (poi, 10, 30)\n"
            + "Point inserted: (p_42, 1, 20)\n"
            + "Point inserted: (far, 200, 200)\n"
            + "Found (p_p, 1, 20)\n"
            + "Point removed: (p_p, 1, 20)\n"
            + "Point removed: (poi, 10, 30)\n"
            + "SkipList dump:\n"
            + "Node has depth 4, Value (null)\n"
            + "Node has depth 4, Value (far, 200, 200)\n"
            + "Node has depth 2, Value (p_42, 1, 20)\n"
            + "SkipList size is: 2\n"
            + "QuadTree dump:\n"
            + "Node at 0, 0, 1024:\n"
            + "(far, 200, 200)\n"
            + "(p_42, 1, 20)\n"
            + "1 quadtree nodes printed\n", systemOut().getHistory());
    }


    /**
     * Tests searching for a name shared by several points, and the world
     * box limits on insert and remove.
     */
    public void testSearchMultipleAndBounds() {
        run("insert ursa 343 213", "insert ursa 514 42",
            "insert big 1024 5", "insert big 5 1024", "search ursa",
            "remove 1025 3", "remove -3 2", "remove 3 1024");
        String out = systemOut().getHistory();
        assertTrue(out.contains("Found (ursa, 343, 213)"));
        assertTrue(out.contains("Found (ursa, 514, 42)"));
        assertTrue(out.contains("Point rejected: (big, 1024, 5)"));
        assertTrue(out.contains("Point rejected: (big, 5, 1024)"));
        assertTrue(out.contains("Point rejected: (1025, 3)"));
        assertTrue(out.contains("Point rejected: (-3, 2)"));
        assertTrue(out.contains("Point rejected: (3, 1024)"));
    }


    /**
     * Tests that a remove by name also removes the point from the
     * QuadTree, and a remove by location also removes it from the
     * SkipList.
     */
    public void testRemoveKeepsInSync() {
        run("insert a 5 5", "insert b 6 6", "remove a", "remove 5 5",
            "remove 6 6", "search b");
        String out = systemOut().getHistory();
        assertTrue(out.contains("Point not found: (5, 5)"));
        assertTrue(out.contains("Point removed: (b, 6, 6)"));
        assertTrue(out.contains("Point not found: b"));
    }


    /**
     * Tests the region search rejection and header.
     */
    public void testRegionsearch() {
        run("regionsearch   -5 -5 20 20 ", "regionsearch 5 5 0 4");
        String out = systemOut().getHistory();
        assertTrue(out.contains("Points intersecting region (-5, -5, 20, "
            + "20):"));
        assertTrue(out.contains("Rectangle rejected: (5, 5, 0, 4)"));
    }


    /**
     * Tests unrecognized commands.
     */
    public void testUnrecognized() {
        run("bogus", "insert a 1", "remove 1 2 3", "regionsearch 1",
            "duplicates 1", "search", "dump now");
        String out = systemOut().getHistory();
        assertTrue(out.contains("Unrecognized command: bogus"));
        assertTrue(out.contains("Unrecognized command: dump now"));
    }
}
