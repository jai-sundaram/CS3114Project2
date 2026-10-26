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
 * Tests the Point class.
 *
 * @author Jai Sundaram, Yaswanth Uppalapati
 *
 * @version 2026-10-25
 */
public class PointTest extends TestCase {

    private Point p;

    /**
     * Creates a point before each test.
     */
    public void setUp() {
        p = new Point("a", 3, 4);
    }


    /**
     * Tests the getters and string forms.
     */
    public void testGettersAndStrings() {
        assertEquals("a", p.getName());
        assertEquals(3, p.getX());
        assertEquals(4, p.getY());
        assertEquals("3, 4", p.toString());
        assertEquals("(a, 3, 4)", p.fullString());
    }


    /**
     * Tests sameLocation.
     */
    public void testSameLocation() {
        assertTrue(p.sameLocation(new Point("b", 3, 4)));
        assertFalse(p.sameLocation(new Point("b", 3, 5)));
        assertFalse(p.sameLocation(new Point("b", 2, 4)));
    }


    /**
     * Tests the world box boundaries.
     */
    public void testInWorld() {
        assertTrue(Point.inWorld(0, 0));
        assertTrue(Point.inWorld(1023, 1023));
        assertFalse(Point.inWorld(-1, 0));
        assertFalse(Point.inWorld(0, -1));
        assertFalse(Point.inWorld(1024, 0));
        assertFalse(Point.inWorld(0, 1024));
    }
}
