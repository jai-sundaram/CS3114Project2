import student.TestCase;

/**
 * Tests the PointsProject main method.
 *
 * @author Jai Sundaram, Yaswanth Uppalapati
 *
 * @version 2026-10-11
 */
public class PointsProjectTest extends TestCase {

    /**
     * Starts capturing output before each test.
     */
    public void setUp() {
        systemOut().clearHistory();
    }


    /**
     * Tests main without a command file.
     *
     * @throws Exception
     *             if main fails
     */
    public void testNoArgs() throws Exception {
        PointsProject.main(new String[0]);
        assertFuzzyEquals("Usage: PointsProject <command-file>\n",
            systemOut().getHistory());
    }


    /**
     * Tests main with a command file that does not exist.
     *
     * @throws Exception
     *             if main fails
     */
    public void testMissingFile() throws Exception {
        PointsProject.main(new String[] { "noSuchFile.txt" });
        assertFuzzyEquals("There is no such input file as |noSuchFile.txt|\n",
            systemOut().getHistory());
    }
}
