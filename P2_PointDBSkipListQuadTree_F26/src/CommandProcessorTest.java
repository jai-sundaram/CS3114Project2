import student.TestCase;
import student.TestableRandom;

// On my honor:
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or unmodified.
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
// - I have not discussed coding details about this project with
// anyone other than the instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

/**
 * Tests the CommandProcessor and Database classes by feeding in command
 * lines and checking the output.
 *
 * @author Jai Sundaram, Yaswanth Uppalapati
 *
 * @version 2026-10-25
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
            "remove 1 -1", "remove 1 1", "regionsearch -5 -5 20 20",
            "regionsearch 5    5   4   -2");
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
            + "Points intersecting region (-5, -5, 20, 20):\n"
            + "1 quadtree nodes visited\n"
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
            "dump", "duplicates", "search p_p",
            "regionsearch 0 0 25 25", "remove p_p", "remove 10 30",
            "duplicates", "dump");
        assertFuzzyEquals("Point inserted: (p_p, 1, 20)\n"
            + "Point inserted: (poi, 10, 30)\n"
            + "Point inserted: (p_42, 1, 20)\n"
            + "Point inserted: (far, 200, 200)\n"
            + "SkipList dump:\n"
            + "Node has depth 4, Value (null)\n"
            + "Node has depth 4, Value (far, 200, 200)\n"
            + "Node has depth 2, Value (p_42, 1, 20)\n"
            + "Node has depth 1, Value (p_p, 1, 20)\n"
            + "Node has depth 1, Value (poi, 10, 30)\n"
            + "SkipList size is: 4\n"
            + "QuadTree dump:\n"
            + "Node at 0, 0, 1024: Internal\n"
            + "  Node at 0, 0, 512: Internal\n"
            + "    Node at 0, 0, 256: Internal\n"
            + "      Node at 0, 0, 128:\n"
            + "      (p_p, 1, 20)\n"
            + "      (poi, 10, 30)\n"
            + "      (p_42, 1, 20)\n"
            + "      Node at 128, 0, 128: Empty\n"
            + "      Node at 0, 128, 128: Empty\n"
            + "      Node at 128, 128, 128:\n"
            + "      (far, 200, 200)\n"
            + "    Node at 256, 0, 256: Empty\n"
            + "    Node at 0, 256, 256: Empty\n"
            + "    Node at 256, 256, 256: Empty\n"
            + "  Node at 512, 0, 512: Empty\n"
            + "  Node at 0, 512, 512: Empty\n"
            + "  Node at 512, 512, 512: Empty\n"
            + "13 quadtree nodes printed\n"
            + "Duplicate points:\n(1, 20)\n"
            + "Found (p_p, 1, 20)\n"
            + "Points intersecting region (0, 0, 25, 25):\n"
            + "Point found: (p_p, 1, 20)\n"
            + "Point found: (p_42, 1, 20)\n"
            + "4 quadtree nodes visited\n"
            + "Point removed: (p_p, 1, 20)\n"
            + "Point removed: (poi, 10, 30)\n"
            + "Duplicate points:\n"
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
        assertTrue(out.contains("1 quadtree nodes visited"));
        assertTrue(out.contains("Rectangle rejected: (5, 5, 0, 4)"));
    }


    /**
     * Tests duplicate reporting, including several names at one coordinate
     * and a leaf containing two distinct duplicate groups.
     */
    public void testDuplicates() {
        run("insert a 10 10", "insert b 10 10", "insert c 20 20",
            "duplicates", "insert d 20 20", "duplicates", "remove a",
            "duplicates");
        assertFuzzyEquals("Point inserted: (a, 10, 10)\n"
            + "Point inserted: (b, 10, 10)\n"
            + "Point inserted: (c, 20, 20)\n"
            + "Duplicate points:\n(10, 10)\n"
            + "Point inserted: (d, 20, 20)\n"
            + "Duplicate points:\n(10, 10)\n(20, 20)\n"
            + "Point removed: (a, 10, 10)\n"
            + "Duplicate points:\n(20, 20)\n",
            systemOut().getHistory());
    }


    /**
     * Tests half-open query edges and recursive quadrant pruning.
     */
    public void testRegionsearchResultsAndVisits() {
        run("insert nw 1 1", "insert ne 700 1", "insert sw 1 700",
            "insert se 700 700", "regionsearch 0 0 512 512",
            "regionsearch 512 0 512 512", "regionsearch 1024 1024 5 5");
        String out = systemOut().getHistory();
        assertTrue(out.contains("Points intersecting region (0, 0, 512, "
            + "512):\nPoint found: (nw, 1, 1)\n2 quadtree nodes visited"));
        assertTrue(out.contains("Points intersecting region (512, 0, 512, "
            + "512):\nPoint found: (ne, 700, 1)\n2 quadtree nodes visited"));
        assertTrue(out.contains("Points intersecting region (1024, 1024, "
            + "5, 5):\n1 quadtree nodes visited"));
    }


    /**
     * Tests a query whose arithmetic would overflow a 32-bit integer.
     */
    public void testRegionsearchLargeRectangle() {
        run("insert corner 1023 1023",
            "regionsearch -2147483648 -2147483648 2147483647 2147483647",
            "regionsearch 0 0 2147483647 2147483647");
        String out = systemOut().getHistory();
        assertTrue(out.contains("Points intersecting region (0, 0, "
            + "2147483647, 2147483647):\n"
            + "Point found: (corner, 1023, 1023)\n"
            + "1 quadtree nodes visited"));
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
