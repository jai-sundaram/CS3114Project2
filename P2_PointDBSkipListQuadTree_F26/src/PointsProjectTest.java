import student.TestCase;

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
 * Tests the PointsProject main method.
 *
 * @author Jai Sundaram, Yaswanth Uppalapati
 *
 * @version 2026-10-25
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
