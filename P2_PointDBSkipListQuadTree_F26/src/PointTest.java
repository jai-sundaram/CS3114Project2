import student.TestCase;

/**
 * Tests the Point class.
 *
 * @author Jai Sundaram
 *
 * @version 2026-10-11
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
