import java.util.ArrayList;
import java.util.Iterator;
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
 * Tests the SkipList class.
 *
 * @author Jai Sundaram, Yaswanth Uppalapati
 *
 * @version 2026-10-25
 */
public class SkipListTest extends TestCase {

    private SkipList<String, Point> sl;

    /**
     * Creates an empty SkipList before each test.
     */
    public void setUp() {
        sl = new SkipList<String, Point>();
        systemOut().clearHistory();
    }


    /**
     * Inserts a pair with the given name and location.
     *
     * @param name
     *            the key
     * @param x
     *            the x-coordinate
     * @param y
     *            the y-coordinate
     * @return the point that was inserted
     */
    private Point add(String name, int x, int y) {
        Point p = new Point(name, x, y);
        sl.insert(new KVPair<String, Point>(name, p));
        return p;
    }


    /**
     * Tests randomLevel with preset values.
     */
    public void testRandomLevel() {
        TestableRandom.setNextBooleans(false, true, true, true, false);
        assertEquals(1, sl.randomLevel());
        assertEquals(4, sl.randomLevel());
    }


    /**
     * Tests the dump of an empty list.
     */
    public void testDumpEmpty() {
        sl.dump();
        assertFuzzyEquals("SkipList dump:\nNode has depth 1, Value (null)\n"
            + "SkipList size is: 0\n", systemOut().getHistory());
    }


    /**
     * Tests that inserts are sorted and depths match the preset values.
     */
    public void testInsertAndDump() {
        TestableRandom.setNextBooleans(false, false, true, false, true,
            true, true, false);
        add("p_p", 1, 20);
        add("poi", 10, 30);
        add("p_42", 1, 20);
        add("far", 200, 200);
        assertEquals(4, sl.size());
        sl.dump();
        assertFuzzyEquals("SkipList dump:\n"
            + "Node has depth 4, Value (null)\n"
            + "Node has depth 4, Value (far, 200, 200)\n"
            + "Node has depth 2, Value (p_42, 1, 20)\n"
            + "Node has depth 1, Value (p_p, 1, 20)\n"
            + "Node has depth 1, Value (poi, 10, 30)\n"
            + "SkipList size is: 4\n", systemOut().getHistory());
    }


    /**
     * Tests search, including duplicate keys and missing keys.
     */
    public void testSearch() {
        add("b", 1, 1);
        add("a", 2, 2);
        add("b", 3, 3);
        add("c", 4, 4);
        ArrayList<KVPair<String, Point>> found = sl.search("b");
        assertEquals(2, found.size());
        assertEquals("b", found.get(0).key());
        assertEquals("b", found.get(1).key());
        assertEquals(0, sl.search("bb").size());
        assertEquals(0, sl.search("z").size());
        assertEquals(0, sl.search("A").size());
    }


    /**
     * Tests remove by key.
     */
    public void testRemove() {
        assertNull(sl.remove("a"));
        Point a = add("a", 1, 1);
        add("c", 2, 2);
        assertNull(sl.remove("b"));
        assertNull(sl.remove("d"));
        assertSame(a, sl.remove("a").value());
        assertEquals(1, sl.size());
        assertNull(sl.remove("a"));
    }


    /**
     * Tests remove by value, which must remove that exact object.
     */
    public void testRemoveByValue() {
        TestableRandom.setNextBooleans(true, true, false, false, true,
            false);
        Point a1 = add("a", 5, 5);
        Point a2 = add("a", 5, 5);
        Point b = add("b", 6, 6);
        assertNull(sl.removeByValue(new Point("a", 5, 5)));
        assertSame(b, sl.removeByValue(b).value());
        assertSame(a1, sl.removeByValue(a1).value());
        assertEquals(1, sl.size());
        assertSame(a2, sl.search("a").get(0).value());
    }


    /**
     * Tests the iterator.
     */
    public void testIterator() {
        add("b", 1, 1);
        add("a", 2, 2);
        Iterator<KVPair<String, Point>> it = sl.iterator();
        assertTrue(it.hasNext());
        assertEquals("a", it.next().key());
        assertEquals("b", it.next().key());
        assertFalse(it.hasNext());
    }
}
